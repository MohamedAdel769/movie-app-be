package com.stp.app.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.stp.app.entity.Genre;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class MovieDetails {

    private Set<Genre> genres;

    @JsonProperty("release_date")
    private LocalDate releaseDate;

    @JsonProperty("original_language")
    private String language;

    public MovieDetails() {
    }

    public MovieDetails(String language) {
        this.genres = new HashSet<>();
        this.releaseDate = LocalDate.of(2021, 10, 26);
        this.language = language;
    }

    public Set<Genre> getGenres() {
        return genres;
    }

    public void setGenres(Set<Genre> genres) {
        this.genres = genres;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}
