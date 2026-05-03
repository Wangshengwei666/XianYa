package com.sysu.xianya.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sysu.xianya.service.AIService;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AIServiceImpl implements AIService {

    private static final String API_ENDPOINT = "https://dashscope.aliyuncs.com/api/v1/services/aigc/text-generation/generation";

    @Value("${dashscope.api-key:}")
    private String apiKey;

    private final OkHttpClient client = new OkHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String generateDescriptionByTitle(String title) throws Exception {
        if (apiKey == null || apiKey.trim().isEmpty()) {
            throw new IllegalStateException("DashScope API key is not configured. Set DASHSCOPE_API_KEY.");
        }

        String prompt = String.format(
                "你是一个二手交易平台的文案专家。请根据以下商品标题，为我生成一段80字以内、吸引人的商品描述。" +
                        "请突出商品的特点和新旧程度，语气要亲切、真实。商品标题是：%s",
                title
        );

        Map<String, Object> inputMap = new HashMap<>();
        inputMap.put("prompt", prompt);

        Map<String, Object> requestBodyMap = new HashMap<>();
        requestBodyMap.put("model", "qwen-plus");
        requestBodyMap.put("input", inputMap);

        String requestBodyJson = objectMapper.writeValueAsString(requestBodyMap);

        RequestBody body = RequestBody.create(requestBodyJson, MediaType.get("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url(API_ENDPOINT)
                .addHeader("Authorization", "Bearer " + apiKey)
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            String responseBody = response.body().string();

            if (!response.isSuccessful()) {
                System.err.println("Failed to call AI API. Response body: " + responseBody);
                throw new Exception("Failed to call AI API");
            }

            JsonNode rootNode = objectMapper.readTree(responseBody);
            return rootNode.path("output").path("text").asText();
        }
    }
}
