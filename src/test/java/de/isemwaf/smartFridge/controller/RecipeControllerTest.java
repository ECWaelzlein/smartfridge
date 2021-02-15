package de.isemwaf.smartFridge.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.isemwaf.smartFridge.model.Recipe;
import de.isemwaf.smartFridge.model.json.IngredientList;
import de.isemwaf.smartFridge.repositories.AccountRepository;
import de.isemwaf.smartFridge.services.impl.AccountUserDetailsService;
import de.isemwaf.smartFridge.services.RecipeService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RecipeController.class)
class RecipeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountRepository accountRepository;

    @MockBean
    private AccountUserDetailsService accountUserDetailsService;

    @MockBean
    private RecipeService recipeService;

    private static final ObjectMapper mapper = new ObjectMapper();


    private Recipe recipe;

    @BeforeEach
    void setUp(){

        recipe = new Recipe();
        recipe.setName("Name");
        recipe.setId(11);
        recipe.setCreated(new Date());
        recipe.setLastModified(new Date());
    }

    @AfterEach
    void tearDown() {
        recipe = null;
    }

    @Test
    @WithMockUser("Test")
    void getRecipe() throws Exception {

        Mockito.when(recipeService.getRecipe(any(Long.class))).thenReturn(recipe);

        mockMvc.perform(get("/api/recipe/11")).andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.equalTo(11)))
                .andExpect(jsonPath("$['name']", Matchers.equalTo("Name")));
    }

    @Test
    @WithMockUser("Test")
    void createRecipeBasedOnIngredients() throws Exception {
    Mockito.when(recipeService.getRecipeBasedOnIngredients(any(IngredientList.class))).thenReturn(recipe);
    Mockito.when(recipeService.saveRecipe(recipe)).thenReturn(recipe);

        mockMvc.perform(post("/api/recipe/").contentType(MediaType.APPLICATION_JSON)
                .content("{\"ingredientList\":[{\"ingredient\":\"Tonic Water\"}]}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", Matchers.equalTo(11)))
                .andExpect(jsonPath("$['name']", Matchers.equalTo("Name")));
    }

    @Test
    @WithMockUser("Test")
    void testCreateRecipeBasedOnIngredients() throws Exception {
        Mockito.when(recipeService.getRandomRecipe(any(String.class))).thenReturn(recipe);
        Mockito.when(recipeService.saveRecipe(recipe)).thenReturn(recipe);

        mockMvc.perform(post("/api/recipe/random?tags=vegetarian,Peanut,main+course"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", Matchers.equalTo(11)))
                .andExpect(jsonPath("$['name']", Matchers.equalTo("Name")));
    }
}