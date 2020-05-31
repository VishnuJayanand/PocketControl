package com.droidlabs.pocketcontrol.utils;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public final class FormatterUtils {

    private static DecimalFormat df = new DecimalFormat("#.00");

    /**
     * Private constructor.
     */
    private FormatterUtils() { }

    /**
     * Formats a float with two decimal numbers.
     * @param number float.
     * @return formatted string.
     */
    public static String roundToTwoDecimals(final float number) {
        df.setRoundingMode(RoundingMode.HALF_EVEN);
        return df.format(number);
    }
}
