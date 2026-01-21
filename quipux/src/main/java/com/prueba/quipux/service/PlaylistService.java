package com.prueba.quipux.service;

import com.prueba.quipux.dto.PlaylistRequest;
import com.prueba.quipux.dto.PlaylistResponse;

import java.util.List;

public interface PlaylistService {
    PlaylistResponse create(PlaylistRequest request);
    List<PlaylistResponse> findAll();
    PlaylistResponse findByName(String listName);
    void deleteByName(String listName);
}
