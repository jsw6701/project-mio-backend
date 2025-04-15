package com.gdsc.projectmiobackend.repository;


import com.gdsc.projectmiobackend.common.PostType;
import com.gdsc.projectmiobackend.entity.Category;
import com.gdsc.projectmiobackend.entity.Post;
import com.gdsc.projectmiobackend.entity.UserEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    Page<Post> findByCategoryAndIsDeleteYNAndPostType(Category category, Pageable pageable, String isDeleteYN, PostType postType);

    Page<Post> findByUserAndIsDeleteYN(UserEntity user, Pageable pageable, String isDeleteYN);

    @Query("SELECT p FROM Post p WHERE p.region3Depth = :activityLocation AND p.isDeleteYN = :isDeleteYN")
    Page<Post> findByLocation(Pageable pageable, @Param("isDeleteYN") String isDeleteYN, @Param("activityLocation") String activityLocation);

    List<Post> findByLatitudeAndLongitudeAndIsDeleteYN(Double latitude, Double longitude, String isDeleteYN);

    List<Post> findByLocationContainingAndIsDeleteYN(String location, String isDeleteYN);


    @Query("SELECT p FROM Post p WHERE " +
            "(:category IS NULL OR p.category = :category) AND " +
            "(:isDeleteYN IS NULL OR p.isDeleteYN = :isDeleteYN) AND " +
            "(:postType IS NULL OR p.postType = :postType) AND " +
            "(:targetDate IS NULL OR p.targetDate = :targetDate) ")
    Page<Post> findPageByCategoryAndIsDeleteYNAndPostTypeAndTargetDate(
            @Param("category") Category category,
            @Param("isDeleteYN") String isDeleteYN,
            @Param("postType") PostType postType,
            @Param("targetDate") LocalDate targetDate,
            @Param("pageable") Pageable pageable
    );

    @Query("SELECT p FROM Post p WHERE " +
            "(:category IS NULL OR p.category = :category) AND " +
            "(:isDeleteYN IS NULL OR p.isDeleteYN = :isDeleteYN) AND " +
            "(:postType IS NULL OR p.postType = :postType) AND " +
            "(:targetDate IS NULL OR p.targetDate = :targetDate) " +
            "ORDER BY p.createDate DESC")
    List<Post> findListByCategoryAndIsDeleteYNAndPostTypeAndTargetDateOrderByCreateDate(
            @Param("category") Category category,
            @Param("isDeleteYN") String isDeleteYN,
            @Param("postType") PostType postType,
            @Param("targetDate") LocalDate targetDate
    );


    @Query("SELECT p FROM Post p WHERE (6371 * acos(cos(radians((SELECT latitude FROM Post WHERE id = ?1))) * cos(radians(p.latitude)) * cos(radians(p.longitude) - radians((SELECT longitude FROM Post WHERE id = ?1))) + sin(radians((SELECT latitude FROM Post WHERE id = ?1))) * sin(radians(p.latitude)))) < 3")
    List<Post> findByDistanceAndIsDeleteYN(Long postId, String isDeleteYN);

    @Modifying
    @Transactional
    @Query("UPDATE Post p SET p.isDeleteYN = 'Y' WHERE p.user.id = :userId and p.id = :id")
    void deletePost(@Param("userId") Long userId, @Param("id") Long id);

    Page<Post> findAllByIsDeleteYN(String isDeleteYN, Pageable pageable);
}
