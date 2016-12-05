/*
 * Copyright (c) 2016, Samuel Savage
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package com.su.folcis667;

import com.su.folcis667.match3.Cell;
import com.su.folcis667.match3.Matched;
import com.su.folcis667.match3.NeighborX;
import com.su.folcis667.match3.NeighborY;
import java.util.ArrayList;

public class Match3Game {

    public class MatchingPair {

        public final Cell mLeft;
        public final Cell mRight;

        MatchingPair(Cell left, Cell right) {
            mLeft = left;
            mRight = right;
        }
    }

    public final Cell[][] mCells;
    private final String[] mColors;

    public Match3Game(int cols, int rows, int colors) {
        mCells = new Cell[rows][cols];
        mColors = GenerateColors(colors);
        InitializeCells();
    }

    public Match3Game(Cell[][] cells) {
        mCells = cells;
        mColors = GenerateColors(0);
//        mCells = new Cell[cells.length][cells[0].length];  // java is the worst
//        for (int i = 0; i < mCells.length; ++i) {
//            for (int j = 0; j < mCells[i].length; ++i) {
//                mCells[i][j] = new Cell(cells[i][j]);
//            }
//        }
    }

    public Cell[][] GetNextState(MatchingPair change) {
        Cell[][] rval = new Cell[mCells.length][mCells[0].length];  // java is the worst
        for (int i = 0; i < mCells.length; ++i) {
            for (int j = 0; j < mCells[i].length; ++j) {
                if (mCells[i][j] == change.mLeft) {
                    rval[i][j] = new Cell(change.mRight);
                    rval[i][j].x(j);
                    rval[i][j].y(i);
                } else if (mCells[i][j] == change.mRight) {
                    rval[i][j] = new Cell(change.mLeft);
                    rval[i][j].x(j);
                    rval[i][j].y(i);
                } else {
                    rval[i][j] = new Cell(mCells[i][j]);
                }
            }
        }
        return rval;
    }

    public void RemoveMatches() {
        while (ClearMatches()) {
            ShiftCells();
        }
        return;
    }

    private boolean ClearMatches() {
        boolean any_match = false;
        for (int i = 0; i < mCells.length; ++i) {
            for (int j = 0; j < mCells[i].length; ++j) {
                if (!Matched.IGNORE_COLOR.equals(mCells[i][j].c())
                        && HasMatchingNeighbor(mCells[i][j], true)) {
                    any_match |= mCells[i][j].color().isOpaque();
                    mCells[i][j].invisible(true);
                }
            }
        }
        return any_match;
    }

    private boolean ShiftCells() {
        //find all the cells that have been set to matched, and move the cells above down.
        // iterate backwards for efficiency
        for (int i = mCells.length - 1; i >= 0; --i) {
            for (int j = mCells[i].length - 1; j >= 0; --j) {
                if (!mCells[i][j].color().isOpaque()) {
                    for (int k = i; k > 0; --k) {
                        Cell update = new Cell(mCells[k - 1][j]);
                        update.y(mCells[k][j].y());
                        mCells[k][j] = update;
                    }
                    mCells[0][j] = new Cell(mCells[0][j]);
                    mCells[0][j].c(Matched.IGNORE_COLOR);
                    mCells[0][j].invisible(false);
                    ++j;
                }
            }
        }
        return false;
    }

    public ArrayList<MatchingPair> GetMatchableCells() {
        ArrayList<MatchingPair> rval = new ArrayList<>();
        for (int i = 0; i < mCells.length; ++i) {
            for (int j = 0; j < mCells[i].length; ++j) {
                if (SwapCreatesMatch(i, j, i, j + 1)) {
                    rval.add(new MatchingPair(mCells[i][j], mCells[i][j + 1]));
                }
                if (SwapCreatesMatch(i, j, i + 1, j)) {
                    rval.add(new MatchingPair(mCells[i][j], mCells[i + 1][j]));
                }
            }
        }
        return rval;
    }

    private boolean SwapCreatesMatch(int row1, int col1, int row2, int col2) {
        boolean rval = false;
        if (IsValidSwap(row1, col1, row2, col2)) {
            Cell old_left = new Cell(mCells[row1][col1]);
            Cell old_right = new Cell(mCells[row2][col2]);
            mCells[row1][col1].x(old_right.x());
            mCells[row1][col1].y(old_right.y());
            mCells[row2][col2].x(old_left.x());
            mCells[row2][col2].y(old_left.y());
            rval = HasMatchingNeighbor(mCells[row1][col1]) || HasMatchingNeighbor(mCells[row2][col2]);
            mCells[row1][col1].x(old_left.x());
            mCells[row1][col1].y(old_left.y());
            mCells[row2][col2].x(old_right.x());
            mCells[row2][col2].y(old_right.y());
        }
        return rval;
    }

    private boolean IsRealCell(int row, int col) {
        return row >= 0 && row < mCells.length
                && col >= 0 && col < mCells[row].length;
    }

    private boolean IsValidSwap(int row1, int col1, int row2, int col2) {
        return IsRealCell(row1, col1) && IsRealCell(row2, col2)
                && (NeighborX.test(mCells[row1][col1], mCells[row2][col2])
                || NeighborY.test(mCells[row1][col1], mCells[row2][col2]));
    }

    private boolean HasMatchingNeighbor(Cell test) {
        return HasMatchingNeighbor(test, false);
    }

    private boolean HasMatchingNeighbor(Cell test, boolean ignoreInvisible) {
        boolean matched = false;
        if (!matched && IsRealCell(test.x() - 2, test.y())) {
            matched = Matched.test(mCells[test.y()][test.x() - 2],
                    mCells[test.y()][test.x() - 1],
                    test, ignoreInvisible);
        }
        if (!matched && IsRealCell(test.x() - 1, test.y())
                && IsRealCell(test.x() + 1, test.y())) {
            matched = Matched.test(mCells[test.y()][test.x() - 1],
                    test,
                    mCells[test.y()][test.x() + 1], ignoreInvisible);
        }
        if (!matched && IsRealCell(test.x() + 2, test.y())) {
            matched = Matched.test(test,
                    mCells[test.y()][test.x() + 1],
                    mCells[test.y()][test.x() + 2], ignoreInvisible);
        }
        if (!matched && IsRealCell(test.x(), test.y() - 2)) {
            matched = Matched.test(mCells[test.y() - 2][test.x()],
                    mCells[test.y() - 1][test.x()],
                    test, ignoreInvisible);
        }
        if (!matched && IsRealCell(test.x(), test.y() - 1)
                && IsRealCell(test.x(), test.y() + 1)) {
            matched = Matched.test(mCells[test.y() - 1][test.x()],
                    test,
                    mCells[test.y() + 1][test.x()], ignoreInvisible);
        }
        if (!matched && IsRealCell(test.x(), test.y() + 2)) {
            matched = Matched.test(test,
                    mCells[test.y() + 1][test.x()],
                    mCells[test.y() + 2][test.x()], ignoreInvisible);
        }
        return matched;
    }

    private void InitializeCells() {
        for (int i = 0; i < mCells.length; ++i) {
            for (int j = 0; j < mCells[i].length; ++j) {
                mCells[i][j] = new Cell("Cell_" + Integer.toString(j) + "_" + Integer.toString(i));
                int index = (i == mCells.length - 1) ? (i + j + 1) % mColors.length : (i + j) % mColors.length;
                String value = mColors[index];
                value += "," + Integer.toString(j) + "," + Integer.toString(i);
                mCells[i][j].set(value);
            }
        }
    }

    private static String[] GenerateColors(int numColors) {
        switch (numColors) {
            case 0:
                return new String[]{};
            case 1:
                return new String[]{"red"};
            case 2:
                return new String[]{"red", "blue"};
            case 3:
                return new String[]{"red", "blue", "green"};
            case 4:
                return new String[]{"red", "blue", "green", "orange"};
            case 5:
                return new String[]{"red", "blue", "green", "orange", "purple"};
            case 6:
                return new String[]{"red", "blue", "green", "orange", "purple", "yellow"};
            case 7:
                return new String[]{"red", "blue", "green", "orange", "purple", "yellow", "white"};
            case 8:
                return new String[]{"red", "blue", "green", "orange", "purple", "yellow", "white", "cyan"};
            case 9:
                return new String[]{"red", "blue", "green", "orange", "purple", "yellow", "white", "cyan", "pink"};
            case 10:
                return new String[]{"red", "blue", "green", "orange", "purple", "yellow", "white", "cyan", "pink", "yellowgreen"};
        }
        return null;
    }

}
