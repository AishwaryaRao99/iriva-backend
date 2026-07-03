package com.aishwarya.ethical.transparency_portal.modules.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.aishwarya.ethical.transparency_portal.modules.user.model.UserModel;

@Repository
public interface UserRepository extends JpaRepository<UserModel, Long> {
 
    Optional<UserModel> findByUsername(String username);
  
    Optional<UserModel> findByEmail(String email);
 
    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}

