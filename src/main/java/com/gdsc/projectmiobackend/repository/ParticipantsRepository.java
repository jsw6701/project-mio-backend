package com.gdsc.projectmiobackend.repository;

import com.gdsc.projectmiobackend.entity.Participants;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
public interface ParticipantsRepository extends JpaRepository<Participants, Long> {
    List<Participants> findByPostIdAndIsDeleteYN(Long postId, String isDeleteYN);

    List<Participants> findPostListByUserIdAndIsDeleteYN(Long userId, String isDeleteYN);

    @Query("SELECT p FROM Participants p WHERE p.user.id = :userId AND p.isDeleteYN = :isDeleteYN")
    List<Participants> findByUserIdAndIsDeleteYN(Long userId, String isDeleteYN);
    
    @Query("SELECT p FROM Participants p WHERE p.postUserId = :postUserId AND p.isDeleteYN = :isDeleteYN AND p.user.id != p.postUserId")
    List<Participants> findByPostUserIdAndIsDeleteYN(Long postUserId, String isDeleteYN);

    Participants findByPostIdAndUserIdAndIsDeleteYN(Long postId, Long userId, String isDeleteYN);

    @Modifying
    @Transactional
    @Query("UPDATE Participants p SET p.isDeleteYN = 'Y' WHERE p.id = :id")
    void deleteParticipant(Long id);

    /**
     * 게시글 삭제 시 모든 신청 삭제 Y로 변경
     */
    @Modifying
    @Transactional
    @Query("UPDATE Participants p SET p.isDeleteYN = 'Y' WHERE p.post.id = :postId")
    void deletePostParticipant(Long postId);
}
