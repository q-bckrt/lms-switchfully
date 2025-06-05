package switchfully.lms.service;

import org.springframework.stereotype.Service;
import switchfully.lms.domain.Codelab;
import switchfully.lms.repository.CodelabRepository;
import switchfully.lms.service.dto.CodelabInputDto;
import switchfully.lms.service.dto.CodelabOutputDto;
import switchfully.lms.service.mapper.CodelabMapper;

import java.util.List;

@Service
public class CodelabService {

    // FIELDS
    private final CodelabRepository codelabRepository;
    private final CodelabMapper codelabMapper;

    // CONSTRUCTOR
    public CodelabService(CodelabRepository codelabRepository, CodelabMapper codelabMapper) {
        this.codelabRepository = codelabRepository;
        this.codelabMapper = codelabMapper;
    }

    // METHODS
    public CodelabOutputDto createCodelab(CodelabInputDto codelabInputDto) {
        Codelab codelab = codelabMapper.inputDtoToCodelab(codelabInputDto);
        System.out.println("Creating codelab: " + codelab.getTitle());
        Codelab saved = codelabRepository.save(codelab);
        return codelabMapper.moduleToOutputDto(saved);
    }

    // Needs more exception handling ?
    public List<CodelabOutputDto> getAllCodelabs() {
        return codelabRepository
                .findAll()
                .stream()
                .map(codelabMapper::moduleToOutputDto)
                .toList();
    }

    public CodelabOutputDto getCodelabById(Long id) {
        Codelab codelab = codelabRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Codelab not found with id: " + id));
        return codelabMapper.moduleToOutputDto(codelab);
    }

    public CodelabOutputDto updateCodelab(Long id, CodelabInputDto codelabInputDto) {
        Codelab codelab = codelabRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Codelab not found with id: " + id));
        codelab.setTitle(codelabInputDto.getTitle());
        codelab.setDetails(codelabInputDto.getDetails());
        Codelab updated = codelabRepository.save(codelab);
        return codelabMapper.moduleToOutputDto(updated);
    }

}
