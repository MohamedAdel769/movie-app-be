package com.stp.app.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.*;

@Entity
public class Movie {

    @Id
    private Integer id;

    @Lob
    //@Column(length = 512)
    private String overview;

    @Column(nullable = false)
    private String title;

    @Column(name = "backdrop_path", length = 50)
    @JsonProperty(value = "backdrop_path")
    private String backdropPath;

    @Column(name = "poster_path",length = 50)
    @JsonProperty(value = "poster_path")
    private String posterPath;

    @Column(name = "vote_count", nullable = false)
    @JsonProperty(value = "vote_count")
    private Integer voteCount;

    @Column(name = "vote_average", nullable = false, precision = 2, scale = 1)
    @JsonProperty(value = "vote_average")
    private Double voteAverage;

    @Column(name = "release_date", nullable = false)
    @JsonProperty(value = "release_date")
    private LocalDate releaseDate; //localdate?

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
            name = "movies_actors",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "actor_id")
    )
    private Set<Actor> actors = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "movies_genres",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    @JsonProperty(value = "genre_ids")
    private Set<Genre> genres = new HashSet<>();

    //<editor-fold desc="Constructor">
    public Movie() {

    }


    public Movie(Integer id, String overview,
                 String title, String backdropPath,
                 String posterPath, Integer voteCount,
                 Double voteAverage, LocalDate releaseDate) {
        this.id = id;
        this.overview = overview;
        this.title = title;
        this.backdropPath = backdropPath;
        this.posterPath = posterPath;
        this.voteCount = voteCount;
        this.voteAverage = voteAverage;
        this.releaseDate = releaseDate;
    }
    //</editor-fold>

    //<editor-fold desc="Getters and Setters">
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public Integer getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(Integer voteCount) {
        this.voteCount = voteCount;
    }

    public Double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(Double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public Set<Actor> getActors() {
        return actors;
    }

    public void setActors(Set<Actor> actors) {
        this.actors = actors;
    }

    public Set<Genre> getGenres() {
        return genres;
    }

    public void setGenres(Set<Genre> genres) {
        this.genres = genres;
    }
    //</editor-fold>

    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", overview='" + overview + '\'' +
                ", title='" + title + '\'' +
                ", backdropPath='" + backdropPath + '\'' +
                ", posterPath='" + posterPath + '\'' +
                ", voteCount=" + voteCount +
                ", voteAverage=" + voteAverage +
                ", releaseDate=" + releaseDate +
//                ", actors=" + actors +
//                ", genres=" + genres +
                '}';
    }
}
