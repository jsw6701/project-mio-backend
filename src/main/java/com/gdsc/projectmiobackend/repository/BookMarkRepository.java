package com.gdsc.projectmiobackend.repository;

import com.gdsc.projectmiobackend.entity.BookMark;
import com.gdsc.projectmiobackend.entity.Post;
import com.gdsc.projectmiobackend.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookMarkRepository extends JpaRepository<BookMark, Long> {
    BookMark findByPostAndUserEntity(Post post, UserEntity userEntity);

    List<BookMark> findByUserEntity(UserEntity userEntity);
}
