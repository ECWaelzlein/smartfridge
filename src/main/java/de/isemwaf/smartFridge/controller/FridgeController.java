package de.isemwaf.smartFridge.controller;

import de.isemwaf.smartFridge.model.Account;
import de.isemwaf.smartFridge.model.Fridge;
import de.isemwaf.smartFridge.model.Recipe;
import de.isemwaf.smartFridge.model.json.IngredientList;
import de.isemwaf.smartFridge.services.FridgeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
public class FridgeController {

    private final FridgeService fridgeService;

    @Autowired
    public FridgeController(FridgeService fridgeService) {
        this.fridgeService = fridgeService;
    }

    @GetMapping(path = "/api/fridge/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Fridge> getFridge(@PathVariable String id) {
        Fridge fridge  = fridgeService.getFridge(Long.parseLong(id));

        if (fridge == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(fridge, HttpStatus.OK);
    }


    @PostMapping(path = "/api/fridge", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Fridge> createRecipeBasedOnIngredients(@RequestBody String userId) {
        Fridge fridge = new Fridge();
        Account account = new Account();

        if (account == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        fridge = fridgeService.createFridge(fridge);

        return new ResponseEntity<>(fridge, HttpStatus.CREATED);
    }

}
