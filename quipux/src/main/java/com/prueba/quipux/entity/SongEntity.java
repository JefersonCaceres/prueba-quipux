package com.prueba.quipux.entity;

import jakarta.persistence.*;

@Entity
@Table(name="song")
public class SongEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="title", nullable=false, length=200)
    private String title;

    @Column(name="artist", nullable=false, length=200)
    private String artist;

    @Column(name="album", length=200)
    private String album;

    @Column(name="release_year", length=10)
    private String year;

    @Column(name="genre", length=80)
    private String genre;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="playlist_id", nullable=false)
    private PlaylistEntity playlist;

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getArtist() { return artist; }
    public String getAlbum() { return album; }
    public String getYear() { return year; }
    public String getGenre() { return genre; }
    public PlaylistEntity getPlaylist() { return playlist; }

    public void setTitle(String title) { this.title = title; }
    public void setArtist(String artist) { this.artist = artist; }
    public void setAlbum(String album) { this.album = album; }
    public void setYear(String year) { this.year = year; }
    public void setGenre(String genre) { this.genre = genre; }
    public void setPlaylist(PlaylistEntity playlist) { this.playlist = playlist; }
}
