package com.msgque.play.serializerDeserializer;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

public class StringDeserializer implements JsonDeserializer<String> {
	@Override
	public String deserialize (JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		String s = json.getAsJsonPrimitive().getAsString();
		s = s.replaceAll("\t", "");
		s = s.trim();
		return s;
	}
}
