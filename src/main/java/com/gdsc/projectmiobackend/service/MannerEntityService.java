package com.gdsc.projectmiobackend.service;

import com.gdsc.projectmiobackend.dto.MannerDto;

import java.util.List;

public interface MannerEntityService {

    List<MannerDto> getMannersByPostUserId(Long userId);

    List<MannerDto> getMannersByGetUserId(Long userId);
}
