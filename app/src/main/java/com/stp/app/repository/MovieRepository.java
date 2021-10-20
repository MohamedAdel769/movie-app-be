package com.stp.app.repository;

import com.stp.app.entity.Movie;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface MovieRepository extends CrudRepository<Movie, Integer>, PagingAndSortingRepository<Movie, Integer> {
}
