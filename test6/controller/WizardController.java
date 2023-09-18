package com.example.test6.controller;

import com.example.test6.pojo.Wizard;
import com.example.test6.repository.WizardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class WizardController {

    @Autowired
    private WizardService wizardService;

    @RequestMapping(value = "/wizards", method = RequestMethod.GET)
    public ResponseEntity<?> getWizard() {
        List<Wizard> wizards = wizardService.retrieveWizards();
        return ResponseEntity.ok(wizards);
    }

//    @RequestMapping(value = "/addWizard", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
//    public ResponseEntity<?> addWizard(
//            @RequestParam("name") String name,
//            @RequestParam("gender") String gender,
//            @RequestParam("dollars") Double dollars,
//            @RequestParam("school") String school,
//            @RequestParam("house") String house,
//            @RequestParam("position") String position){
//        Wizard n = new Wizard(null,gender, name, school, house, dollars, position);
//        String response = wizardService.saveWizard(n);
//        System.out.println("n : " + n);
//        System.out.println("response : " + response);
//        return ResponseEntity.ok(response);
//    }

    @RequestMapping(value = "/addWizard", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> addWizard(@RequestBody Wizard wizard)  {
        String response = wizardService.addWizard(wizard);
        return ResponseEntity.ok(response);
    }

    @RequestMapping(value ="/updateWizard/{_id}", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> updateWizard(@PathVariable("_id") String _id, @RequestBody Wizard wizard) {
        String response = wizardService.updateWizard(_id, wizard);
        return ResponseEntity.ok(response);
    }
    @RequestMapping(value = "/deleteWizard/{_id}", method = RequestMethod.POST)
    public ResponseEntity<?> deleteWizard(@PathVariable("_id") String _id) {
        String wizard = wizardService.deleteWizard(_id);
        return ResponseEntity.ok(wizard);
    }



}