package com.stp.app.tmdb.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.stp.app.entity.Movie;

import java.util.ArrayList;
import java.util.List;

public class TopRated {
    private Integer page;
    private List<Movie> results = new ArrayList<>();

    @JsonProperty(value = "total_results")
    private Integer totalResults;

    @JsonProperty(value = "total_pages")
    private Integer totalPages;

    public TopRated() {
    }

    //<editor-fold desc="Getters and Setters">
    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public List<Movie> getResults() {
        return results;
    }

    public void setResults(List<Movie> results) {
        this.results = results;
    }

    public Integer getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(Integer totalResults) {
        this.totalResults = totalResults;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }
    //</editor-fold>

    @Override
    public String toString() {
        return "TopRated{" +
                "page=" + page +
                ", results=" + results +
                ", totalResults=" + totalResults +
                ", totalPages=" + totalPages +
                '}';
    }
}
