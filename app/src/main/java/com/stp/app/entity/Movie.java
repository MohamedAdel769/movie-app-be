package com.stp.app.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.*;

@Entity
public class Movie {
    //TODO: primtive types ?

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
    private LocalDate releaseDate;

    @Column(name = "is_hidden", columnDefinition = "boolean default false")
    private Boolean isHidden;

    @Column(columnDefinition = "integer default 0")
    private Integer flags;

    //TODO: assume user flag once or handle it using set<user,flag>?
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "movies_flags",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    @JsonIgnore
    private Set<User> usersFlagged;

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

    @OneToMany(mappedBy = "movie", fetch = FetchType.LAZY)
    @JsonIgnore
    Set<UserRating> userRatings = new HashSet<>();

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

    public Boolean getHidden() {
        return isHidden;
    }

    public void setHidden(Boolean hidden) {
        isHidden = hidden;
    }

    public Set<User> getUsersFlagged() {
        return usersFlagged;
    }

    public void setUsersFlagged(Set<User> usersFlagged) {
        this.usersFlagged = usersFlagged;
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

    public Set<UserRating> getUserRatings() {
        return userRatings;
    }

    public void setUserRatings(Set<UserRating> userRatings) {
        this.userRatings = userRatings;
    }

    public Integer getFlags() {
        return flags;
    }

    public void setFlags(Integer flags) {
        this.flags = flags;
    }

    //</editor-fold>

    public void addUserFlag(User user){
        usersFlagged.add(user);
        flags = usersFlagged.size();
    }

    public void updateIsHidden() {
        this.isHidden = !this.isHidden;
    }

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
                ", flags=" + flags +
                '}';
    }
}
