package com.example.lab5rab;

import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.ArrayList;


public class Sentence implements Serializable {
    ArrayList<String>  badSentences = new ArrayList<String>();
    ArrayList<String>   goodSentences = new ArrayList<String>();

    public ArrayList<String> getBadSentences() {
        return badSentences;
    }

    public ArrayList<String> getGoodSentences() {
        return goodSentences;
    }


    public void setBadSentences(ArrayList<String> badSentences) {
        this.badSentences = badSentences;
    }

    public void setGoodSentences(ArrayList<String> goodSentences) {
        this.goodSentences = goodSentences;
    }
    //    public Sentence(ArrayList<String> badSentences, ArrayList<String> goodSentences) {
//        this.badSentences = badSentences;
//        this.goodSentences = goodSentences;
//    }
}