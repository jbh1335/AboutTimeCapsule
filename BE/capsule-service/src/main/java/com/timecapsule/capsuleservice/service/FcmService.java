//package com.timecapsule.capsuleservice.service;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.google.auth.oauth2.GoogleCredentials;
//import com.timecapsule.capsuleservice.api.response.CommonRes;
//import com.timecapsule.capsuleservice.dto.FcmMessageDto;
//import lombok.RequiredArgsConstructor;
//import okhttp3.*;
//import org.apache.http.HttpHeaders;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.core.io.ClassPathResource;
//import org.springframework.stereotype.Component;
//
//import java.util.Arrays;
//
//import java.io.IOException;
//
//@Component
//@RequiredArgsConstructor
//public class FcmService {
//    @Value("${firebase.key.path}")
//    private String firebaseSdkPath;
//    @Value("$firebase.key.scope")
//    private String firebaseScope;
//    private final String API_URL = "https://fcm.googleapis.com/v1/projects/abouttimecapsule-ef8be/messages:send";
//    private final ObjectMapper objectMapper;
//    public CommonRes sendMessageTo(String targetToken, String title, String body) throws IOException {
//        String message = makeMessage(targetToken, title, body);
//
//        OkHttpClient client = new OkHttpClient();
//        RequestBody requestBody = RequestBody.create(message, MediaType.get("application/json; charset=utf-8"));
//        Request request = new Request.Builder()
//                .url(API_URL)
//                .post(requestBody)
//                .addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken())
//                .addHeader(HttpHeaders.CONTENT_TYPE, "application/json; UTF-8")
//                .build();
//
//        Response response = client.newCall(request).execute();
//        System.out.println("response.body().string(): " + response.body().string());
//
//        return new CommonRes(true, "메세지 푸시");
//    }
//
//    private String makeMessage(String targetToken, String title, String body) throws JsonProcessingException {
//        FcmMessageDto fcmMessageDto = FcmMessageDto.builder()
//                .message(FcmMessageDto.Message.builder()
//                        .token(targetToken)
//                        .notification(FcmMessageDto.Notification.builder()
//                                .title(title)
//                                .body(body)
//                                .image(null)
//                                .build())
//                        .build()).validateOnly(false).build();
//
//        // fcmMessageDto 객체 -> Json 문자열
//        return objectMapper.writeValueAsString(fcmMessageDto);
//    }
//
//    private String getAccessToken() throws IOException {
//        GoogleCredentials googleCredentials = GoogleCredentials
//                .fromStream(new ClassPathResource(firebaseSdkPath).getInputStream())
//                .createScoped(Arrays.asList(firebaseScope));
//
//        googleCredentials.refreshIfExpired();
//        return googleCredentials.getAccessToken().getTokenValue();
//    }
//}
