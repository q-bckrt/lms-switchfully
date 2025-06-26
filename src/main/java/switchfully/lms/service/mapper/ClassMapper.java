package switchfully.lms.service.mapper;

import org.springframework.stereotype.Component;
import switchfully.lms.domain.Course;
import switchfully.lms.service.dto.*;
import switchfully.lms.domain.Class;

import java.util.List;

/**
 * Mapper class for mapping different types of class objects to each other <p>
 */
@Component
public class ClassMapper {

    /** maps a ClassInputDto to a domain entity
     * @param payload the input dto = json payload with information needed to create a class entity
     * @see ClassInputDto
     * @see Class
     * @return mapped class object
     * */
    public Class intputToClass(ClassInputDto payload) {
        return new Class(payload.getTitle());
    }

    /** maps a class domain entity to a classOutputDtoList, which holds all domain entity fields
     * @param classDomain the class domain entity to map
     * @param userList the list of userOutputDtos to add to the mapped entity (mapping of users handled in service layer)
     * @param course the CourseOutputDto to add to the mapped entity (mapping of course handled in service layer)
     * @see Class
     * @see ClassOutputDtoList
     * @see UserOutputDto
     * @see CourseOutputDto
     * @see switchfully.lms.service.ClassService
     * @return mapped ClassOutputDtoList object
     * */
    public ClassOutputDtoList classToOutputList(Class classDomain, List<UserOutputDto> userList, CourseOutputDto course) {
        return new ClassOutputDtoList(classDomain.getId(),
                course,
                classDomain.getTitle(),
                userList);
    }

    /** maps a class domain entity to a classOutputDto, which holds only the title and id of the class domain entity
     * @param classDomain the class domain entity to map
     * @see Class
     * @see ClassOutputDto
     * @return mapped ClassOutputDto object
     * */
    public ClassOutputDto classToOutput(Class classDomain, Course course) {
        if(course == null) {
            return new ClassOutputDto(classDomain.getId(),
                    classDomain.getTitle(),
                    null,
                    null
            );
        }
        return new ClassOutputDto(classDomain.getId(),
                classDomain.getTitle(),
                course.getId(),
                course.getTitle()
        );
    }
}
