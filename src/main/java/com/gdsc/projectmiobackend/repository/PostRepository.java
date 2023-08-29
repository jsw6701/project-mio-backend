package com.gdsc.projectmiobackend.repository;


import com.gdsc.projectmiobackend.entity.Category;
import com.gdsc.projectmiobackend.entity.Post;
import com.gdsc.projectmiobackend.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    Page<Post> findByCategory(Category category, Pageable pageable);

    Page<Post> findByUser(UserEntity user, Pageable pageable);

    List<Post> findByLatitudeAndLongitude(Double latitude, Double longitude);

    List<Post> findByLocation(String location);

}
