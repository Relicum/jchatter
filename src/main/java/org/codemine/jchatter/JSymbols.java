/*
 * The MIT License
 *
 * Copyright (c) 2014 Relicum
 *
 * The following authors supplied code that this Lib is based on @Spoonyloony @bobacadodl @dorkrepublic
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package org.codemine.jchatter;

/**
 * Name: JSymbols.java Created: 27 June 2014
 *
 * @author Relicum
 * @version 0.0.1
 */
public enum JSymbols {

    BLACK_CHESS_PAWN('\u2654'),
    WHITE_CHESS_KING('\u2655'),
    WHITE_CHESS_QUEEN('\u2656'),
    WHITE_CHESS_ROOK('\u2657'),
    WHITE_CHESS_BISHOP('\u2658'),
    WHITE_CHESS_KNIGHT('\u2659'),
    WHITE_CHESS_PAWN('\u265A'),
    BLACK_CHESS_KING('\u265B'),
    BLACK_CHESS_QUEEN('\u265C'),
    BLACK_CHESS_ROOK('\u265D'),
    BLACK_CHESS_BISHOP('\u265E'),
    BLACK_CHESS_KNIGHT('\u265F'),
    POINT_BLACK_LEFT('\u261A'),
    POINT_BLACK_RIGHT('\u261B'),
    EYES_AND_HEAD('\u268E'),
    CHECK_MARK('\u2713'),
    HEAVY_CHECK_MARK('\u2714'),
    BALLOT_X('\u2717'),
    HEAVY_BALLOT_X('\u2718'),
    HEAVY_BLACK_HEART('\u2764'),
    DIGIT_ONE('\u2776'),
    DIGIT_TWO('\u2777'),
    DIGIT_THREE('\u2778'),
    DIGIT_FOUR('\u2779'),
    DIGIT_FIVE('\u277A'),
    DIGIT_SIX('\u277B'),
    DIGIT_SEVEN('\u277C'),
    DIGIT_EIGHT('\u277D'),
    DIGIT_NINE('\u227E'),
    LIGHT_VERTICAL_BAR('\u2758'),
    MEDIUM_VERTICAL_BAR('\u2759'),
    HEAVY_VERTICAL_BAR('\u275A'),
    AIRPLANE('\u2708'),
    ENVELOPE('\u2709'),
    WHITE_STAR('\u2729'),
    CIRCLED_WHITE_STAR('\u272A');






    private final Character symbol;
    private final char toString;

    private JSymbols(Character p) {

        this.symbol = p;
        this.toString = p;
    }

    public String getSymbol() {

        return String.valueOf(this.symbol);
    }

    @Override
    public String toString() {
        return String.valueOf(toString);
    }


}


