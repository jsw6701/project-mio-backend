package com.gdsc.projectmiobackend.repository;

import com.gdsc.projectmiobackend.entity.RefreshTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, Long> {
    @Override
    Optional<RefreshTokenEntity> findById(Long aLong);
}
