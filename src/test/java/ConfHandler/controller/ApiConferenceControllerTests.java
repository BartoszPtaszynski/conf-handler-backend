package ConfHandler.controller;

import ConfHandler.Admin.AdminController;
import ConfHandler.Admin.AdminService;
import ConfHandler.model.dto.ConferenceInfoDto;
import ConfHandler.model.dto.MetadataDto;
import ConfHandler.sevice.DisplayConferenceService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ApiConferenceController.class)
public class ApiConferenceControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DisplayConferenceService displayConferenceService;

    @Test
    public void testGetTimeLineByDay() throws Exception {
        LocalDate date = LocalDate.now();
        when(displayConferenceService.getDayOfConference(date, null)).thenReturn(List.of());

        mockMvc.perform(get("/getTimeLineByDate")
                        .param("date", date.toString())
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));

        verify(displayConferenceService, times(1)).getDayOfConference(date, null);
    }

    @Test
    public void testGetTimeLineByDayOfParticipant() throws Exception {
        LocalDate date = LocalDate.now();
        UUID id = UUID.randomUUID();
        when(displayConferenceService.getDayOfConference(date, id)).thenReturn(List.of());

        mockMvc.perform(get("/getBookmarkedEvents")
                        .param("date", date.toString())
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));

        verify(displayConferenceService, times(1)).getDayOfConference(date, null);
    }

    @Test
    public void testGetConferenceInfo() throws Exception {
        OffsetDateTime date = OffsetDateTime.of(LocalDateTime.of(2012, 12, 12, 10, 00), ZoneOffset.UTC);
        ConferenceInfoDto mockConferenceInfoDto = new ConferenceInfoDto("conference", date, date);
        when(displayConferenceService.getConferenceInfo()).thenReturn(mockConferenceInfoDto);

        String response = mockMvc.perform(get("/conferenceDetails")
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse().getContentAsString();

        assertTrue(response.contains("\"name\":\"conference\""));
        assertTrue(response.contains("\"startDate\":\"2012-12-12T10:00:00Z\""));
        assertTrue(response.contains("\"endDate\":\"2012-12-12T10:00:00Z\""));
        verify(displayConferenceService, times(1)).getConferenceInfo();
    }

    @Test
    public void testGetConferenceInfoNullPtr() throws Exception {
        when(displayConferenceService.getConferenceInfo()).thenThrow(new NullPointerException("Conference info not found"));

        mockMvc.perform(get("/conferenceDetails")
                        .contentType("application/json"))
                .andExpect(status().isConflict())
                .andExpect(content().string("Conference info not found"));

        verify(displayConferenceService, times(1)).getConferenceInfo();
    }

    @Test
    public void testGetMetadata() throws Exception {
        MetadataDto mockData = MetadataDto.builder()
                .contactEmail("mail")
                .contactCellNumber("number")
                .contactWebsite("website")
                .contactLandlineNumber("number").build();

        when(displayConferenceService.getMetadata()).thenReturn(mockData);

        String response = mockMvc.perform(get("/conferenceMetadata")
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse().getContentAsString();

        assertTrue(response.contains("\"contactEmail\":\"mail\""));
        assertTrue(response.contains("\"contactWebsite\":\"website\""));
        verify(displayConferenceService, times(1)).getMetadata();
    }

    @Test
    public void testGetMetadataNullPtr() throws Exception {
        when(displayConferenceService.getMetadata()).thenThrow(new NullPointerException("Metadata not found"));

        mockMvc.perform(get("/conferenceMetadata")
                        .contentType("application/json"))
                .andExpect(status().isConflict())
                .andExpect(content().string("Metadata not found"));

        verify(displayConferenceService, times(1)).getMetadata();
    }
}
