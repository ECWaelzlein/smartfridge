package de.isemwaf.smartFridge.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.isemwaf.smartFridge.model.Food;
import de.isemwaf.smartFridge.model.FoodInventory;
import de.isemwaf.smartFridge.model.Fridge;
import de.isemwaf.smartFridge.model.json.FoodInventoryModel;
import de.isemwaf.smartFridge.repositories.AccountRepository;
import de.isemwaf.smartFridge.services.impl.AccountUserDetailsService;
import de.isemwaf.smartFridge.services.FoodInventoryService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;

import static org.mockito.ArgumentMatchers.isA;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FoodInventoryController.class)
public class FoodInventoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FoodInventoryService foodInventoryService;

    @MockBean
    private AccountRepository accountRepository;

    @MockBean
    private AccountUserDetailsService accountUserDetailsService;

    private static final ObjectMapper mapper;

    static {
        mapper = new ObjectMapper();
    }

    @Test
    @WithMockUser("Test")
    public void testFoodInventoryGetter() throws Exception
    {
        Food food = new Food();
        food.setId(999);
        food.setName("Kuchen");
        food.setBarcode("1231234142");
        food.setQuantity("750");
        food.setLastModified(new Date());
        food.setVersion(2);
        food.setCreated(new Date());
        FoodInventory inventory = new FoodInventory();
        inventory.setId(1);
        inventory.setFood(food);
        inventory.setFridge(new Fridge());
        inventory.setExpirationDate(new Date());
        inventory.setCreated(new Date());
        inventory.setLastModified(new Date());

        Mockito.when(foodInventoryService.getItem(1)).thenReturn(inventory);
        Mockito.when(foodInventoryService.getItem(-1)).thenThrow(new EmptyResultDataAccessException("Invalid id", 11));
        int id = 1;
        mockMvc.perform(get("/api/food-inventory/" + id+"?userId=1")).andExpect(status().isOk())
                .andExpect(jsonPath("$[0]['id']", Matchers.equalTo(1)));
        id = -1;
        mockMvc.perform(get("/api/food-inventory/" + id+"?userId=1")).andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithMockUser("Test")
    public void testFoodInventoryCreateNewItem() throws Exception
    {
        Food food = new Food();
        food.setId(999);
        food.setName("Kuchen");
        food.setBarcode("1231234142");
        food.setQuantity("750");
        food.setLastModified(new Date());
        food.setVersion(2);
        food.setCreated(new Date());
        FoodInventory inventory = new FoodInventory();
        inventory.setId(1);
        inventory.setFood(food);
        inventory.setFridge(new Fridge());
        inventory.setExpirationDate(new Date());
        inventory.setCreated(new Date());
        inventory.setLastModified(new Date());
        FoodInventoryModel model = new FoodInventoryModel();
        model.setFoodId(""+food.getId());
        model.setUserId("1");
        model.setExpirationDate(new Date());
        Mockito.when(foodInventoryService.saveItem(isA(FoodInventoryModel.class))).thenReturn(inventory);
        String json = mapper.writeValueAsString(model);
        System.out.println(json);
        mockMvc.perform(post("/api/food-inventory/").contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8").content(json).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated()).
                andExpect(jsonPath("$.id", Matchers.equalTo(1)));
    }
    @Test
    @WithMockUser("Test")
    public void testFoodInventoryDeleteItem() throws Exception
    {
        int id = 999;
        mockMvc.perform(delete("/api/food-inventory/" + id)).andExpect(status().isNoContent());
    }
    @Test
    @WithMockUser("Test")
    public void testFoodInventoryDeleteInvalidItem() throws Exception
    {
        int id = 999;
        Mockito.doThrow(new EmptyResultDataAccessException("Invalid id", 11)).when(foodInventoryService).deleteItem(id);
        mockMvc.perform(delete("/api/food-inventory/" + id)).andExpect(status().isUnprocessableEntity());
    }
}
