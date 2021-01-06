package de.isemwaf.smartFridge.services;


import de.isemwaf.smartFridge.model.Fridge;

public interface FridgeService {
    Fridge createFridge(Fridge fridge);
    Fridge getFridge(long id);
    Fridge getFridgeByAccountId(long userId);
}