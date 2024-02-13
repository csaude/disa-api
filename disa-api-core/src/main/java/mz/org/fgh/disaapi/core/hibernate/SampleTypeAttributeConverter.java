package mz.org.fgh.disaapi.core.hibernate;

import javax.persistence.AttributeConverter;

import mz.org.fgh.disaapi.core.result.model.SampleType;

public class SampleTypeAttributeConverter implements AttributeConverter<SampleType, String> {

    @Override
    public String convertToDatabaseColumn(SampleType attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.name();
    }

    @Override
    public SampleType convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }

        // If sample type is anything other than the defined
        // enum value, we should return null.
        try {
            return SampleType.valueOf(dbData);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
