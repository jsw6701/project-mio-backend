package com.gdsc.projectmiobackend.repository;

import com.gdsc.projectmiobackend.entity.Alarm;
import com.gdsc.projectmiobackend.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlarmRepository extends JpaRepository<Alarm, Long> {

    List<Alarm> findByUserEntity(UserEntity userEntity);
}
