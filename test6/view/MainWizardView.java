package com.example.test6.view;


import com.example.test6.pojo.Wizard;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;

@Route(value = "mainPage.it")
public class MainWizardView extends VerticalLayout {
    TextField name = new TextField("Fullname");
    RadioButtonGroup<String> gender = new RadioButtonGroup<>("Gender :");
    HorizontalLayout h1 = new HorizontalLayout();
    ComboBox<String> position = new ComboBox<>("Position");
    NumberField dollars = new NumberField("Dollars");
    ComboBox<String> school = new ComboBox<>("School");
    ComboBox<String> house = new ComboBox<>("House");
    ArrayList<Wizard> wizards;
    int current = -1;
    public MainWizardView() {
        name.setPlaceholder("Fullname");
        gender.setItems("Male", "Female");
        position.setItems("Student", "Teacher");
//        position.setPlaceholder("Position");


        dollars.setPrefixComponent(new Span("$"));
        school.setItems("Hogwarts", "Beauxbatons", "Durmstrang");
        school.setPlaceholder("School");
        house.setItems("Gryffindor", "Ravenclaw", "Hufflepuff", "Slytherin");
        house.setPlaceholder("House");

        Button prev = new Button("<<");
        Button next = new Button(">>");
        Button create = new Button("Create");
        Button update = new Button("Update");
        Button delete = new Button("Delete");
        h1.add(prev, create, update, delete, next);

        add(name, gender, position, dollars, school, house, h1);

        wizards = this.getWizards();

//        this.setTextField(wizards.get(current));
        prev.addClickListener(buttonClickEvent -> {this.backward();});
        next.addClickListener(buttonClickEvent -> {this.forward();});


        create.addClickListener(event -> {
            System.out.println("in create btn");

            Wizard wizard = new Wizard(String.valueOf(gender.getValue()), String.valueOf(name.getValue()), String.valueOf(school.getValue()), String.valueOf(house.getValue()), dollars.getValue(), String.valueOf(position.getValue()));
            System.out.println("wizard : " + wizard);

            String output = String.valueOf(WebClient.create()
                    .post()
                    .uri("http://localhost:8080/addWizard")
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromValue(wizard))
                    .retrieve()
                    .bodyToMono(String.class)
                    .block());

            System.out.println("output : " + output);
            wizards = this.getWizards();
            current = getWizards().size()-1;
            System.out.println(output);

        });

        update.addClickListener(buttonClickEvent -> {

            Wizard wizard = new Wizard(String.valueOf(gender.getValue()), name.getValue(), String.valueOf(school.getValue()), String.valueOf(house.getValue()), (dollars.getValue()), String.valueOf(position.getValue()));
            System.out.println("wizard : " + wizard);
            System.out.println("current.get_id() : " + wizards.get(current).get_id());
            String output = String.valueOf(WebClient.create()
                    .post()
                    .uri("http://localhost:8080/updateWizard/"+ wizards.get(current).get_id())
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromValue(wizard))
                    .retrieve()
                    .bodyToMono(String.class)
                    .block());
            wizards = this.getWizards();
            System.out.println("output : " + output);
        });

        delete.addClickListener(buttonClickEvent -> {
            String output = String.valueOf(WebClient.create().post()
                    .uri("http://localhost:8080/deleteWizard/"+wizards.get(current).get_id())
                    .retrieve()
                    .bodyToMono(String.class)
                    .block());

            wizards = this.getWizards();
            this.backward();

            System.out.println("current : " + wizards.get(current).get_id());
            System.out.println("output : "+ output);
        });

    }

    private void forward() {
        current++;
        if (current >= wizards.size()) { current = wizards.size()-1; }
        this.setTextField(wizards.get(current));
    }
    private void backward() {
        current--;
        if (current < 0) {current = 0;}
        this.setTextField(wizards.get(current));
    }

    public void setTextField(Wizard wizard) {
        name.setValue(wizard.getName());
        String gender;
        if (wizard.getSex().equals("m")) {gender = "Male";}
        else {gender = "Female";}
        this.gender.setValue(gender);
        String position = wizard.getPosition();
        this.position.setValue(position.substring(0,1).toUpperCase()+position.substring(1));
        dollars.setValue(wizard.getMoney());
        school.setValue(wizard.getSchool());
        house.setValue(wizard.getHouse());

    }

    public ArrayList<Wizard> getWizards() {
        ArrayList<Wizard> response = WebClient.create().get().uri("http://localhost:8080/wizards").retrieve().bodyToMono(ArrayList.class).block();
        ObjectMapper mapper = new ObjectMapper();
        ArrayList<Wizard> wizards = mapper.convertValue(response, new TypeReference<ArrayList<Wizard>>() {});
        return wizards;
    }
}