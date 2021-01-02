package de.isemwaf.smartFridge.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//Vielleicht ein DB-Mock oder Service Mock bauen, um nicht auf der echten DB zu arbeiten
//So werden nicht nur Controller sondern auch DB und Service getestet (Kein strikter Unit-Test)
@WebMvcTest
class MealControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private static final ObjectMapper mapper = new ObjectMapper();

    @Test
    void getMeals() throws Exception {
        int id = -1;
        mockMvc.perform(get("/api/meal/"+id)).andExpect(status().is(422));
    }

    @Test
    void deleteMeal() throws Exception {
        int id = -1;
        mockMvc.perform(delete("/api/meal/"+id)).andExpect(status().is(422));
    }

    @Test
    void addMeal() throws Exception {
        int id = -1;
        mockMvc.perform(post("/api/meal").contentType(MediaType.APPLICATION_JSON).content("FakeMeal")).andExpect(status().is(422));
    }

    @Test
    void changeMeal() throws Exception {
        int id = -1;
        mockMvc.perform(post("/api/meal/"+id)).andExpect(status().is(422));
    }
}