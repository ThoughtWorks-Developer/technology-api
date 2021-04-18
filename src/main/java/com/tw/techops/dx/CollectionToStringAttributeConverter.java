package com.tw.techops.dx;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class CollectionToStringAttributeConverter implements AttributeConverter<Collection<String>, String> {

    public String convertToDatabaseColumn(Collection<String> value) {
        if (value == null || value.isEmpty()) {
            return "";
        }
        return String.join(",", value);
    }

    public Collection<String> convertToEntityAttribute(String value) {
        if (value == null || value.trim().length() == 0) {
            return new ArrayList<String>();
        }
        return Arrays.asList(value.split(","));
    }
}