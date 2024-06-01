package ConfHandler.Admin;

import ConfHandler.SuccessJsonResponse;
import ConfHandler.model.entity.Participant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("admin")
@Slf4j
public class AdminController {

    @Autowired
    private AdminService adminService;
    @PostMapping("/addParticipants")
    public SuccessJsonResponse addParticipants(@RequestBody List<ParticipantCommand> command) {
        adminService.addParticipants(command);
        return new SuccessJsonResponse("Added "+command.size());
    }

    @GetMapping("/getParticipants")
    public List<?> getParticipants() {

        return (adminService.getParticipants());
    }
    @GetMapping("/getEventsLectures")
    public List<?> getEventsLectures() {

        return (adminService.getEventsLectures());
    }
    @PostMapping("/addSession")
    public SuccessJsonResponse addSession(@RequestBody List<SessionCommand> command) {
        adminService.addSession(command);
        return new SuccessJsonResponse("Sessions added");
    }
    @PostMapping("/addEventOrLecture")
    public SuccessJsonResponse addEventOrLecture(@RequestBody List<EventLectureCommand> command) {
        adminService.addEventOrLecture(command);
        return new SuccessJsonResponse("Events and lectures added");
    }
    @GetMapping("/getSessions")
    public List<?> getSessions() {
        return (adminService.getSessions());
    }

}
