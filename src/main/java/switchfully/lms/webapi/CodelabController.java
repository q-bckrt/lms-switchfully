package switchfully.lms.webapi;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import switchfully.lms.service.CodelabService;
import switchfully.lms.service.CommentService;
import switchfully.lms.service.dto.*;

import java.util.List;

/**
 * CodelabController handles HTTP requests related to codelabs.
 * It allows coaches to create, retrieve, and update codelabs.
 * Students can retrieve all codelabs and view individual codelabs.
 *
 * @see CodelabService
 */
@RestController
@RequestMapping("/codelabs")
// @CrossOrigin(origins = "http://localhost:4200")
@CrossOrigin(origins = "https://lms-sw-frontend.netlify.app/")
public class CodelabController {

    // FIELDS
    private final CodelabService codelabService;
    private final CommentService commentService;

    // CONSTRUCTOR
    public CodelabController(CodelabService codelabService, CommentService commentService) {

        this.codelabService = codelabService;
        this.commentService = commentService;
    }

    // METHODS

    /** Create a new codelab.
     * Only coaches can create codelabs.
     *
     * @param codelabInputDto the input data for the new codelab
     * @return the created codelab as a CodelabOutputDto
     */
    @PostMapping(consumes = "application/json", produces = "application/json")
    @PreAuthorize("hasAuthority('COACH')")
    @ResponseStatus(HttpStatus.CREATED)
    public CodelabOutputDto createCodelab(@RequestBody CodelabInputDto codelabInputDto) {
        CodelabOutputDto codelabOutputDto = codelabService.createCodelab(codelabInputDto);
        System.out.println("Codelab DTO created with title: " + codelabOutputDto.getTitle());
        return codelabOutputDto;
    }

    /** Get all codelabs.
     * Both students and coaches can retrieve all codelabs.
     *
     * @return a list of all codelabs as CodelabOutputDto
     */
    @GetMapping(produces = "application/json")
    @PreAuthorize("hasAnyAuthority('STUDENT','COACH')")
    @ResponseStatus(HttpStatus.OK)
    public List<CodelabOutputDto> getAllCodelabs() {
        return codelabService.getAllCodelabs();
    }

    /** Get a codelab by its ID.
     * Both students and coaches can retrieve a codelab by its ID.
     *
     * @param id the ID of the codelab to retrieve
     * @return the codelab as a CodelabOutputDto
     */
    @GetMapping(path = "/{id}", produces = "application/json")
    @PreAuthorize("hasAnyAuthority('STUDENT','COACH')")
    @ResponseStatus(HttpStatus.OK)
    public CodelabOutputDto getCodelabById(@PathVariable Long id) {
        return codelabService.getCodelabById(id);
    }

    /** Edit (title) of a codelab.
     * Only coaches can update codelabs.
     *
     * @param id the ID of the codelab to update
     * @param codelabInputDto the input data for the updated codelab
     * @return the updated codelab as a CodelabOutputDto
     */
    @PutMapping(path = "/{id}", produces = "application/json", consumes = "application/json")
    @PreAuthorize("hasAuthority('COACH')")
    @ResponseStatus(HttpStatus.OK)
    public CodelabOutputDto updateCodelab(@PathVariable Long id, @RequestBody CodelabInputDto codelabInputDto) {
        return codelabService.updateCodelab(id, codelabInputDto);
    }

    /** See overview of the all student's progress for a codelab.
     * Only coaches can see everyone progress.
     * ProgressPerCodelabDtoList return the title of the codelab and a list of the display name of the students and their progress
     * @param id the ID of the codelab to update
     * @return ProgressPerCodelabDtoList object
     */
    @GetMapping(path = "/{id}/user-progress-overview", produces = "application/json")
    @PreAuthorize("hasAuthority('COACH')")
    @ResponseStatus(HttpStatus.OK)
    public ProgressPerCodelabDtoList getCodelabProgressPerCodelab(@PathVariable Long id) {
        return codelabService.getCodelabProgressPerCodelab(id);
    }

    /** Post a new comment under a codelab.
     * coaches and students can post comments.
     *
     * @param commentInputDto the input data for the new comment
     * @return the created comment as a CommentOutputDto
     */
    @PostMapping(path = "/{codelabId}/comments/{username}", consumes = "application/json", produces = "application/json")
    @PreAuthorize("hasAnyAuthority('STUDENT','COACH')")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentOutputDto postComment(@RequestBody CommentInputDto commentInputDto,
                                        @PathVariable Long codelabId,
                                        @PathVariable String username) {
        return commentService.postComment(commentInputDto, username, codelabId);
    }

    /** Get all comments for a codelab
     * coaches and students can see comments.
     *
     * @param codelabId id of the codelab
     * @return the created comment as a CommentOutputDto
     */
    @GetMapping(path = "/{codelabId}/comments", produces = "application/json")
    @PreAuthorize("hasAnyAuthority('STUDENT', 'COACH')")
    @ResponseStatus(HttpStatus.OK)
    public List<CommentOutputDto> getComment(@PathVariable Long codelabId) {
        return commentService.getAllCommentsByCodelabId(codelabId);
    }

    /**
     * Helper methods for the frontend to get the list of possible values for the progress level of a codelab
     *
     * @return list of String
     */
    @GetMapping(path = "/progress-levels", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public List<String> getProgressLevels() {
        //AUTHORIZE
        return codelabService.getProgressLevels();
    }

    /**
     * Helper methods for the frontend to get the progress of specific codelab for a specific user
     *
     * @return ProgressPer
     */
    @GetMapping(path = "/{codelabId}/progress-level/{username}", produces = "application/json")
    @PreAuthorize("hasAnyAuthority('STUDENT','COACH')")
    @ResponseStatus(HttpStatus.OK)
    public ProgressPerUserDto getProgressForCodelabUser(@PathVariable Long codelabId,
                                                        @PathVariable String username) {
        //AUTHORIZE
        return codelabService.getCodelabProgressPerUserForOneCodelab(username, codelabId);
    }

}
