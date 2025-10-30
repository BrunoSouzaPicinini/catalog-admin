package com.bspicinini.catalog.admin.util.jackson;

import com.fasterxml.jackson.annotation.JsonInclude;
import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.util.StdDateFormat;

public class CustomObjectMapper {
    private static final ObjectMapper mapper = JsonMapper.builder()
            .disable(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES)
            .disable(DeserializationFeature.FAIL_ON_NULL_CREATOR_PROPERTIES)            
            .addModule(new BigDecimalModule())
            .propertyNamingStrategy(PropertyNamingStrategies.LOWER_CAMEL_CASE)
            .changeDefaultPropertyInclusion(incl -> incl.withValueInclusion(JsonInclude.Include.NON_NULL)) // do not serialize nulls
            .defaultDateFormat(new StdDateFormat())
            .build();

    public static <T> T clone(T object, Class<T> clazz) {
        try {
            byte[] bytes = mapper.writeValueAsBytes(object);
            return mapper.readValue(bytes, clazz);
        } catch (Exception e) {
            throw new RuntimeException("Error on cloning object", e);
        }
    }

    public static ObjectMapper getMapper() {
        return mapper;
    }
}
