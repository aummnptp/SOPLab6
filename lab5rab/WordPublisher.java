package com.example.lab5rab;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Objects;

@RestController
public class WordPublisher  {
    @Autowired
    private RabbitTemplate rabbitTemplate;
    protected Word words = new Word();
//    protected Sentence sentences = new Sentence();

//    ArrayList<String> goodWords = words.get();

    @RequestMapping(value = "/getWord",method = RequestMethod.GET)
    public Word getWord(){
        return words;

    }

    @RequestMapping(value = "/addBad/{word}",method = RequestMethod.POST)
    public ArrayList<String> addBadWord(@PathVariable("word") String s){
        words.badWords.add(s);
        return words.badWords;

    }


    @RequestMapping(value = "/delBad/{word}",method = RequestMethod.GET)
    public ArrayList<String> deleteBadWord(@PathVariable("word") String s){
        words.badWords.remove(s);
        return words.badWords;
    }

    @RequestMapping(value = "/addGood/{s}",method = RequestMethod.POST)
    public ArrayList<String> addGoodWord(@PathVariable("s") String s){
        words.goodWords.add(s);
        return words.goodWords;

    }

    @RequestMapping(value = "/delGood/{word}",method = RequestMethod.GET)
    public ArrayList<String> deleteGoodWord(@PathVariable("word") String s){
        words.goodWords.remove(s);
        return words.goodWords;

    }


    @RequestMapping(value = "/proof/{sentence}",method = RequestMethod.POST)
    public String proofSentence(@PathVariable("sentence") String s){
        boolean isGood = false;
        boolean isBad = false;
        for (String goodWord : words.goodWords) {
            if (s.contains(goodWord)) {
                isGood = true;
                break;
            }
        }
        for (String badWord : words.badWords) {
            if (s.contains(badWord)) {
                isBad = true;
                break;
            }
        }

        if(isGood && isBad){
            rabbitTemplate.convertAndSend("Fanout","",s);
            return "Found Good and Bad Sentence";
        }
        else if(isGood){
            rabbitTemplate.convertAndSend("Direct","good",s);
            return "Found Good Sentence";
        }
        else if(isBad){
            rabbitTemplate.convertAndSend("Direct","bad",s);
            return "Found Bad Sentence";
        }
        return "Not Found Good or Bad Sentence";
    }

    @RequestMapping(value = "/getSentence",method = RequestMethod.GET)
    public Sentence getSentence(){
        Object result = rabbitTemplate
                .convertSendAndReceive("Direct","","");
        return ((Sentence) result);

    }

}