package de.isemwaf.smartFridge;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.isemwaf.smartFridge.model.Food;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class FoodInventoryControllerTest {
    @Autowired
    private MockMvc mockMvc;

    private static final ObjectMapper mapper;

    static {
        mapper = new ObjectMapper();
    }

    @Test
    public void testFoodInventoryGetter() throws Exception
    {
        int id = 1;
       mockMvc.perform(get("/api/foodinventory/" + id)).andExpect(status().isOk()).andExpect(jsonPath("$[0].id", Matchers.equalTo("1")));
       id = -1;
       mockMvc.perform(get("/api/foodinventory/" + id)).andExpect(status().isOk());
    }

    @Test
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
        String json = mapper.writeValueAsString(food);
        mockMvc.perform(post("/api/foodinventory/").contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8").content(json).accept(MediaType.APPLICATION_JSON)).andExpect(status().isCreated()).andExpect(jsonPath("$.id", Matchers.equalTo(999)));
    }
    @Test
    public void testFoodInventoryDeleteItem() throws Exception
    {
        int id = 999;
        mockMvc.perform(delete("api/foodinventory/" + id)).andExpect(status().isOk());
    }
}
