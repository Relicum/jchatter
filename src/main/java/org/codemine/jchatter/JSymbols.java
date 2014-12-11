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
 * Simple way to add unicode symbols to your {@link org.codemine.jchatter.JChat} messages.
 * <p>The usage of unicode symbols is a valid format when using the TellRaw command and the JSON
 * chat formatting.The quote below is from http://minecraft.gamepedia.com/Tellraw under the Raw JSON format section.
 * As JChat uses tellraw to send the messages through the console.
 * <pre>
 *     Finally, unlike other commands using JSON, /tellraw Strings support Unicode via the notation
 *     &#92;u####, where #### is the Unicode hexadecimal number for the desired character.
 *</pre>
 *
 * @author Relicum
 * @version 0.0.1
 */
public enum JSymbols {

    /**
     * The POINT_BLACK_LEFT <strong>&amp;#x261A;</strong>
     */
    POINT_BLACK_LEFT('\u261A'),
    /**
     * The POINT_BLACK_RIGHT <strong>&amp;#x261B;</strong>
     */
    POINT_BLACK_RIGHT('\u261B'),
    /**
     * The CHECK_MARK <strong>&amp;#x2713;</strong>
     */
    CHECK_MARK('\u2713'),
    /**
     * The HEAVY_CHECK_MARK <strong>&amp;#x2714;</strong>
     */
    HEAVY_CHECK_MARK('\u2714'),
    /**
     * The BALLOT_X <strong>&amp;#x2717;</strong>
     */
    BALLOT_X('\u2717'),
    /**
     * The HEAVY_BALLOT_X <strong>&amp;#x2718;</strong>
     */
    HEAVY_BALLOT_X('\u2718'),
    /**
     * The HEAVY_BLACK_HEART <strong>&amp;#x2764;</strong>
     */
    HEAVY_BLACK_HEART('\u2764'),
    /**
     * The DIGIT_ONE <strong>&amp;#x2776;</strong>
     */
    DIGIT_ONE('\u2776'),
    /**
     * The DIGIT_TWO <strong>&amp;#x2777;</strong>
     */
    DIGIT_TWO('\u2777'),
    /**
     * The DIGIT_THREE <strong>&amp;#x2778;</strong>
     */
    DIGIT_THREE('\u2778'),
    /**
     * The DIGIT_FOUR <strong>&amp;#x2779;</strong>
     */
    DIGIT_FOUR('\u2779'),
    /**
     * The DIGIT_FIVE <strong>&amp;#x277A;</strong>
     */
    DIGIT_FIVE('\u277A'),
    /**
     * The DIGIT_SIX <strong>&amp;#x277B;</strong>
     */
    DIGIT_SIX('\u277B'),
    /**
     * The DIGIT_SEVEN <strong>&amp;#x277C;</strong>
     */
    DIGIT_SEVEN('\u277C'),
    /**
     * The DIGIT_EIGHT <strong>&amp;#x277D;</strong>
     */
    DIGIT_EIGHT('\u277D'),
    /**
     * The DIGIT_NINE <strong>&amp;#x277E;</strong>
     */
    DIGIT_NINE('\u227E'),
    /**
     * The LIGHT_VERTICAL_BAR <strong>&amp;#x2758;</strong>
     */
    LIGHT_VERTICAL_BAR('\u2758'),
    /**
     * The MEDIUM_VERTICAL_BAR <strong>&amp;#x2759;</strong>
     */
    MEDIUM_VERTICAL_BAR('\u2759'),
    /**
     * The HEAVY_VERTICAL_BAR <strong>&amp;#x275A;</strong>
     */
    HEAVY_VERTICAL_BAR('\u275A'),
    /**
     * The AIRPLANE <strong>&amp;#x2708;</strong>
     */
    AIRPLANE('\u2708'),
    /**
     * The ENVELOPE <strong>&amp;#x2709;</strong>
     */
    ENVELOPE('\u2709'),
    /**
     * The WHITE_STAR <strong>&amp;#x2729;</strong>
     */
    WHITE_STAR('\u2729'),
    /**
     * The CIRCLED_WHITE_STAR <strong>&amp;#x272A;</strong>
     */
    CIRCLED_WHITE_STAR('\u272A'),
    /**
     * The VICTORY_HAND <strong>&amp;#x270C;</strong>
     */
    VICTORY_HAND('\u270C'),
    /**
     * The BLACK_SCISSORS <strong>&amp;#x2702;</strong>
     */
    BLACK_SCISSORS('\u2702'),

    /**
     * The HEAVY_ARROW <strong>&amp;#x2794;</strong>
     */
    HEAVY_ARROW('\u2794'),

    /**
     * The THIN_ARROW <strong>&amp;#x2799;</strong>
     */
    THIN_ARROW('\u2799'),

    /**
     * The DOUBLE_QUOTE <strong>&amp;#x275E;</strong>
     */
    DOUBLE_QUOTE('\u275E'),

    /**
     * The SINGLE_QUOTE <strong>&amp;#x275C;</strong>
     */
    SINGLE_QUOTE('\u275C'),

    /**
     * The BLACK_DIAMOND <strong>&amp;#x2756;</strong>
     */
    BLACK_DIAMOND('\u2756'),

    /**
     * The BLACK_FLORETTE <strong>&amp;#x273F;</strong>
     */
    BLACK_FLORETTE('\u273F'),

    /**
     * The WHITE_FLORETTE <strong>&amp;#x2740;</strong>
     */
    WHITE_FLORETTE('\u2740');


    private final Character symbol;
    private final char toString;

    private JSymbols(Character p) {

        this.symbol = p;
        this.toString = p;
    }

    /**
     * Gets symbol.
     *
     * @return the symbol
     */
    public String getSymbol() {

        return String.valueOf(this.symbol);
    }

    /**
     * To string.
     *
     * @return the string
     */
    @Override
    public String toString() {
        return String.valueOf(toString);
    }


}


