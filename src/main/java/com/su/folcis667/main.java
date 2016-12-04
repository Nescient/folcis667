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
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import net.sf.tweety.commons.Formula;
import net.sf.tweety.logics.fol.FolBeliefSet;
import net.sf.tweety.logics.fol.parser.FolParser;
import net.sf.tweety.logics.fol.syntax.FolFormula;
import net.sf.tweety.logics.fol.syntax.QuantifiedFormula;

public class main {

    public static void main(String[] args) {
        System.out.println("hello world");
        FolParser fp = new FolParser();
        Cell test = new Cell("A");
        System.out.println(javafx.scene.paint.Color.RED);
        test.set("red, 0, 0");
        System.out.println(test.c());
        System.out.println(test.x());
        System.out.println(test.y());
        System.out.println(test.color());
        Match3Game asdf = new Match3Game(20, 20, 10);
        try {
            Formula i = new FolParser().parseFormula("forall X: forall Y: A(X,Y)");
            Formula one = fp.parseFormula("NeighborX(A,B) <=> A.y == B.y && ((A.x == B.x + 1) || (A.x == B.x-1))");
        } catch (Exception ex) {
            System.out.println(ex);
        }
        Set<FolFormula> formulas = new HashSet<>();
        //FolFormula x = new FolFormula
//                QuantifiedFormula y = new QuantifiedFormula
//        FolBeliefSet m3_believe = new FolBeliefSet();
    }
}
