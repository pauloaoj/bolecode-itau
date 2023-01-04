package br.com.guaranamineiro.sankhya.model.util;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * https://gist.github.com/adrianoluis/610d58655c48a187ffaa7ed85cbe0843
 */
public class UtilCodigoBarra {
	
    /**
     * Check if a barcode string is of type ITF.
     *
     * @param code Barcode string
     * @return <number>true</number> if is a valid string, <number>false</number> otherwise
     */
    public static boolean isValidBarcode(final String code) {
        return null != code && code.replaceAll("[^0-9]", "").length() == 44;
    }

    public static boolean isValidTypefulLine(final String code) {
        return null != code && (
                code.matches("^([0-9]{5})(\\.)?([0-9]{5})(\\s)?([0-9]{5})(\\.)?([0-9]{6})(\\s)?([0-9]{5})(\\.)?([0-9]{6})(\\s)?([0-9]{1})(\\s)?([0-9]{14})$") ||
                        code.matches("^([0-9]{11})(\\-)?([0-9])(\\s)?([0-9]{11})(\\-)?([0-9])(\\s)?([0-9]{11})(\\-)?([0-9])(\\s)?([0-9]{11})(\\-)?([0-9])$"));
    }

    /**
     * Check if the provided number is a valid "Boleto" number.
     *
     * @param code Tipeful Line or Barcode to extract
     * @return <number>true</number> if is a valid string, <number>false</number> otherwise
     */
    public static boolean isValidDocketDocument(String code) {
        code = code.replaceAll("[^0-9]", "").trim();

        if (!isValidBarcode(code)) {
            code = toBarcode(code);
        }

        final String realCode = code.substring(0, 4) + code.substring(5);
        final String validationDigit = code.substring(4, 5);

        return mod11(realCode).equals(validationDigit);
    }

    /**
     * Check if the provided number is a valid "Convênio" number.
     *
     * @param typefulLine Tipeful Line to extract
     * @return <number>true</number> if is a valid string, <number>false</number> otherwise
     */
    public static boolean isValidContractDocument(String typefulLine) {
        typefulLine = typefulLine.replaceAll("[^0-9]", "").trim();

        if (!isValidTypefulLine(typefulLine) && typefulLine.length() != 48) {
            return false;
        }

        final String codeBlock1 = typefulLine.substring(0, 11);
        final String codeBlock2 = typefulLine.substring(12, 23);
        final String codeBlock3 = typefulLine.substring(24, 35);
        final String codeBlock4 = typefulLine.substring(36, 47);

        final String validationDigit1 = typefulLine.substring(11, 12);
        final String validationDigit2 = typefulLine.substring(23, 24);
        final String validationDigit3 = typefulLine.substring(35, 36);
        final String validationDigit4 = typefulLine.substring(47);

        return mod10(codeBlock1).equals(validationDigit1) && mod10(codeBlock2).equals(validationDigit2) &&
                mod10(codeBlock3).equals(validationDigit3) && mod10(codeBlock4).equals(validationDigit4);
    }

    /**
     * Try to format the barcode string of type ITF into "Linha Digitável"
     *
     * @param barcode Barcode string
     * @return A well formatted version of the barcode given
     */
    public static String toTypefulLine(String barcode) {
        barcode = barcode.replaceAll("[^0-9]", "").trim();

        if (!isValidBarcode(barcode)) {
            return barcode;
        }

        if (isValidDocketDocument(barcode)) {
            final String codeBlock1 = barcode.substring(0, 4) + barcode.substring(19, 20);
            final String codeBlock2 = barcode.substring(20, 24);
            final String codeBlock3 = barcode.substring(24, 29);
            final String codeBlock4 = barcode.substring(29, 34);
            final String codeBlock5 = barcode.substring(34, 39);
            final String codeBlock6 = barcode.substring(39, 44);
            final String codeBlock7 = barcode.substring(4, 5);
            final String codeBlock8 = barcode.substring(5, 19);

            final String validationDigit1 = mod10(codeBlock1 + codeBlock2);
            final String validationDigit2 = mod10(codeBlock3 + codeBlock4);
            final String validationDigit3 = mod10(codeBlock5 + codeBlock6);

            return String.format("%1$s.%2$s%9$s %3$s.%4$s%10$s %5$s.%6$s%11$s %7$s %8$s",
                    codeBlock1, codeBlock2, codeBlock3, codeBlock4, codeBlock5, codeBlock6, codeBlock7, codeBlock8,
                    validationDigit1, validationDigit2, validationDigit3);
        } else {
            final String codeBlock1 = barcode.substring(0, 11);
            final String codeBlock2 = barcode.substring(11, 22);
            final String codeBlock3 = barcode.substring(22, 33);
            final String codeBlock4 = barcode.substring(33, 44);

            final String validationDigit1 = mod10(codeBlock1);
            final String validationDigit2 = mod10(codeBlock2);
            final String validationDigit3 = mod10(codeBlock3);
            final String validationDigit4 = mod10(codeBlock4);

            return String.format("%1$s-%5$s %2$s-%6$s %3$s-%7$s %4$s-%8$s",
                    codeBlock1, codeBlock2, codeBlock3, codeBlock4,
                    validationDigit1, validationDigit2, validationDigit3, validationDigit4);
        }

    }

