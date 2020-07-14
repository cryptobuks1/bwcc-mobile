package com.inovasialfatih.klinikbwcc.helper;

import android.text.Html;
import android.text.Spanned;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Created by MUTI on 12/12/2016.
 */

public class HStr {
    public static String formatCurrency(double amount, int precision, String pattern, Locale locale) {
        NumberFormat nf = NumberFormat.getCurrencyInstance(locale);
        DecimalFormat df = (DecimalFormat) nf;
        df.setMinimumFractionDigits(precision);
        df.setMaximumFractionDigits(precision);
        df.setDecimalSeparatorAlwaysShown(true);
        df.applyPattern(pattern);
        return df.format(amount);
    }

    public static String formatNumber(double amount, int precision, String pattern, Locale locale) {
        NumberFormat nf = NumberFormat.getNumberInstance(locale);
        DecimalFormat df = (DecimalFormat) nf;
        df.setMinimumFractionDigits(precision);
        df.setMaximumFractionDigits(precision);
        df.setDecimalSeparatorAlwaysShown(true);
        df.applyPattern(pattern);
        return df.format(amount);
    }

    public static String formatCurrency(double amount, int precision, String prefix) {
        DecimalFormat fm = (DecimalFormat) DecimalFormat.getCurrencyInstance();
        DecimalFormatSymbols fms = new DecimalFormatSymbols();

        fms.setCurrencySymbol(prefix);
        fms.setMonetaryDecimalSeparator(',');
        fms.setGroupingSeparator('.');

        fm.setDecimalFormatSymbols(fms);
        fm.setMinimumFractionDigits(precision);
        fm.setMaximumFractionDigits(precision);

        return fm.format(amount);
    }

    public static String formatNumber(double amount, int precision, Locale locale) {
        NumberFormat nf = NumberFormat.getNumberInstance(locale);
        nf.setMinimumFractionDigits(precision);
        nf.setMaximumFractionDigits(precision);
        return nf.format(amount);
    }

    public static String timeFormat(double number) {
        NumberFormat nf = NumberFormat.getIntegerInstance();
        String num = nf.format(number);
        if(number < 10)
            return "0" + num + ".00";
        return num + ".00";
    }

    public static String toTitleCase(String input) {
        input = input.toLowerCase();

        StringBuilder titleCase = new StringBuilder();
        boolean nextTitleCase = true;

        for (char c : input.toCharArray()) {
            if (Character.isSpaceChar(c)) {
                nextTitleCase = true;
            } else if (nextTitleCase) {
                c = Character.toTitleCase(c);
                nextTitleCase = false;
            }

            titleCase.append(c);
        }

        return titleCase.toString();
    }

    @SuppressWarnings("deprecation")
    public static Spanned fromHtml(String html){
        Spanned result;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            result = Html.fromHtml(html,Html.FROM_HTML_MODE_LEGACY);
        } else {
            result = Html.fromHtml(html);
        }
        return result;
    }
}
