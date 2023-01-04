package br.com.guaranamineiro.sankhya.model.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;

public class UtilDate {

	public static String dateToString(Date data) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String retorno = sdf.format(data);
		return retorno;
	}
	
	public static Date stringToDate(String data) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date retorno = sdf.parse(data);
		return retorno;
	}
	
	public static Date stringToDatetime(String data) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date retorno = sdf.parse(data);
		return retorno;
	}
	
	public static Date stringToDatetimeISO(String data) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		Date retorno = sdf.parse(data);
		return retorno;
	}
	
	public static String stringToFormat(Date data, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		String retorno = sdf.format(data);
		return retorno;
	}
	
	public static Date stringToDateFormat(String data, String format) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		Date retorno = sdf.parse(data);
		return retorno;
	}
	
	public static Date stringtoDateUTC(String data) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		Date retorno = sdf.parse(data);
		return retorno;
	}
	
	public static Boolean stringToDateFormatValid(String data, String format) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		try {
			sdf.parse(data);			
		} catch (Exception e) {
			return false;
		}	
		return true;
	}
	
	public static LocalDateTime convertToLocalDateTime(Date date) throws Exception {
		return Instant.ofEpochMilli(date.getTime())
				.atZone(ZoneId.systemDefault())
				.toLocalDateTime();
	}
	
	public static Timestamp convertToTimestamp(String data) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		Date retorno = sdf.parse(data);
		return new Timestamp(retorno.getTime());
	}
	
	public static Timestamp convertToTimestamp(String data, String format) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		Date retorno = sdf.parse(data);
		return new Timestamp(retorno.getTime());
	}
	
	public static Timestamp convertToTimestamp(Date data) throws Exception {
		return new Timestamp(data.getTime());
	}
	
	public static Timestamp getDataAtual() throws Exception {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		return timestamp;
	}
	
	public static Date getHoje(String format) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		Date hoje = Calendar.getInstance().getTime();		
		String data = sdf.format(hoje);
		return sdf.parse(data);
	}
	
	public static long countDaysBetween(Date dtBegin, Date dtEnd) throws Exception {
		LocalDateTime begin = convertToLocalDateTime(dtBegin);
		LocalDateTime end = convertToLocalDateTime(dtEnd);
		
		long days = begin.until(end, ChronoUnit.DAYS);
		return days;
	}
	
	public static long countYearsBetween(Date dtBegin, Date dtEnd) throws Exception {
		LocalDateTime begin = convertToLocalDateTime(dtBegin);
		LocalDateTime end = convertToLocalDateTime(dtEnd);
		
		long days = begin.until(end, ChronoUnit.YEARS);
		return days;
	}
}
