package com.gdsc.projectmiobackend.repository;

import com.gdsc.projectmiobackend.entity.Alarm;
import com.gdsc.projectmiobackend.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlarmRepository extends JpaRepository<Alarm, Long> {

    Alarm findByUserEntity(UserEntity userEntity);
}
