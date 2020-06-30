package com.droidlabs.pocketcontrol.utils;

import java.util.Locale;

public final class CurrencyUtils {

    private static final Locale DEFAULT_LOCALE = Locale.GERMANY;
    public static final String DEFAULT_CURRENCY = "€";

    /**
     * Private constructor to prevent extending utility classes.
     */
    private CurrencyUtils() { }

    /**
     * Formats amount with default currency.
     * @param amount amount to be formatted.
     * @return formatted amount.
     */
    public static String formatAmount(final float amount) {
        return String.format(DEFAULT_LOCALE, DEFAULT_CURRENCY + " %.2f", amount);
    }

    /**
     * Formats amount with custom currency.
     * @param amount amount to be formatted.
     * @param currency currency to be used.
     * @return formatted amount.
     */
    public static String formatAmount(final float amount, final String currency) {
        return String.format(DEFAULT_LOCALE, currency + " %.2f", amount);
    }
}
