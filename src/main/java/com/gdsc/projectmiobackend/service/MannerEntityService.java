package com.gdsc.projectmiobackend.service;

import com.gdsc.projectmiobackend.dto.MannerDto;

import java.util.List;

public interface MannerEntityService {

    List<MannerDto> getMannersByUserId(Long userId);
}
