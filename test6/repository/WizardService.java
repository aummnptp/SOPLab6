package com.example.test6.repository;

import com.example.test6.pojo.Wizard;
import com.example.test6.pojo.Wizards;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class WizardService {
    @Autowired
    private WizardRepository repository;

    public WizardService(WizardRepository repository) {
        this.repository = repository;
    }
    public List<Wizard> retrieveWizards() {
        return repository.findAll();
    }
    public Wizard retrieveWizardByName(String name) {
        return repository.findByName(name);
    }
    public String addWizard(Wizard wizard) {
        repository.save(wizard);
        return "Added Successfully";
    }

    public String updateWizard(String _id, Wizard updateWizard) {
        Optional<Wizard> findingWizard = repository.findById(_id);  //updateWizard.get_id()
        if (findingWizard.isPresent()) {
            Wizard wizard = findingWizard.get();
            wizard.setName(updateWizard.getName());
            wizard.setHouse(updateWizard.getHouse());
            wizard.setSchool(updateWizard.getSchool());
            wizard.setMoney(updateWizard.getMoney());
            wizard.setSex(updateWizard.getSex());
            wizard.setPosition(updateWizard.getPosition());
            repository.save(wizard);
            return "Updated Successfully";
        }
        return "Wizard not found";
    }

    public String deleteWizard(String _id) {
        Optional<Wizard> findingWizard = repository.findById(_id);
        if (findingWizard.isPresent()) {
            repository.deleteById(_id);
            return "Deleted Successfully";
        }
        return "Wizard not found";
    }


}