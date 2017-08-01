package com.msgque.pulse.serializerDeserializer;


import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateSerializerDeserializer implements JsonSerializer<Date>, JsonDeserializer<Date> {
	private final DateFormat dateFormatWithMilliseconds;
	private final DateFormat dateFormatWithOutMilliseconds;

	public DateSerializerDeserializer () {
		dateFormatWithMilliseconds = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);      //This is the format I need
		dateFormatWithMilliseconds.setTimeZone(TimeZone.getTimeZone("UTC"));

		dateFormatWithOutMilliseconds = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);      //This is the format I need
		dateFormatWithOutMilliseconds.setTimeZone(TimeZone.getTimeZone("UTC"));
	}

	@Override
	public Date deserialize (JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		String date = json.getAsString();
		DateFormat formatToUse;
//    Log.e(DateSerializerDeserializer.class.getSimpleName(), date.split("\\.").length + ":date:" + date);
		if (date.split("\\.").length == 2) {
			formatToUse = dateFormatWithMilliseconds;
		} else {
			formatToUse = dateFormatWithOutMilliseconds;
		}
		try {
			return formatToUse.parse(date);
		} catch (ParseException e) {
			throw new JsonParseException(e);
		}
	}

	@Override
	public JsonElement serialize (Date date, Type typeOfSrc, JsonSerializationContext context) {
		return new JsonPrimitive(dateFormatWithMilliseconds.format(date));
	}
}
