package com.example.intermediateapp.util;

import com.example.intermediateapp.dto.ResourceResponse;
import feign.Response;
import feign.codec.Decoder;
import java.io.IOException;
import java.lang.reflect.Type;

public class CustomTextDecoder implements Decoder {
    @Override
    public Object decode(Response response, Type type) throws IOException {
        if (type == String.class || type == ResourceResponse.class) { // Assuming ResourceResponse can be represented as String or has appropriate conversion
            return new java.io.BufferedReader(response.body().asReader()).readLine(); // Wrap with BufferedReader
        }
        throw new RuntimeException("Type not supported by this decoder");
    }
}