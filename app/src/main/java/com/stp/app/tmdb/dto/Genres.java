package com.stp.app.tmdb.dto;

import com.stp.app.entity.Genre;

import java.util.HashSet;
import java.util.Set;

public class Genres {
    Set<Genre> genres = new HashSet<>();

    public Genres() {}

    public Set<Genre> getGenres() {
        return genres;
    }

    public void setGenres(Set<Genre> genres) {
        this.genres = genres;
    }

    @Override
    public String toString() {
        return "Genres{" +
                "genres=" + genres +
                '}';
    }
}
