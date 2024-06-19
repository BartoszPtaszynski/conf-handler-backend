package ConfHandler.model.dto;

import ConfHandler.model.entity.Lecture;
import ConfHandler.model.entity.Lecturer;
import ConfHandler.model.entity.Session;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class InvolvedInEvents {
    private List<LectureShortDto> chairmanOfLectures;
    private List<SessionShortDto> chairmanOfSessions;
    private List<LectureShortDto> lecturerOfLectures;
}
