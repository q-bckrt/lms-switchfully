package switchfully.lms.service.dto;

import java.util.List;
import java.util.Objects;

public class ClassOutputDto {
    private String title;

    public ClassOutputDto( String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ClassOutputDto that = (ClassOutputDto) o;
        return Objects.equals(title, that.title);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(title);
    }

    @Override
    public String toString() {
        return "ClassOutputDto{" +
                "title='" + title + '\'' +
                '}';
    }
}
