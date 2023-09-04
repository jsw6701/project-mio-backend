package com.gdsc.projectmiobackend.service;

import com.gdsc.projectmiobackend.dto.request.AlarmCreateRequestDto;
import com.gdsc.projectmiobackend.entity.Alarm;

import java.util.List;

public interface AlarmService {
    Alarm saveAlarm(AlarmCreateRequestDto alarm);

    List<Alarm> getAllAlarm(String email);

    void deleteAlarm(Long id, String email);
}
