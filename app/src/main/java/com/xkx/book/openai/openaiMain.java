package com.xkx.book.openai;

import android.util.Log;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class openaiMain {


    public static CompletableFuture<String> func1(String prompt) {
        CompletableFuture<String> future = new CompletableFuture<>();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api-inference.huggingface.co/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        OpenAIService service = retrofit.create(OpenAIService.class);

        // 确保请求构造的输入正确，这里使用 inputs 作为字段
        CompletionRequest request = new CompletionRequest(prompt,50);
//        request.setInputs(prompt);
//        request.setMaxTokens(50); // 假设需要设置最大tokens

        Call<List<Object>> call = service.getCompletion(request);

        call.enqueue(new Callback<List<Object>>() {
            @Override
            public void onResponse(Call<List<Object>> call, Response<List<Object>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // 确保解析响应体时使用正确的字段
                    String result = response.body().get(0).toString();
                    future.complete(result); // 成功时完成
                } else {
                    // 记录更多详细的错误信息
                    Log.e("API_ERROR", "Request URL: " + call.request().url());
                    Log.e("API_ERROR", "Response Code: " + response.code());
                    Log.e("API_ERROR", "Response Message: " + response.message());

                    // 检查是否有错误体并打印
                    if (response.errorBody() != null) {
                        try {
                            String errorBody = response.errorBody().string();
                            Log.e("API_ERROR", "Error Body: " + errorBody);
                        } catch (IOException e) {
                            Log.e("API_ERROR", "Error parsing error body", e);
                        }
                    }

                    future.completeExceptionally(new Exception("Error: " + response.message()));
                }
            }

            @Override
            public void onFailure(Call<List<Object>> call, Throwable t) {
                // 失败时记录错误信息
                Log.e("API_ERROR", "Request failed: " + t.getMessage(), t);
                future.completeExceptionally(t); // 失败时完成异常
            }
        });

        return future; // 返回 Future 对象
    }


}
