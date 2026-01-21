package com.prueba.quipux.dto;

import java.util.List;

public record PlaylistRequest(String nombre,
                              String descripcion,
                              List<SongDto> canciones) {
}
