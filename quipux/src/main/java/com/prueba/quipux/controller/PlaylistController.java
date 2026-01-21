package com.prueba.quipux.controller;

import com.prueba.quipux.dto.PlaylistRequest;
import com.prueba.quipux.dto.PlaylistResponse;
import com.prueba.quipux.service.PlaylistService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/lists")
public class PlaylistController {

    private final PlaylistService playlistService;

    public PlaylistController(PlaylistService playlistService) {
        this.playlistService = playlistService;
    }

    @PostMapping
    public ResponseEntity<PlaylistResponse> create(@RequestBody PlaylistRequest request) {
        PlaylistResponse created = playlistService.create(request);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{listName}")
                .buildAndExpand(created.nombre())
                .toUri();

        return ResponseEntity.created(location).body(created); // 201 + Location + body
    }

    @GetMapping
    public ResponseEntity<List<PlaylistResponse>> findAll() {
        return ResponseEntity.ok(playlistService.findAll()); // 200
    }

    @GetMapping("/{listName}")
    public ResponseEntity<PlaylistResponse> findByName(@PathVariable String listName) {
        return ResponseEntity.ok(playlistService.findByName(listName)); // 200 o 404
    }

    @DeleteMapping("/{listName}")
    public ResponseEntity<Void> delete(@PathVariable String listName) {
        playlistService.deleteByName(listName);
        return ResponseEntity.noContent().build(); // 204 o 404
    }
}
