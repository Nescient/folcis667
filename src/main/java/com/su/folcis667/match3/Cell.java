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

import net.sf.tweety.logics.commons.syntax.Variable;

public class Cell extends Variable {

    final String DELIMITER = ",";

    public Cell(String name) {
        super(name);
    }

    /**
     * This parses strings of the form [color, x, y].
     *
     * @param value The value of this variable.
     */
    @Override
    public void set(String value) {
        super.set(value.replaceAll("[^a-zA-Z0-9,]", "").toUpperCase());
    }

    public String c() {
        return this.value.split(DELIMITER)[0];
        //Color.web("orange", 0.5);
    }
    
    public String c_str(){
        return this.value.split(DELIMITER)[0];
    }

    public int x() {
        return Integer.parseInt(this.value.split(DELIMITER)[1]);
    }

    public int y() {
        return Integer.parseInt(this.value.split(DELIMITER)[2]);
    }

}
