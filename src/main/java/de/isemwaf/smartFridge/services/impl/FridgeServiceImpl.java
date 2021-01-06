package de.isemwaf.smartFridge.services.impl;


import de.isemwaf.smartFridge.model.Fridge;
import de.isemwaf.smartFridge.repositories.FridgeRepository;
import de.isemwaf.smartFridge.services.FridgeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FridgeServiceImpl implements FridgeService {

    private final FridgeRepository fridgeRepository;

    @Autowired
    public FridgeServiceImpl(FridgeRepository fridgeRepository) {
        this.fridgeRepository = fridgeRepository;
    }


    @Override
    public Fridge createFridge(Fridge fridge) {
        return fridgeRepository.save(fridge);
    }

    @Override
    public Fridge getFridge(long id) {
        return fridgeRepository.findById(id).isPresent() ? fridgeRepository.findById(id).get() : null;
    }

    @Override
    public Fridge getFridgeByAccountId(long userId) {
        return fridgeRepository.findByAccount_Id(userId);
    }
}
