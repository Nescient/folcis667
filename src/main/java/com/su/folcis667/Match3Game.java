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
import java.util.Vector;

public class Match3Game {

    private final Cell[][] mCells;
    private final String[] mColors;

    public Match3Game(int cols, int rows, int colors) {
        mCells = new Cell[cols][rows];
        mColors = GenerateColors(colors);
        InitializeCells();
    }

    private void InitializeCells() {
        for (int i = 0; i < mCells.length; ++i) {
            for (int j = 0; j < mCells[i].length; ++j) {
                mCells[i][j] = new Cell("Cell_"+Integer.toString(i) + "_" + Integer.toString(j));
                String value = mColors[(i + j) % mColors.length];
                value += "," + Integer.toString(i) + "," + Integer.toString(j);
                mCells[i][j].set(value);
                System.out.println(mCells[i][j].c());
                System.out.println(mCells[i][j].color());
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
