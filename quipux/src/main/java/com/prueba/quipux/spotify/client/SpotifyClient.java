package com.prueba.quipux.spotify.client;

import com.prueba.quipux.spotify.dto.SpotifyGenresResponse;
import com.prueba.quipux.spotify.dto.SpotifyTokenResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Base64;
import java.util.List;

@Component
public class SpotifyClient {

    private final WebClient accountsClient;
    private final WebClient apiClient;
    private final String clientId;
    private final String clientSecret;

    public SpotifyClient(
            WebClient.Builder builder,
            @Value("${spotify.accounts-base-url}") String accountsBaseUrl,
            @Value("${spotify.api-base-url}") String apiBaseUrl,
            @Value("${spotify.client-id}") String clientId,
            @Value("${spotify.client-secret}") String clientSecret
    ) {
        this.accountsClient = builder.baseUrl(accountsBaseUrl).build();
        this.apiClient = builder.baseUrl(apiBaseUrl).build();
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }

    public String getAccessToken() {
        String basic = Base64.getEncoder()
                .encodeToString((clientId + ":" + clientSecret).getBytes());

        SpotifyTokenResponse token = accountsClient.post()
                .uri("/api/token")
                .header(HttpHeaders.AUTHORIZATION, "Basic " + basic)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData("grant_type", "client_credentials"))
                .retrieve()
                .bodyToMono(SpotifyTokenResponse.class)
                .block();

        if (token == null || token.accessToken() == null) {
            throw new IllegalStateException("No se pudo obtener token de Spotify");
        }
        return token.accessToken();
    }

    public SpotifyGenresResponse getGenres(String accessToken) {
     /*   SpotifyGenresResponse res = apiClient.get()
                .uri("/recommendations/available-genre-seeds")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .retrieve()
                .bodyToMono(SpotifyGenresResponse.class)
                .block();

        return res == null ? new SpotifyGenresResponse(java.util.List.of()) : res;*/
        return getMockGenres();
    }

    public SpotifyGenresResponse getMockGenres() {
        return new SpotifyGenresResponse(
                List.of(
                        "pop",
                        "rock",
                        "jazz",
                        "reggaeton",
                        "latin",
                        "salsa",
                        "electronic",
                        "hip-hop"
                )
        );
    }

}