    /**
     * Try to parse the "Linha Digitável" to a barcode
     *
     * @param typefulLine "Linha Digitável" string
     * @return Corresponding barcode
     */
    public static String toBarcode(String typefulLine) {
        typefulLine = typefulLine.replaceAll("[^0-9]", "").trim();

        if (!isValidTypefulLine(typefulLine)) {
            return typefulLine;
        }

        if (isValidContractDocument(typefulLine)) {
            return typefulLine.substring(0, 11) +
                    typefulLine.substring(12, 23) +
                    typefulLine.substring(24, 35) +
                    typefulLine.substring(36, 47);
        } else {
            String code = typefulLine.substring(0, 4) +
                    typefulLine.substring(32, 47) +
                    typefulLine.substring(4, 9) +
                    typefulLine.substring(10, 20) +
                    typefulLine.substring(21, 31);

            final String realCode = code.substring(0, 4) + code.substring(5);
            final String validationDigit = code.substring(4, 5);

            if (!mod11(realCode).equals(validationDigit)) {
                code = typefulLine;
            }

            return code;
        }

    }

    /**
     * Returns the expiration date of a "Boleto" document.
     *
     * @param code Typeful Line or Barcode to extract
     * @return a {@link java.util.Date} representing the expiration date
     */
    public static Date expiresAt(String code) {
        Date expiresAt = null;

        if (isValidDocketDocument(code)) {
            final int daysToExpire = Integer.parseInt(toBarcode(code).substring(5, 9));
            final Calendar calendar = new GregorianCalendar(1997, Calendar.OCTOBER, 7);
            calendar.add(Calendar.DAY_OF_YEAR, daysToExpire);
            expiresAt = calendar.getTime();
        }

        return expiresAt;
    }

    /**
     * Returns the document value to be paid
     *
     * @param code Typeful Line or Barcode to extract
     * @return the document value as double
     */
    public static double documentValue(String code) {
        double value = 0.0;

        String barcode = code;

        if (!isValidBarcode(code)) {
            barcode = toBarcode(code);
        }

        if (isValidDocketDocument(barcode)) {
            value = Double.parseDouble(barcode.substring(9, 19)) / 100;
        } else if (isValidContractDocument(code)) { // so valida se for linha digitavel
            value = Double.parseDouble(barcode.substring(5, 15)) / 100;
        }

        return value;
    }

    public static String mod10(String number) {
        // XXX important to reverse the string
        number = new StringBuffer(number).reverse().toString();

        int sum = 0;
        int weight = 2;
        int plus;

        for (char c : number.toCharArray()) {
            plus = Character.getNumericValue(c) * weight;

            if (plus >= 10) {
                plus = (plus - 10) + 1;
            }

            sum += plus;

            if (weight == 2) {
                weight = 1;
            } else {
                weight = 2;
            }

        }

        int digit = 10 - (sum % 10);

        if (digit == 10) {
            digit = 0;
        }

        return String.valueOf(digit);
    }

    public static String mod11(String number) {
        // XXX important to reverse the string
        number = new StringBuffer(number).reverse().toString();

        int sum = 0;
        int weight = 2;
        int base = 9;

        for (char c : number.toCharArray()) {
            sum += Character.getNumericValue(c) * weight;

            if (weight < base) {
                weight++;
            } else {
                weight = 2;
            }

        }

        int digit = 11 - (sum % 11);

        if (digit == 0 || digit > base) {
            digit = 1;
        }

        return String.valueOf(digit);
    }

}
