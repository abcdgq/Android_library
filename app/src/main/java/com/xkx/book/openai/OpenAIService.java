package com.xkx.book.openai;

import java.util.List;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;


public interface OpenAIService {
    @Headers("Authorization: Bearer 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")
    @POST("models/gpt2") // 根据实际API替换URL
    Call<List<Object>> getCompletion(@Body CompletionRequest request);
//    Call<List<Object>> getResponse(@Body L); // 请求方法
}