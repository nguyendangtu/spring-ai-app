package com.example.springbootapp;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class StockControllerIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void shouldCreateAndReadStock() throws Exception {
        String createPayload = "{\"symbol\":\"AAPL\",\"companyName\":\"Apple Inc\",\"price\":210.50,\"quantity\":25}";

        mockMvc.perform(post("/api/stocks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createPayload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.symbol").value("AAPL"));

        mockMvc.perform(get("/api/stocks?page=0&size=10&sortBy=id&direction=asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].symbol").value("AAPL"));

        mockMvc.perform(put("/api/stocks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"symbol\":\"AAPL\",\"companyName\":\"Apple Inc.\",\"price\":220.00,\"quantity\":30}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.companyName").value("Apple Inc."));

        mockMvc.perform(delete("/api/stocks/1"))
                .andExpect(status().isNoContent());
    }
}
