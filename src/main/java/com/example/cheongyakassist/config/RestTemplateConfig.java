package com.example.cheongyakassist.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.*;
import org.springframework.lang.NonNull;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j

public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate() {
        // 응답 바디를 인터셉터에서 읽어도, 이후 컨버터가 다시 읽을 수 있도록 buffering
        ClientHttpRequestFactory factory =
                new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory());

        RestTemplate rt = new RestTemplate(factory);
        rt.getInterceptors().add(new LoggingInterceptor());
        return rt;
    }

    @Slf4j
    static class LoggingInterceptor implements ClientHttpRequestInterceptor {

        @Override
        public @NonNull ClientHttpResponse intercept(
                @NonNull HttpRequest request,
                @NonNull byte[] body,
                @NonNull ClientHttpRequestExecution execution
        ) throws IOException {

            // ===== Request log =====
            log.debug("➡️ [HTTP REQUEST] {} {}", request.getMethod(), request.getURI());
            log.debug("➡️ [HTTP REQUEST HEADERS] {}", request.getHeaders());
            log.debug("➡️ [HTTP REQUEST BODY] {}", new String(body, StandardCharsets.UTF_8));

            long start = System.currentTimeMillis();
            ClientHttpResponse response = execution.execute(request, body);
            long ms = System.currentTimeMillis() - start;

            // ===== Response log =====
            byte[] respBytes = StreamUtils.copyToByteArray(response.getBody());
            String respBody = new String(respBytes, StandardCharsets.UTF_8);

            log.debug("⬅️ [HTTP RESPONSE] status={} ({} ms)", response.getStatusCode(), ms);
            log.debug("⬅️ [HTTP RESPONSE HEADERS] {}", response.getHeaders());
            log.debug("⬅️ [HTTP RESPONSE BODY] {}", respBody);

            // BufferingClientHttpRequestFactory 덕분에 response body는 다시 읽힙니다.
            return response;
        }
    }
}
