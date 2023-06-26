
package com.Gamarra.app.Utils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class RedondeoUtil {
    public static double redondear(double numero) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator('.');
        DecimalFormat decimalFormat = new DecimalFormat("#.00", symbols);
        return Double.parseDouble(decimalFormat.format(numero));
    }
}
