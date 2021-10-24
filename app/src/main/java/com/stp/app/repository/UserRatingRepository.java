package com.stp.app.repository;

import com.stp.app.entity.UserRating;
import com.stp.app.entity.UserRatingKey;
import org.springframework.data.repository.CrudRepository;

public interface UserRatingRepository extends CrudRepository<UserRating, UserRatingKey> {
}
