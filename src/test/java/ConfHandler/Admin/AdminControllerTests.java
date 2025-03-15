package ConfHandler.Admin;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdminController.class)
public class AdminControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdminService adminService;

    @Test
    public void testAddParticipants() throws Exception {
        List<ParticipantCommand> mockParticipants = Arrays.asList(
                new ParticipantCommand(),
                new ParticipantCommand()
        );

        mockMvc.perform(post("http://localhost:8080/admin/addParticipants")
                        .contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(mockParticipants)))
                .andExpect(status().isOk())
                .andExpect(content().string("Added 2"));

        verify(adminService, times(1)).addParticipants(mockParticipants);
    }

    @Test
    public void testGetParticipants() throws Exception {
        List<ParticipantInfo> mockParticipants = Arrays.asList(new ParticipantInfo(UUID.randomUUID().toString(), "name", "surname",
                "mail", "affilation", "title"));

        when(adminService.getParticipants()).thenReturn(mockParticipants);

        mockMvc.perform(get("/admin/getParticipants"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(mockParticipants.size()));

        verify(adminService, times(1)).getParticipants();
    }

    @Test
    public void testGetEventsLectures() throws Exception {
        List<EventLectureInfo> mockEvents = Arrays.asList(new EventLectureInfo(UUID.randomUUID().toString(),  "event",
                LocalDate.now(), LocalTime.now(), LocalTime.now().plusHours(1), UUID.randomUUID().toString(),
                "topic", "abstract", "description", UUID.randomUUID().toString(),
                UUID.randomUUID().toString()));

        when(adminService.getEventsLectures()).thenReturn(mockEvents);

        mockMvc.perform(get("/admin/getEventsLectures"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(mockEvents.size()));

        verify(adminService, times(1)).getEventsLectures();
    }

    @Test
    public void testAddSession() throws Exception {
        List<SessionCommand> mockSession = Arrays.asList(
                new SessionCommand()
        );

        mockMvc.perform(post("http://localhost:8080/admin/addSession")
                        .contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(mockSession)))
                .andExpect(status().isOk())
                .andExpect(content().string("Sessions added"));

        verify(adminService, times(1)).addSession(mockSession);
    }

    @Test
    public void testAddEventOrLecture() throws Exception{
        List<EventLectureCommand> mockEvent = Arrays.asList(
                new EventLectureCommand()
        );

        mockMvc.perform(post("http://localhost:8080/admin/addEventOrLecture")
                        .contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(mockEvent)))
                .andExpect(status().isOk())
                .andExpect(content().string("Events and lectures added"));

        verify(adminService, times(1)).addEventOrLecture(mockEvent);
    }

    @Test
    public void testGetSessions() throws Exception {
        List<SessionInfo> mockSessions = Arrays.asList(new SessionInfo(UUID.randomUUID(), "name", LocalDate.now(),
                LocalTime.now(), LocalTime.now(), "city", "street", "building", "room", "chairman"));

        when(adminService.getSessions()).thenReturn(mockSessions);

        mockMvc.perform(get("/admin/getSessions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(mockSessions.size()));

        verify(adminService, times(1)).getSessions();
    }

    @Test
    void updateSession() throws Exception {
        UUID sessionId = UUID.randomUUID();
        List<SessionInfo> mockSession = Arrays.asList(new SessionInfo(sessionId, "name", LocalDate.of(2000, 12, 12), LocalTime.of(10, 30, 00),
                LocalTime.of(10, 30, 00), "", "", "", "", ""));
        String jsonContent = "[{\"id\":\"" + sessionId.toString() + "\", " +
                "\"name\":\"name\", " +
                "\"sessionDate\":\"2000-12-12\", " +
                "\"timeStart\":\"10:30:00\", " +
                "\"timeEnd\":\"10:30:00\", " +
                "\"city\":\"\", " +
                "\"street\":\"\", " +
                "\"building\":\"\", " +
                "\"roomNumber\":\"\", " +
                "\"chairman\":\"\"}]";

        mockMvc.perform(put("/admin/updateSessions")
                        .contentType("application/json")
                        .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().string("Sessions updated"));

        verify(adminService, times(1)).updateSession(mockSession);
    }

    @Test
    public void testUpdateEventOrLecture() throws Exception {
        UUID eventId = UUID.randomUUID();
        List<EventLectureInfo> mockEvent = Arrays.asList(new EventLectureInfo(eventId.toString(), "name", LocalDate.of(2000, 12, 12), LocalTime.of(10, 30, 00),
                LocalTime.of(10, 30, 00), "", "", "", "", "", ""));
        String jsonContent = "[{\"id\":\"" + eventId.toString() + "\", " +
                "\"name\":\"name\", " +
                "\"eventDate\":\"2000-12-12\", " +
                "\"timeStart\":\"10:30:00\", " +
                "\"timeEnd\":\"10:30:00\", " +
                "\"sessionId\":\"\", " +
                "\"topic\":\"\", " +
                "\"abstract\":\"\", " +
                "\"description\":\"\", " +
                "\"lecturers\":\"\", " +
                "\"chairman\":\"\"}]";


        mockMvc.perform(put("/admin/updateEventOrLecture")
                        .contentType("application/json")
                        .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().string("Events updated"));

        verify(adminService, times(1)).updateEventLecture(mockEvent);
    }

    @Test
    public void testSendEmails() throws Exception {
        String message = "Test message";
        mockMvc.perform(post("/admin/sendEmails")
                        .contentType("application/json")
                        .content(message))
                .andExpect(status().isOk())
                .andExpect(content().string("emails sent"));

        verify(adminService, times(1)).sendEmails();
    }
}
