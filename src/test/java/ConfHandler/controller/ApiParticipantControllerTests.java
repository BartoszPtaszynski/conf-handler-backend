package ConfHandler.controller;

import ConfHandler.Admin.AdminController;
import ConfHandler.exception.EventNotFoundException;
import ConfHandler.exception.ParticipantNotFoundException;
import ConfHandler.sevice.ParticipantService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ApiParticipantController.class)
public class ApiParticipantControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ParticipantService participantService;

    @Test
    public void testSignParticipantToEvent() throws Exception {
        UUID eventId = UUID.randomUUID();
        UUID participantId = UUID.randomUUID();

        doNothing().when(participantService).saveParticipantToEvent(participantId, eventId);

        mockMvc.perform(post("/addBookmark")
                        .param("idEvent", eventId.toString())
                        .param("idParticipant", participantId.toString())
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(content().string("successfully added"));

        verify(participantService, times(1)).saveParticipantToEvent(participantId, eventId);
    }

    @Test
    void testSignParticipantToEventNotFound() throws Exception {
        UUID eventId = UUID.randomUUID();
        UUID participantId = UUID.randomUUID();

        doThrow(new EventNotFoundException(eventId))
                .when(participantService).saveParticipantToEvent(participantId, eventId);

        mockMvc.perform(post("/addBookmark")
                        .param("idEvent", eventId.toString())
                        .param("idParticipant", participantId.toString())
                        .contentType("application/json"))
                .andExpect(status().isConflict())
                .andExpect(content().string("Event with id=" + eventId + " not found"));

        verify(participantService, times(1)).saveParticipantToEvent(participantId, eventId);
    }

    @Test
    void testRemoveParticipantFromEvent() throws Exception {
        UUID eventId = UUID.randomUUID();
        UUID participantId = UUID.randomUUID();

        doNothing().when(participantService).deleteParticipantFromEvent(participantId, eventId);

        mockMvc.perform(delete("/removeBookmark")
                        .param("idEvent", eventId.toString())
                        .param("idParticipant", participantId.toString())
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(content().string("successfully removed"));

        verify(participantService, times(1)).deleteParticipantFromEvent(participantId, eventId);
    }

    @Test
    void testRemoveParticipantFromEventNotFound() throws Exception {
        UUID eventId = UUID.randomUUID();
        UUID participantId = UUID.randomUUID();

        doThrow(new ParticipantNotFoundException(participantId))
                .when(participantService).deleteParticipantFromEvent(participantId, eventId);

        mockMvc.perform(delete("/removeBookmark")
                        .param("idEvent", eventId.toString())
                        .param("idParticipant", participantId.toString())
                        .contentType("application/json"))
                .andExpect(status().isConflict())
                .andExpect(content().string("Participant with id=" + participantId +  " not found"));

        verify(participantService, times(1)).deleteParticipantFromEvent(participantId, eventId);
    }
}
