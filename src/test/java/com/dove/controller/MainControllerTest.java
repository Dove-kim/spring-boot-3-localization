package com.dove.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.TimeZone;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MainController.class)
class MainControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void givenAcceptTimeZoneHeader_whenResolveTimeZone_thenTimeZoneIsSet() throws Exception {
        // Given
        String acceptTimeZone = "Europe/Paris";
        TimeZone expectedTimeZone = TimeZone.getTimeZone(acceptTimeZone);

        // When
        String response = mockMvc.perform(get("/").header("Accept-TimeZone", acceptTimeZone))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        // Then
        Assertions.assertEquals(response, expectedTimeZone.getID());
    }
}