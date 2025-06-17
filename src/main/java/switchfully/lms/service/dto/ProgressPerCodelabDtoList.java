package switchfully.lms.service.dto;

import java.util.List;

public class ProgressPerCodelabDtoList {

    private String codelabTitle;
    private List<ProgressPerCodelabDto> progressPerCodelabDtoList;

    public ProgressPerCodelabDtoList() {

    }

    public ProgressPerCodelabDtoList(String codelabTitle, List<ProgressPerCodelabDto> progressPerCodelabDtoList) {
        this.codelabTitle = codelabTitle;
        this.progressPerCodelabDtoList = progressPerCodelabDtoList;
    }

    public String getCodelabTitle() {
        return codelabTitle;
    }

    public List<ProgressPerCodelabDto> getProgressPerCodelabDtoList() {
        return progressPerCodelabDtoList;
    }

    @Override
    public String toString() {
        return "ProgressPerCodelabDtoList{" +
                "codelabTitle='" + codelabTitle + '\'' +
                ", progressPerCodelabDtoList=" + progressPerCodelabDtoList +
                '}';
    }
}
