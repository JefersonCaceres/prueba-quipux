package com.prueba.quipux.service.impl;

import com.prueba.quipux.dto.PlaylistRequest;
import com.prueba.quipux.dto.PlaylistResponse;
import com.prueba.quipux.dto.SongDto;
import com.prueba.quipux.entity.PlaylistEntity;
import com.prueba.quipux.entity.SongEntity;
import com.prueba.quipux.exception.BadRequestException;
import com.prueba.quipux.exception.NotFoundException;
import com.prueba.quipux.repository.PlaylistRepository;
import com.prueba.quipux.service.PlaylistService;
import com.prueba.quipux.spotify.client.SpotifyClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class PlaylistServiceImpl implements PlaylistService {

    private final PlaylistRepository playlistRepository;
    private final SpotifyClient spotifyClient;

    public PlaylistServiceImpl(PlaylistRepository playlistRepository, SpotifyClient spotifyClient) {
        this.playlistRepository = playlistRepository;
        this.spotifyClient = spotifyClient;
    }

    @Override
    @Transactional
    public PlaylistResponse create(PlaylistRequest request) {
        if (request == null || request.nombre() == null || request.nombre().trim().isEmpty()) {
            throw new BadRequestException("Nombre de lista inválido");
        }

        String listName = request.nombre().trim();
        if (playlistRepository.existsByName(listName)) {
            throw new BadRequestException("La lista ya existe");
        }

        // Géneros válidos desde Spotify
        Set<String> allowedGenres = new HashSet<>();
        String token = spotifyClient.getAccessToken();
        var genres = spotifyClient.getGenres(token);
        if (genres != null && genres.genres() != null) allowedGenres.addAll(genres.genres());

        PlaylistEntity playlist = new PlaylistEntity();
        playlist.setName(listName);
        playlist.setDescription(request.descripcion());

        List<SongEntity> songs = mapSongs(request.canciones(), allowedGenres);
        playlist.replaceSongs(songs);

        PlaylistEntity saved = playlistRepository.save(playlist);
        return toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PlaylistResponse> findAll() {
        return playlistRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public PlaylistResponse findByName(String listName) {
        PlaylistEntity entity = playlistRepository.findByName(listName)
                .orElseThrow(() -> new NotFoundException("Lista no encontrada"));
        return toResponse(entity);
    }

    @Override
    @Transactional
    public void deleteByName(String listName) {
        PlaylistEntity entity = playlistRepository.findByName(listName)
                .orElseThrow(() -> new NotFoundException("Lista no encontrada"));
        playlistRepository.delete(entity);
    }

    private List<SongEntity> mapSongs(List<SongDto> dtos, Set<String> allowedGenres) {
        if (dtos == null) return List.of();

        return dtos.stream().map(dto -> {
            if (dto.titulo() == null || dto.titulo().trim().isEmpty())
                throw new BadRequestException("Título inválido");
            if (dto.artista() == null || dto.artista().trim().isEmpty())
                throw new BadRequestException("Artista inválido");
            if (dto.genero() == null || dto.genero().trim().isEmpty())
                throw new BadRequestException("Género inválido");

            String genre = dto.genero().trim();
            if (!allowedGenres.isEmpty() && !allowedGenres.contains(genre)) {
                throw new BadRequestException("Género no permitido");
            }

            SongEntity s = new SongEntity();
            s.setTitle(dto.titulo().trim());
            s.setArtist(dto.artista().trim());
            s.setAlbum(dto.album());
            s.setYear(dto.anno());
            s.setGenre(genre);
            return s;
        }).toList();
    }

    private PlaylistResponse toResponse(PlaylistEntity entity) {
        List<SongDto> songs = entity.getSongs().stream()
                .map(s -> new SongDto(s.getTitle(), s.getArtist(), s.getAlbum(), s.getYear(), s.getGenre()))
                .toList();

        return new PlaylistResponse(entity.getName(), entity.getDescription(), songs);
    }
}
