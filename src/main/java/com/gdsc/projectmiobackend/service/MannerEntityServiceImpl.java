package com.gdsc.projectmiobackend.service;

import com.gdsc.projectmiobackend.dto.MannerDto;
import com.gdsc.projectmiobackend.entity.MannerEntity;
import com.gdsc.projectmiobackend.repository.MannerEntityRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class MannerEntityServiceImpl implements MannerEntityService{

    private final MannerEntityRepository mannerEntityRepository;
    @Override
    public List<MannerDto> getMannersByPostUserId(Long userId) {
        List<MannerEntity> mannerEntities = mannerEntityRepository.findAllByGetUserId(userId);
        return mannerEntities.stream().map(MannerEntity::toDto).toList();
    }


    @Override
    public List<MannerDto> getMannersByGetUserId(Long userId) {
        List<MannerEntity> mannerEntities = mannerEntityRepository.findAllByPostUserId(userId);
        return mannerEntities.stream().map(MannerEntity::toDto).toList();
    }
}
