package com.stp.app.repository;

import com.stp.app.entity.UserRating;
import com.stp.app.entity.UserRatingKey;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRatingRepository extends CrudRepository<UserRating, UserRatingKey> {
    List<UserRating> findAllByUserIdOrderByRatingDesc(Integer userId);
}
