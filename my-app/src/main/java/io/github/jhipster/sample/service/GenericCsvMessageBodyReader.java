package org.acme;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import jakarta.enterprise.context.Dependent;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.ext.MessageBodyReader;
import jakarta.ws.rs.ext.Provider;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

@Provider
@Dependent
@Consumes("text/csv")
public class GenericCsvMessageBodyReader<T> implements MessageBodyReader<T> {

    private final CsvMapper csvMapper;

    public GenericCsvMessageBodyReader() {
        this.csvMapper = new CsvMapper();
    }

    @Override
    public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return true;
    }

    @Override
    public T readFrom(
        Class<T> aClass,
        Type type,
        Annotation[] annotations,
        MediaType mediaType,
        MultivaluedMap<String, String> multivaluedMap,
        InputStream inputStream
    ) throws IOException, WebApplicationException {
        CsvSchema schema = CsvSchema.emptySchema().withHeader().withColumnReordering(true);
        if (List.class.isAssignableFrom(aClass)) {
            // If the target type is a List, extract the generic type and read values accordingly
            ParameterizedType paramType = (ParameterizedType) type;
            Class<?> listType = (Class<?>) paramType.getActualTypeArguments()[0];
            MappingIterator<?> iterator = csvMapper.readerFor(listType).with(schema).readValues(inputStream);
            T t = (T) iterator.readAll();
            return t;
        } else {
            // For single objects
            return csvMapper.readerFor(aClass).with(schema).readValue(inputStream);
        }
    }
}