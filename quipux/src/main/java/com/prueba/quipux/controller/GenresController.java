package com.prueba.quipux.controller;

import com.prueba.quipux.spotify.client.SpotifyClient;
import com.prueba.quipux.spotify.dto.SpotifyGenresResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GenresController {

    private final SpotifyClient spotifyClient;

    public GenresController(SpotifyClient spotifyClient) {
        this.spotifyClient = spotifyClient;
    }

    @GetMapping("/genres")
    public SpotifyGenresResponse genres() {
        String token = spotifyClient.getAccessToken();
        return spotifyClient.getGenres(token);
    }
}
