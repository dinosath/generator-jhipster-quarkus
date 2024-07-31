package org.acme;

import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import jakarta.enterprise.context.Dependent;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.ext.MessageBodyWriter;
import jakarta.ws.rs.ext.Provider;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

@Provider
@Dependent
@Produces("text/csv")
public class GenericCsvMessageBodyWriter<T> implements MessageBodyWriter<T> {

    private final CsvMapper csvMapper;

    public GenericCsvMessageBodyWriter() {
        this.csvMapper = new CsvMapper();
    }

    @Override
    public boolean isWriteable(Class<?> aClass, Type type, Annotation[] annotations, MediaType mediaType) {
        return true;
    }

    @Override
    public void writeTo(
        T t,
        Class<?> aClass,
        Type type,
        Annotation[] annotations,
        MediaType mediaType,
        MultivaluedMap<String, Object> multivaluedMap,
        OutputStream outputStream
    ) throws IOException, WebApplicationException {
        CsvSchema schema;
        if (type instanceof ParameterizedType) {
            ParameterizedType paramType = (ParameterizedType) type;
            Type[] typeArguments = paramType.getActualTypeArguments();
            if (typeArguments.length > 0) {
                Class<?> elementType = (Class<?>) typeArguments[0];
                schema = csvMapper.schemaFor(elementType).withHeader();
            } else {
                schema = CsvSchema.emptySchema().withHeader();
            }
        } else {
            schema = csvMapper.schemaFor(aClass).withHeader();
        }
        csvMapper.writer(schema).writeValue(outputStream, t);
    }
}