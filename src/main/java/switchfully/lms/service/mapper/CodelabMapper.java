package switchfully.lms.service.mapper;

import switchfully.lms.domain.Codelab;
import org.springframework.stereotype.Component;
import switchfully.lms.repository.SubmoduleRepository;
import switchfully.lms.service.dto.CodelabOutputDto;
import switchfully.lms.service.dto.CodelabInputDto;

@Component
public class CodelabMapper {

    // FIELDS
    private final SubmoduleRepository submoduleRepository;

    // CONSTRUCTOR
    public CodelabMapper(SubmoduleRepository submoduleRepository) {
        this.submoduleRepository = submoduleRepository;
    }

    // METHODS
    public CodelabOutputDto moduleToOutputDto(Codelab codelab) {
        return new CodelabOutputDto(
                codelab.getId(),
                codelab.getTitle(),
                codelab.getDetails(),
                codelab.getParentSubmodule().getId()
        );
    }

    public Codelab inputDtoToCodelab(CodelabInputDto codelabInputDto) {
        return new Codelab(codelabInputDto.getTitle(), codelabInputDto.getDetails());
    }
}
