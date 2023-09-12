package com.example.lab5rab;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class SentenceConsumer {
    protected Sentence sentences = new Sentence();
    @RabbitListener(queues = "GoodWordQueue")
    public void addGoodSentence(String s){
        sentences.goodSentences.add(s);
//        System.out.println(sentences.goodSentences);
    }
    @RabbitListener(queues = "BadWordQueue")
    public void addBadSentence(String s){
        sentences.badSentences.add(s);
//        System.out.println(sentences.badSentences);
    }
    @RabbitListener(queues = "GetQueue")
    public Sentence getSentences() {
        return sentences;
    }





}