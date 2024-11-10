package com.cuk.catsnap.global.jsonformat.deserialize;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/*
 * List<LocalTime>을 ["HH:mm", "HH:mm","HH:mm" ...] 으로 json 직렬화 하는 클래스
 */
public class HoursMinutesListSerializer extends JsonSerializer<List<LocalTime>> {

    @Override
    public void serialize(List<LocalTime> value, JsonGenerator gen, SerializerProvider serializers)
        throws IOException {
        gen.writeStartArray();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        for (LocalTime localTime : value) {
            gen.writeString(localTime.format(formatter));
        }
        gen.writeEndArray();
    }
}
