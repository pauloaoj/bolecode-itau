package br.com.guaranamineiro.sankhya.model.util;

import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class UtilJson {

	public static String convertToJson(Object data) {
		Gson gson = new GsonBuilder()
				.setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
				.create();
		
		String json = gson.toJson(data);
		return json;
	}
	
	public static String convertToJsonNonIgnoreNull(Object data) {
		Gson gson = new GsonBuilder()
				.serializeNulls()
				.setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
				.create();
		
		String json = gson.toJson(data);
		return json;
	}
	
	public static <T> T[] convertToList(String json, Type clazz) {
		Gson gson = new GsonBuilder()
				.setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
				.create();
		
		T[] data = gson.fromJson(json, clazz);
		return data;
	}
}
