package com.msgque.play.serializerDeserializer;


import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import org.apache.commons.lang3.math.NumberUtils;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import static java.util.Calendar.getInstance;

public class CalenderSerializerDeserializer implements JsonSerializer<Calendar>, JsonDeserializer<Calendar> {
  private final DateFormat dateFormatWithMilliseconds;
  private final DateFormat dateFormatWithOutMilliseconds;
  private final DateFormat solrDateFormat;

  public CalenderSerializerDeserializer() {
    dateFormatWithMilliseconds = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);      //This is the format I need
    dateFormatWithMilliseconds.setTimeZone(TimeZone.getTimeZone("UTC"));

    dateFormatWithOutMilliseconds = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);      //This is the format I need
    dateFormatWithOutMilliseconds.setTimeZone(TimeZone.getTimeZone("UTC"));

    solrDateFormat = new SimpleDateFormat("dd MMM yyyy HH.mm", Locale.US);      //This is the format I need
    solrDateFormat.setTimeZone(TimeZone.getDefault());
  }

  @Override
  public Calendar deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
    String dateString = json.getAsString().replaceAll(" +", " ");
    if (NumberUtils.isCreatable(dateString)) {
      if (dateString.length() == 10) dateString += "000";
      Calendar c = getInstance();
      c.setTimeInMillis(Long.parseLong(dateString));
      return c;
    }
    DateFormat formatToUse;
    Date date;
    if (dateString.split(" ").length == 4) {
      formatToUse = solrDateFormat;
    } else if (dateString.split("\\.").length == 2) {
      formatToUse = dateFormatWithMilliseconds;
    } else {
      formatToUse = dateFormatWithOutMilliseconds;
    }
    Calendar c = getInstance();
    try {
      date = formatToUse.parse(dateString);
    } catch (ParseException e) {
      throw new JsonParseException(e);
    }
    if (date != null) {
      c.setTime(date);
    }
    return c;
  }

  @Override
  public JsonElement serialize(Calendar date, Type typeOfSrc, JsonSerializationContext context) {
    return new JsonPrimitive(dateFormatWithMilliseconds.format(date.getTime()));
  }
}
