package com.bspicinini.catalog.admin.util.jackson;

import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonGenerator;
import tools.jackson.core.JsonParser;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.SerializationContext;
import tools.jackson.databind.ValueDeserializer;
import tools.jackson.databind.ValueSerializer;
import tools.jackson.databind.module.SimpleModule;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class BigDecimalModule extends SimpleModule {

    public BigDecimalModule() {
        addSerializer(BigDecimal.class, new BigDecimalSerializer());
        addDeserializer(BigDecimal.class, new BigDecimalDeserializer());
    }

    private static class BigDecimalSerializer extends ValueSerializer<BigDecimal> {      

        @Override
        public void serialize(BigDecimal value, JsonGenerator gen, SerializationContext ctxt) throws JacksonException {
            gen.writeNumber(value.setScale(2, RoundingMode.HALF_EVEN));
        }
    }

    private static class BigDecimalDeserializer extends ValueDeserializer<BigDecimal> {

        @Override
        public BigDecimal deserialize(JsonParser parser, DeserializationContext ctxt) throws JacksonException {
            return new BigDecimal(parser.getValueAsString()).setScale(2, RoundingMode.HALF_EVEN);
        }    
    }
}
