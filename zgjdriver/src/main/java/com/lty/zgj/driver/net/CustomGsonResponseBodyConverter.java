package com.lty.zgj.driver.net;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.lty.zgj.driver.BaseApplication;
import com.lty.zgj.driver.bean.HttpResult;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;

import cn.droidlover.xdroidbase.cache.SharedPref;
import okhttp3.MediaType;
import okhttp3.ResponseBody;
import retrofit2.Converter;

import static okhttp3.internal.Util.UTF_8;

/**
 * Created by Charles on 2016/3/17.
 */
class CustomGsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {

    private final Gson gson;
    private final TypeAdapter<T> adapter;

    CustomGsonResponseBodyConverter(Gson gson, TypeAdapter<T> adapter) {
        this.gson = gson;
        this.adapter = adapter;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        String response = value.string();
        Log.e("response",response);
        HttpResult httpResult = gson.fromJson(response, HttpResult.class);
        int resultCode = httpResult.getCode();
        SharedPref.getInstance(BaseApplication.getContext()).putInt("resultCode", resultCode);
        if (httpResult.isCodeInvalid()) {
            value.close();
            //抛出一个RuntimeException, 这里抛出的异常会到Subscriber的onError()方法中统一处理
            throw new ApiException(resultCode,httpResult.getMessage());
        }
        MediaType contentType = value.contentType();
        Charset charset = contentType != null ? contentType.charset(UTF_8) : UTF_8;
        InputStream inputStream = new ByteArrayInputStream(response.getBytes());
        Reader reader = new InputStreamReader(inputStream, charset);
        JsonReader jsonReader = gson.newJsonReader(reader);
        try {
            return adapter.read(jsonReader);
        } finally {
            value.close();
        }
    }
}
