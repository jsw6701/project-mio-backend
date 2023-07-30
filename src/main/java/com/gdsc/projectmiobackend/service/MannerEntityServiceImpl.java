package com.gdsc.projectmiobackend.service;

import com.gdsc.projectmiobackend.dto.MannerDto;
import com.gdsc.projectmiobackend.entity.MannerEntity;
import com.gdsc.projectmiobackend.repository.MannerEntityRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class MannerEntityServiceImpl implements MannerEntityService{

    private final MannerEntityRepository mannerEntityRepository;
    @Override
    public List<MannerDto> getMannersByUserId(Long userId) {
        List<MannerEntity> mannerEntities = mannerEntityRepository.findAllByUserId(userId);
        return mannerEntities.stream().map(MannerEntity::toDto).collect(Collectors.toList());
    }

}
