package com.example.railyatri.RestApi;

import com.example.railyatri.Model.ApiErrorModel;

import java.io.IOException;
import java.lang.annotation.Annotation;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Response;

public class ErrorUtils {

    public static ApiErrorModel parseError(Response<?> response) {

        Converter<ResponseBody, ApiErrorModel> converter =
                ApiClientClass.getClient()
                        .responseBodyConverter(ApiErrorModel.class, new Annotation[0]);

        ApiErrorModel apiError;

        try {
            assert response.errorBody() != null;
            apiError = converter.convert(response.errorBody());
        } catch (IOException e) {
            return new ApiErrorModel();
        }

        return apiError;
    }
}
