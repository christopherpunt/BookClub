package bookclub.converters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.HashMap;

@Slf4j
@Converter
public class NotificationDataConverter implements AttributeConverter<HashMap, String> {

    private final static ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(HashMap attribute) {
        try {
            return objectMapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            log.error("Error converting map to JSON string: {}", e.getMessage());
            return null;
        }
    }

    @Override
    public HashMap<String, Object> convertToEntityAttribute(String dbData) {
        try {
            if (dbData != null && !dbData.isEmpty()) {
                return objectMapper.readValue(dbData, HashMap.class);
            }
            return new HashMap<>();
        } catch (IOException e) {
            log.error("Error converting JSON string to map: {}", e.getMessage());
            return null;
        }
    }
}
