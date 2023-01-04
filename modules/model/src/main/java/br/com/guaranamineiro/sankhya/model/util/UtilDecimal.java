package br.com.guaranamineiro.sankhya.model.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class UtilDecimal {

	public static String formatPtBr(BigDecimal valor) throws Exception {
		String retorno = new DecimalFormat("#,##0.00").format(valor);
		return retorno;
	}	
}
