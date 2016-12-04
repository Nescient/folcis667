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
        mCells = new Cell[cols][rows];
        mColors = GenerateColors(colors);
        InitializeCells();
    }

    public ArrayList<Cell[][]> GetNextState() {
        ArrayList<Cell[][]> rval = new ArrayList<>();
        return rval;
    }

    public ArrayList<MatchingPair> GetMatchableCells() {
        ArrayList<MatchingPair> rval = new ArrayList<>();
        for (int i = 0; i < mCells.length; ++i) {
            for (int j = 0; j < mCells[i].length; ++j) {
                if (SwapCreatesMatch(i, j, i, j + 1)) {
                    rval.add(new MatchingPair(mCells[i][j], mCells[i][j]));
                }
            }
        }
        return rval;
    }

    private boolean SwapCreatesMatch(int row1, int col1, int row2, int col2) {
        if (IsValidSwap(row1, col1, row2, col2)) {
            Cell right = new Cell(mCells[row1][col1]);  // do the swap
            Cell left = new Cell(mCells[row2][col2]);  // do the swap
            return HasMatchingNeighbor(right) || HasMatchingNeighbor(left);
        }
        return false;
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
        boolean matched = false;
        if (!matched && IsRealCell(test.x() - 2, test.y())) {
            matched = Matched.test(mCells[test.y()][test.x() - 2],
                    mCells[test.y()][test.x() - 1],
                    test);
        }
        if (!matched && IsRealCell(test.x() - 1, test.y())
                && IsRealCell(test.x() + 1, test.y())) {
            matched = Matched.test(mCells[test.y()][test.x() - 1],
                    test,
                    mCells[test.y()][test.x() + 1]);
        }
        if (!matched && IsRealCell(test.x() + 2, test.y())) {
            matched = Matched.test(test,
                    mCells[test.y()][test.x() + 1],
                    mCells[test.y()][test.x() + 2]);
        }
        if (!matched && IsRealCell(test.x(), test.y() - 2)) {
            matched = Matched.test(mCells[test.y() - 2][test.x()],
                    mCells[test.y() - 1][test.x()],
                    test);
        }
        if (!matched && IsRealCell(test.x(), test.y() - 1)
                && IsRealCell(test.x(), test.y() + 1)) {
            matched = Matched.test(mCells[test.y() - 1][test.x()],
                    test,
                    mCells[test.y() + 1][test.x()]);
        }
        if (!matched && IsRealCell(test.x(), test.y() + 2)) {
            matched = Matched.test(test,
                    mCells[test.y() + 1][test.x()],
                    mCells[test.y() + 2][test.x()]);
        }
        return matched;
    }

    private void InitializeCells() {
        for (int i = 0; i < mCells.length; ++i) {
            for (int j = 0; j < mCells[i].length; ++j) {
                mCells[i][j] = new Cell("Cell_" + Integer.toString(i) + "_" + Integer.toString(j));
                String value = mColors[(i + j) % mColors.length];
                value += "," + Integer.toString(i) + "," + Integer.toString(j);
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
