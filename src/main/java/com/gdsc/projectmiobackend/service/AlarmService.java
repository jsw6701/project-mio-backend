package com.gdsc.projectmiobackend.service;

import com.gdsc.projectmiobackend.dto.request.AlarmCreateRequestDto;
import com.gdsc.projectmiobackend.entity.Alarm;

public interface AlarmService {
    Alarm saveAlarm(AlarmCreateRequestDto alarm);

    void deleteAlarm(Long id, String email);
}
