package com.prueba.quipux.dto;

import java.util.List;

public record PlaylistResponse(String nombre,
                               String descripcion,
                               List<SongDto> canciones) {
}
