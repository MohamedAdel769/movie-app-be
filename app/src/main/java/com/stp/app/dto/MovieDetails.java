package com.stp.app.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.stp.app.entity.Genre;

import java.time.LocalDate;
import java.util.Set;

public class MovieDetails {

    private Set<Genre> genres;

    @JsonProperty("release_date")
    private LocalDate releaseDate;

    public MovieDetails() {
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
}
