package com.suika.englishlearning.mapper;

import com.suika.englishlearning.model.Difficulty;
import com.suika.englishlearning.model.dto.difficulty.DifficultyDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class DifficultyMapper implements Mapper<Difficulty, DifficultyDto> {
    @Override
    public DifficultyDto toDto(Difficulty difficultyEntity) {
        DifficultyDto difficultyDto = new DifficultyDto();
        difficultyDto.setName(difficultyEntity.getName());
        difficultyDto.setDescription(difficultyEntity.getDescription());

        return difficultyDto;
    }

    @Override
    public Difficulty toEntity(DifficultyDto difficultyDto) {
        Difficulty difficulty = new Difficulty();
        difficulty.setName(difficultyDto.getName());
        difficulty.setDescription(difficultyDto.getDescription());

        return difficulty;
    }

    @Override
    public List<DifficultyDto> toDtoList(List<Difficulty> difficultyEntityList) {
        return difficultyEntityList.stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public List<Difficulty> toEntityList(List<DifficultyDto> dtoList) {
        return dtoList.stream().map(this::toEntity).collect(Collectors.toList());
    }
}
