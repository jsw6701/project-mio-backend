package com.gdsc.projectmiobackend.service;

import com.gdsc.projectmiobackend.dto.request.AlarmCreateRequestDto;
import com.gdsc.projectmiobackend.entity.Alarm;
import com.gdsc.projectmiobackend.entity.Post;
import com.gdsc.projectmiobackend.entity.UserEntity;
import com.gdsc.projectmiobackend.repository.AlarmRepository;
import com.gdsc.projectmiobackend.repository.PostRepository;
import com.gdsc.projectmiobackend.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@AllArgsConstructor
public class AlarmServiceImpl implements AlarmService{

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final AlarmRepository alarmRepository;

    @Override
    public Alarm saveAlarm(AlarmCreateRequestDto alarm){
        UserEntity user = userRepository.findById(alarm.getUserId()).orElseThrow(() -> new IllegalArgumentException("유저정보가 없습니다."));
        Post post = postRepository.findById(alarm.getPostId()).orElseThrow(() -> new IllegalArgumentException("포스트 정보가 없습니다."));
        return alarm.toEntity(alarm, post, user);
    }

    @Override
    public void deleteAlarm(Long id, String email){
        UserEntity user = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("유저정보가 없습니다."));

        Alarm alarm = alarmRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("알람 정보가 없습니다."));
        if(Objects.equals(alarm.getUserEntity().getId(), user.getId())){
            alarmRepository.delete(alarm);
        }
        else throw new IllegalArgumentException("해당 알람을 삭제할 권한이 없습니다.");
    }
}
