package edu.coursemgr.common;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomerObjectMapper extends ObjectMapper {

    private static final long serialVersionUID = 1L;

    public CustomerObjectMapper() {

        super();
//      this.setSerializationInclusion(Include.NON_NULL);
        // 允许单引号
//        this.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        // 字段和值都加引号
//        this.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        // 数字也加引号
//        this.configure(JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS, true);
//        this.configure(JsonGenerator.Feature.QUOTE_NON_NUMERIC_NUMBERS, true);
        this.getSerializerProvider().setNullValueSerializer(new JsonSerializer<Object>(){
            @Override
            public void serialize(Object value, JsonGenerator generator,
                                  SerializerProvider provider) throws IOException,
                    JsonProcessingException {
                generator.writeString("");
            }
        });
    }

}
