package com.stp.app.entity;

import javax.persistence.*;

@Entity
public class UserRating {

    @EmbeddedId
    private UserRatingKey id;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @MapsId("movieId")
    @JoinColumn(name = "movie_id")
    private Movie movie;

    @Column(nullable = false, precision = 2, scale = 1)
    private Double rating;

    private String description;

    public UserRating() {
    }

    public UserRating(UserRatingKey id, User user,
                      Movie movie, Double rating, String description) {
        this.id = id;
        this.user = user;
        this.movie = movie;
        this.rating = rating;
        this.description = description;
    }

    public UserRatingKey getId() {
        return id;
    }

    public void setId(UserRatingKey id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "UserRating{" +
                "user=" + user.getId() +
                ", movie=" + movie.getTitle() +
                ", rating=" + rating +
                ", description='" + description + '\'' +
                '}';
    }
}
