package com.cuk.catsnap.global.jsonformat.serializer;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/*
 * ["HH:mm", "HH:mm","HH:mm" ...]을 List<LocalTime>으로 json 역직렬화 하는 클래스
 */
public class HoursMinutesListDeserializer extends JsonDeserializer<List<LocalTime>> {

    @Override
    public List<LocalTime> deserialize(JsonParser p, DeserializationContext ctxt)
        throws IOException, JacksonException {
        List<LocalTime> localTimeList = new ArrayList<>();
        ArrayNode node = p.getCodec().readTree(p);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

        for (JsonNode jsonNode : node) {
            localTimeList.add(LocalTime.parse(jsonNode.asText(), formatter)); // 문자열을 LocalTime으로 변환
        }
        return localTimeList;
    }
}
