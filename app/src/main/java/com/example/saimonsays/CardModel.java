package com.example.saimonsays;

public class CardModel {
    public String getPlayerName(){return  name;}
    public int getScore(){return  score;}
    public String getDate(){return  date;}

    String name;
    int score;
    String date;

    public CardModel(String name, int score, String date) {
        this.name = name;
        this.score = score;
        this.date = date;

    }
}

