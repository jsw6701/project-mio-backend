package com.gdsc.projectmiobackend.repository;

import com.gdsc.projectmiobackend.entity.Participants;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
public interface ParticipantsRepository extends JpaRepository<Participants, Long> {
    List<Participants> findByPostId(Long postId);

    List<Participants> findPostListByUserId(Long userId);

    List<Participants> findByUserId(Long userId);

    Participants findByPostIdAndUserId(Long postId, Long userId);
}
