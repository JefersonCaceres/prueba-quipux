package com.prueba.quipux;

import com.prueba.quipux.dto.PlaylistRequest;
import com.prueba.quipux.dto.SongDto;
import com.prueba.quipux.exception.BadRequestException;
import com.prueba.quipux.repository.PlaylistRepository;
import com.prueba.quipux.service.impl.PlaylistServiceImpl;
import com.prueba.quipux.spotify.client.SpotifyClient;
import com.prueba.quipux.spotify.dto.SpotifyGenresResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PlaylistServiceImplTest {

    @Mock
    PlaylistRepository playlistRepository;
    @Mock
    SpotifyClient spotifyClient;

    @InjectMocks
    PlaylistServiceImpl service;

    @Test
    void create_shouldFail_whenNameNull() {
        assertThrows(BadRequestException.class, () -> service.create(new PlaylistRequest(null, "d", List.of())));
    }

    @Test
    void create_shouldFail_whenGenreNotAllowed() {
        when(playlistRepository.existsByName("Lista 1")).thenReturn(false);
        when(spotifyClient.getAccessToken()).thenReturn("t");
        when(spotifyClient.getGenres("t")).thenReturn(new SpotifyGenresResponse(List.of("pop")));

        PlaylistRequest req = new PlaylistRequest(
                "Lista 1", "desc",
                List.of(new SongDto("t1", "a1", "al", "2020", "rock"))
        );

        assertThrows(BadRequestException.class, () -> service.create(req));
    }
}