package de.isemwaf.smartFridge.controller;

import de.isemwaf.smartFridge.model.Food;
import de.isemwaf.smartFridge.repositories.AccountRepository;
import de.isemwaf.smartFridge.services.impl.AccountUserDetailsService;
import de.isemwaf.smartFridge.services.FoodService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FoodController.class)
public class FoodControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountRepository accountRepository;

    @MockBean
    private AccountUserDetailsService accountUserDetailsService;

    @MockBean
    private FoodService foodService;



    @Test
    @WithMockUser("Test")
    void createFoodWithBarcode() throws Exception {
        String barcode = "1231234142";
        Food food = new Food();
        food.setId(999);
        food.setName("Kuchen");
        food.setBarcode(barcode);
        food.setQuantity("750");
        food.setLastModified(new Date());
        food.setVersion(2);
        food.setCreated(new Date());
        Mockito.when(foodService.searchForBarcode(any(String.class))).thenReturn(Optional.of(food));

        mockMvc.perform(post("/api/food").contentType(MediaType.APPLICATION_JSON).content("Barcode"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.equalTo(999)));
    }

    @Test
    @WithMockUser("Test")
    void getFoodWithId() throws Exception {
        String barcode = "1231234142";
        Food food = new Food();
        food.setId(999);
        food.setName("Kuchen");
        food.setBarcode(barcode);
        food.setQuantity("750");
        food.setLastModified(new Date());
        food.setVersion(2);
        food.setCreated(new Date());
        Mockito.when(foodService.getFood(any(Long.class))).thenReturn(food);

        mockMvc.perform(get("/api/food/"+food.getId())).andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", Matchers.equalTo(999)));
    }
}
