package com.stp.app.repository;

import com.stp.app.entity.Movie;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface MovieRepository extends CrudRepository<Movie, Integer>, PagingAndSortingRepository<Movie, Integer> {

    List<Movie> findMovieByFlagsGreaterThan(Integer flags);
    List<Movie> findAllByIsHiddenFalse(Pageable pageable);
    List<Movie> findMovieByFlagsGreaterThanAndIsHiddenFalse(Integer flags);
}
