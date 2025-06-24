package switchfully.lms.service;

import org.springframework.stereotype.Service;
import switchfully.lms.domain.*;
import switchfully.lms.repository.CodelabRepository;
import switchfully.lms.repository.UserCodelabRepository;
import switchfully.lms.repository.UserRepository;
import switchfully.lms.service.dto.*;
import switchfully.lms.service.mapper.CodelabMapper;
import switchfully.lms.service.mapper.UserCodelabMapper;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for managing codelabs.
 * Provides methods to create, retrieve, update, and manage codelabs.
 */
@Service
public class CodelabService {

    // FIELDS
    private final CodelabRepository codelabRepository;
    private final UserCodelabRepository userCodelabRepository;
    private final UserRepository userRepository;

    private final CodelabMapper codelabMapper;
    private final UserCodelabMapper userCodelabMapper;

    private final UserCodelabService userCodelabService;

    // CONSTRUCTOR
    public CodelabService(CodelabRepository codelabRepository, CodelabMapper codelabMapper,
                          UserCodelabRepository userCodelabRepository, UserCodelabMapper userCodelabMapper,
                          UserCodelabService userCodelabService, UserRepository userRepository) {
        this.codelabRepository = codelabRepository;
        this.codelabMapper = codelabMapper;
        this.userCodelabRepository = userCodelabRepository;
        this.userCodelabMapper = userCodelabMapper;
        this.userCodelabService = userCodelabService;
        this.userRepository = userRepository;
    }

    // METHODS
    /**
     * Creates a new codelab based on the provided input DTO, and associates it with the parent submodule
     * that is specified in the input DTO.
     * Create link between this new codelab and all the user part of the related course/class
     * @param codelabInputDto the input DTO containing codelab details
     * @see UserCodelabService
     * @return the created codelab as an output DTO
     */
    public CodelabOutputDto createCodelab(CodelabInputDto codelabInputDto) {
        Codelab codelab = codelabMapper.inputDtoToCodelab(codelabInputDto);
        Codelab saved = codelabRepository.save(codelab);
        saved.getParentSubmodule().addChildCodelab(saved);
        // update link between user and new codelab
        userCodelabService.updateLinkBetweenUserAndCodelabWithCodelab(saved);
        return codelabMapper.codelabToOutputDto(saved);
    }

    /**
     * Retrieves all codelabs.
     *
     * @return a list of all codelabs as output DTOs
     */
    public List<CodelabOutputDto> getAllCodelabs() {
        return codelabRepository
                .findAll()
                .stream()
                .map(codelabMapper::codelabToOutputDto)
                .toList();
    }

    /**
     * Retrieves a codelab by its ID.
     *
     * @param id the ID of the codelab to retrieve
     * @return the codelab as an output DTO
     * @throws IllegalArgumentException if no codelab is found with the given ID
     */
    public CodelabOutputDto getCodelabById(Long id) {
        Codelab codelab = codelabRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Codelab not found with id: " + id));
        return codelabMapper.codelabToOutputDto(codelab);
    }

    /**
     * Updates an existing codelab with the provided input DTO.
     *
     * @param id              the ID of the codelab to update
     * @param codelabInputDto the input DTO containing updated codelab details
     * @return the updated codelab as an output DTO
     * @throws IllegalArgumentException if no codelab is found with the given ID
     */
    public CodelabOutputDto updateCodelab(Long id, CodelabInputDto codelabInputDto) {
        Codelab codelab = codelabRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Codelab not found with id: " + id));
        codelab.setTitle(codelabInputDto.getTitle());
        codelab.setDetails(codelabInputDto.getDetails());
        Codelab updated = codelabRepository.save(codelab);
        return codelabMapper.codelabToOutputDto(updated);
    }

    /**
     * Get an overview of the user progression for a specific codelab.
     *
     * @param id              the ID of the codelab to check
     * @return ProgressPerCodelabDtoList object
     * @throws IllegalArgumentException if no codelab is found with the given ID
     */
    public ProgressPerCodelabDtoList getCodelabProgressPerCodelab(Long id) {
        Codelab codelab = codelabRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Codelab not found with id: " + id));

        List<UserCodelab> userCodelabsList = userCodelabRepository.findByUserRoleCodelabId(UserRole.STUDENT, id);

        List<ProgressPerCodelabDto> progressDtos = userCodelabsList.stream()
                .map(userCodelabMapper::userCodelabToProgressPerCodelabDto)
                .toList();
        return  userCodelabMapper.codelabTitleAndProgressPerCodelabDtoToProgressPerCodelabDtoList(codelab.getTitle(), progressDtos);
    }

    /** Get the progress level values.
     * These values are to be used in a dropdown menu for a student to record where he/she is at the moment in a specific codelab.
     * @return List of String
     * */
    public List<String> getProgressLevels() {
        return Arrays.stream(ProgressLevel.values()).map(Enum::name).collect(Collectors.toList());
    }

    /** Get the progress for one codelab of a specific user.
     * Get user from database and the UserCodelab associated with its ID.
     * Add the title of the codelab and return a ProgressPerUserDtoList, with the username and a list of codelab title and their progress
     * @param username username use to retrieve a User from the database
     * @see UserCodelabRepository
     * @see UserCodelabMapper
     * @return ProgressPerUserDtoList object
     * */
    public ProgressPerUserDto getCodelabProgressPerUserForOneCodelab(String username, Long codelabId) {
        User user = userRepository.findByUserName(username);
        UserCodelabId userCodelabId = new UserCodelabId(user.getId(),codelabId);
        UserCodelab userCodelabList = userCodelabRepository.findById(userCodelabId).get();

        return userCodelabMapper.userCodelabToProgressPerUserDto(userCodelabList);

    }


}
