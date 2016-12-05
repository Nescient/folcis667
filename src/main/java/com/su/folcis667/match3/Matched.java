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
package com.su.folcis667.match3;

import javafx.scene.paint.Color;
import net.sf.tweety.logics.commons.syntax.Predicate;

public class Matched extends Predicate {

    public Matched() {
        super("Matched", 3 /*arity*/);
    }

    private static boolean color_match(Color a, Color b) {
        return a.getRed() == b.getRed()
                && a.getBlue() == b.getBlue()
                && a.getGreen() == b.getGreen();
    }

    public static boolean test(Cell A, Cell B, Cell C) {
        return Aligned.test(A, B, C)
                && color_match(A.color(), B.color())
                && color_match(B.color(), C.color());
    }

    public static boolean test(Cell A, Cell B, Cell C, boolean ignoreInvisible) {
        boolean all_cells_visible = A.color().isOpaque()
                && B.color().isOpaque()
                && C.color().isOpaque();
        return Matched.test(A, B, C) && (all_cells_visible || ignoreInvisible);
    }
}
