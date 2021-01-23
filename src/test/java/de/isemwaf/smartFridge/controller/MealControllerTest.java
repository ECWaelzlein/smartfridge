package de.isemwaf.smartFridge.controller;


import de.isemwaf.smartFridge.repositories.AccountRepository;
import de.isemwaf.smartFridge.services.AccountUserDetailsService;
import de.isemwaf.smartFridge.services.MealService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class, DataSourceTransactionManagerAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
@WebMvcTest(MealController.class)
class MealControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MealService mealService;

    @MockBean
    private AccountRepository accountRepository;

    @MockBean
    private AccountUserDetailsService accountUserDetailsService;

    @BeforeEach
    void beforeEach(){

    }

    @WithMockUser("Test")
    @Test
    void getMealInvalidId() throws Exception {
        Mockito.when(mealService.findMeal(-1)).thenReturn(Optional.empty());

        int id = -1;
        mockMvc.perform(get("/api/meal/"+id+"?userId=-1")).andExpect(status().is(422));
    }


    @Test
    @WithMockUser("Test")
    void deleteMealInvalidId() throws Exception {
        int id = -1;
        Mockito.when(mealService.deleteMeal(-1)).thenThrow(new EmptyResultDataAccessException("Invalid id", 11));
        mockMvc.perform(delete("/api/meal/"+id)).andExpect(status().is(422));
    }

    @Test
    @WithMockUser("Test")
    void addInvalidMeal() throws Exception {
        int id = -1;

        mockMvc.perform(post("/api/meal").contentType(MediaType.APPLICATION_JSON).content("\"FakeMeal\":\"\"")).andExpect(status().is(422));
    }

    @Test
    @WithMockUser("Test")
    void changeMealInvalidBody() throws Exception {
        int id = -1;
        mockMvc.perform(post("/api/meal/"+id)).andExpect(status().is(422));
    }
}