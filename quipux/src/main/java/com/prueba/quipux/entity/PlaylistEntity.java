package com.prueba.quipux.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "playlist", uniqueConstraints = @UniqueConstraint(columnNames = "name"))
public class PlaylistEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="name", nullable=false, length=120)
    private String name;

    @Column(name="description", length=500)
    private String description;

    @OneToMany(mappedBy = "playlist", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SongEntity> songs = new ArrayList<>();

    public void replaceSongs(List<SongEntity> newSongs) {
        songs.clear();
        if (newSongs != null) {
            for (SongEntity s : newSongs) {
                s.setPlaylist(this);
                songs.add(s);
            }
        }
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public List<SongEntity> getSongs() { return songs; }

    public void setName(String name) { this.name = name; }
    public void setDescription(String description) { this.description = description; }
}
