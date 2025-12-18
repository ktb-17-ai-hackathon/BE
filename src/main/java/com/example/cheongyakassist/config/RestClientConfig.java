package com.example.cheongyakassist.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.*;
import org.springframework.lang.NonNull;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

@Slf4j
@Configuration
public class RestClientConfig {

    @Bean
    public RestTemplate restTemplate() {
        // SSL 인증서 검증 우회
        disableSslVerification();

        SimpleClientHttpRequestFactory baseFactory = new SimpleClientHttpRequestFactory();
        baseFactory.setConnectTimeout(3000);   // 3초 (죽은 IP면 빨리 포기)
        baseFactory.setReadTimeout(180000);    // 180초 (LLM 느려도 기다림)

        // ✅ 응답 바디 로깅을 위해 buffering 필수
        ClientHttpRequestFactory bufferingFactory =
                new BufferingClientHttpRequestFactory(baseFactory);

        RestTemplate rt = new RestTemplate(bufferingFactory);

        // ✅ 요청/응답 로그 인터셉터 추가
        rt.getInterceptors().add(new RestTemplateLoggingInterceptor());

        return rt;
    }

    @Slf4j
    static class RestTemplateLoggingInterceptor implements ClientHttpRequestInterceptor {

        @Override
        public @NonNull ClientHttpResponse intercept(
                @NonNull HttpRequest request,
                @NonNull byte[] body,
                @NonNull ClientHttpRequestExecution execution
        ) throws IOException {

            // ===== Request =====
            log.debug("➡️ [HTTP REQUEST] {} {}", request.getMethod(), request.getURI());
            log.debug("➡️ [HTTP REQUEST HEADERS] {}", request.getHeaders());
            log.debug("➡️ [HTTP REQUEST BODY] {}", new String(body, StandardCharsets.UTF_8));

            long start = System.currentTimeMillis();
            ClientHttpResponse response = execution.execute(request, body);
            long ms = System.currentTimeMillis() - start;

            // ===== Response =====
            byte[] respBytes = StreamUtils.copyToByteArray(response.getBody());
            String respBody = new String(respBytes, StandardCharsets.UTF_8);

            log.debug("⬅️ [HTTP RESPONSE] status={} ({} ms)", response.getStatusCode(), ms);
            log.debug("⬅️ [HTTP RESPONSE HEADERS] {}", response.getHeaders());
            log.debug("⬅️ [HTTP RESPONSE BODY] {}", respBody);

            // BufferingClientHttpRequestFactory 덕분에 컨버터가 다시 읽을 수 있습니다.
            return response;
        }
    }

    private void disableSslVerification() {
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public X509Certificate[] getAcceptedIssuers() {
                            return new X509Certificate[0];
                        }
                        @Override
                        public void checkClientTrusted(X509Certificate[] certs, String authType) {
                        }
                        @Override
                        public void checkServerTrusted(X509Certificate[] certs, String authType) {
                        }
                    }
            };

            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

            HostnameVerifier allHostsValid = (hostname, session) -> true;
            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);

        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            log.error("Failed to disable SSL verification", e);
        }
    }
}
