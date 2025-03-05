package com.suika.englishlearning.service;

import com.suika.englishlearning.exception.ResourceNotFoundException;
import com.suika.englishlearning.mapper.DifficultyMapper;
import com.suika.englishlearning.model.Difficulty;
import com.suika.englishlearning.model.dto.difficulty.DifficultyDto;
import com.suika.englishlearning.repository.DifficultyRepository;
import jakarta.persistence.EntityExistsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class DifficultyService {
    private final DifficultyRepository difficultyRepository;
    private final DifficultyMapper difficultyMapper;

    public DifficultyService(DifficultyRepository difficultyRepository) {
        this.difficultyRepository = difficultyRepository;
        difficultyMapper = new DifficultyMapper();
    }

    public List<DifficultyDto> getDifficulties() {
        return difficultyMapper.toDtoList(difficultyRepository.findAll());
    }

    public DifficultyDto getDifficultyByName(String name) {
        return difficultyMapper.toDto(difficultyRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Difficulty not found")));
    }

    @Transactional
    public String editDifficulty(String name, DifficultyDto difficultyDto) {
        Difficulty difficulty = difficultyRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Difficulty not found"));

        String editName = difficultyDto.getName();
        if (editName != null &&
                !editName.isEmpty() &&
                !Objects.equals(difficulty.getDescription(), editName)) {
            difficulty.setName(editName);
        }
        else {
            throw new InvalidParameterException("Invalid difficulty name");
        }

        String editDescription = difficultyDto.getDescription();
        if (editDescription != null &&
                !editDescription.isEmpty()) {
            difficulty.setDescription(editDescription);
        }
        else {
            throw new InvalidParameterException("Invalid difficulty description");
        }

        return "Edit successfully";
    }

    public String addDifficulty(DifficultyDto difficultyDto) {
        String name = difficultyDto.getName();
        Optional<Difficulty> difficulty = difficultyRepository.findByName(name);

        if(difficulty.isPresent()) {
            throw new EntityExistsException("Difficulty existed.");
        };

        Difficulty newDifficulty = difficultyMapper.toEntity(difficultyDto);
        difficultyRepository.save(newDifficulty);

        return "Added successfully";
    }

    public String deleteDifficulty(String name)
    {
        Difficulty difficulty = difficultyRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Difficulty not found"));

        difficultyRepository.delete(difficulty);

        return "Deleted successfully";
    }
}
