package com.droplite.util;

import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class InternalApiClient {

    private final RestTemplate restTemplate;

    public <T> T callApi(String url, HttpMethod method, Map<String, String> queryParams, Object requestBody, Class<T> responseType) {

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUri(URI.create(url));
        if (queryParams != null) {
            queryParams.forEach(uriBuilder::queryParam);
        }
        String finalUrl = uriBuilder.toUriString();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Object> requestEntity = new HttpEntity<>(requestBody, headers);
        return restTemplate.exchange(finalUrl, method, requestEntity, responseType).getBody();
    }

}
