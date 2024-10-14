package com.example.intermediateapp.util;

import com.example.intermediateapp.dto.ResourceResponse;
import feign.Response;
import feign.Util;
import feign.codec.Decoder;
import java.io.IOException;
import java.lang.reflect.Type;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CustomResourceResponseDecoder implements Decoder {
    private final ObjectMapper objectMapper;

    public CustomResourceResponseDecoder(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public Object decode(Response response, Type type) throws IOException {
        if (type.getTypeName().equals(ResourceResponse.class.getName())) {
            String body = Util.toString(response.body().asReader());
            // Here, you might need to adjust how you convert the string to ResourceResponse
            // This example assumes ResourceResponse can be created from JSON string
            return objectMapper.readValue(body, ResourceResponse.class);
        }
        throw new RuntimeException("Type not supported by this decoder");
    }
}
