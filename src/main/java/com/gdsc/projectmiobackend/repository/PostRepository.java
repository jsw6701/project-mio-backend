package com.gdsc.projectmiobackend.repository;


import com.gdsc.projectmiobackend.entity.Category;
import com.gdsc.projectmiobackend.entity.Post;
import com.gdsc.projectmiobackend.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    Page<Post> findByCategory(Category category, Pageable pageable);

    Page<Post> findByUser(UserEntity user, Pageable pageable);

    List<Post> findByLatitudeAndLongitude(Double latitude, Double longitude);

    List<Post> findByLocationContaining(String location);

    @Query("SELECT p FROM Post p WHERE (6371 * acos(cos(radians((SELECT latitude FROM Post WHERE id = ?1))) * cos(radians(p.latitude)) * cos(radians(p.longitude) - radians((SELECT longitude FROM Post WHERE id = ?1))) + sin(radians((SELECT latitude FROM Post WHERE id = ?1))) * sin(radians(p.latitude)))) < 3")
    List<Post> findByDistance(Long postId);
}
