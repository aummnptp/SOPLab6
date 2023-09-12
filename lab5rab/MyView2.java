package com.example.lab5rab;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.Route;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Arrays;


@Route(value="index2")
public class MyView2 extends HorizontalLayout {

    private TextArea tfWord, tfSen,tfGoodSen,tfBadSen;
    private ComboBox<String> cbBadWord, cbGoodWord;
    private Button addGood,addBad,addSen,showSen;

    public MyView2(){
        tfWord = new TextArea("Add Word");
        tfSen = new TextArea("Add Sentence");
        tfGoodSen = new TextArea("Good Sentences");
        tfBadSen = new TextArea("Bad Sentences");
        tfGoodSen.setReadOnly(true);
        tfBadSen.setReadOnly(true);

        cbBadWord = new ComboBox();
        cbBadWord.setLabel("Bad Words");

        cbGoodWord = new ComboBox<>();
        cbGoodWord.setLabel("Good Words");

//        cbGoodWord.setItems();

        addGood = new Button("Add Good Word");
        addBad = new Button("Add Bad Word");
        addSen = new Button("Add Sentence");
        showSen = new Button("Show Sentences");

        VerticalLayout vl1 = new VerticalLayout();
        VerticalLayout vl2 = new VerticalLayout();


        vl1.add(tfWord,addGood,addBad,cbGoodWord,cbBadWord);
        vl2.add(tfSen,addSen,tfGoodSen,tfBadSen,showSen);
        this.add(vl1,vl2);

        addGood.addClickListener(event ->{
            String wordInput = tfWord.getValue();
            ArrayList<String> out = WebClient.create()
                    .post()
                    .uri("http://localhost:8080/addGood/"+wordInput)
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<ArrayList<String>>() {})
                    .block();
            cbGoodWord.setItems(out.toArray(new String[0]));
        });


        addBad.addClickListener(event -> {
            String wordInput = tfWord.getValue();
            ArrayList<String> out = WebClient.create()
                    .post()
                    .uri("http://localhost:8080/addBad/"+wordInput)
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<ArrayList<String>>() {})
                    .block();
            cbBadWord.setItems(out.toArray(new String[0]));
        });


        addSen.addClickListener(event ->{
            String senInput = tfSen.getValue();
            String out = WebClient.create()
                    .post()
                    .uri("http://localhost:8080/proof/"+senInput)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            new Notification( out, 3000).open();

        });

        showSen.addClickListener(event ->{

            Sentence out = WebClient.create()
                    .get()
                    .uri("http://localhost:8080/getSentence")
                    .retrieve()
                    .bodyToMono(Sentence.class)
                    .block();
            tfGoodSen.setValue(out.goodSentences.toString());
            tfBadSen.setValue(out.badSentences.toString());


        });

//        getWord
        Word out = WebClient.create()
                .get()
                .uri("http://localhost:8080/getWord")
                .retrieve()
                .bodyToMono(Word.class)
                .block();
        cbGoodWord.setItems(out.goodWords.toArray(new String[0]));
        cbBadWord.setItems(out.badWords.toArray(new String[0]));
    }
}