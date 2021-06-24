package com.jacobroe.ControllersAndViews.repositories;

import org.springframework.data.repository.CrudRepository;
import com.jacobroe.ControllersAndViews.models.User;

public interface UserRepository extends CrudRepository<User, Long>{
	User findByEmail(String email);
}