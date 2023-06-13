package com.mellivora.base.http.converter;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;

final class BaseGsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {

    private final Gson gson;
    private final Type type;

    BaseGsonResponseBodyConverter(Gson gson, Type type) {
        this.gson = gson;
        this.type = type;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        try {
            //泛型为String处理
            if (type instanceof Class) {
                //处理泛型为String类型的情况
                Class<?> clazz = (Class<?>) type;
                if (clazz == String.class) {
                    return (T) value.string();
                }
            }
            //其他类型泛型处理
            JsonReader jsonReader = gson.newJsonReader(value.charStream());
            return gson.fromJson(jsonReader, type);
        } finally {
            value.close();
        }
    }
}