package com.gdsc.projectmiobackend.repository;

import com.gdsc.projectmiobackend.entity.MannerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MannerEntityRepository extends JpaRepository<MannerEntity, Long> {

    List<MannerEntity> findAllByGetUserId(Long userId);

    List<MannerEntity> findAllByPostUserId(Long userId);
}
