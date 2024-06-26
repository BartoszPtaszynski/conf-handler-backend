package ConfHandler.Admin;

import ConfHandler.SuccessJsonResponse;
import ConfHandler.exception.ParticipantNotFoundException;
import ConfHandler.model.dto.MetadataDto;
import ConfHandler.model.entity.Participant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
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
    public SuccessJsonResponse addSession(@RequestBody List<SessionCommand> command)  {
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

    @PutMapping("/updateSessions")
    public SuccessJsonResponse updateSession(@RequestBody List<SessionInfo> command)  {
        adminService.updateSession(command);
        return new SuccessJsonResponse("Sessions updated");
    }
    @PutMapping("/updateEventOrLecture")
    public SuccessJsonResponse updateEventOrLecture(@RequestBody List<EventLectureInfo> command)  {
        adminService.updateEventLecture(command);
        return new SuccessJsonResponse("Events updated");
    }

    @PostMapping("sendEmails")
    public SuccessJsonResponse sendEmails()  {
        adminService.sendEmails();
        return new SuccessJsonResponse("emails sent");

    }


    @MessageMapping("/conference")
    @SendTo("/")
    public String handleUpdate(String message) throws InterruptedException {
        Thread.sleep(1000);
        return "Update received: " + message;
    }

}
