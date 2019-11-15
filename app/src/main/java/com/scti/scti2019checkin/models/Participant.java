package com.scti.scti2019checkin.models;

public class Participant {
    private int id;
    private String name;

    public Participant(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public static String capitalize(String text) {
        text = text.toLowerCase();
        String[] words = text.split(" ");
        String result = "";

        for (int i = 0; i < words.length; i++) {
            String word = words[i];
            if (!word.equals("da") && !word.equals("de") && !word.equals("do") && !word.equals("e") && !word.equals("a")) {
                words[i] = word.substring(0, 1).toUpperCase() + word.substring(1);
            }
        }

        for (int i = 0; i < words.length; i++) {
            result += words[i];
            if (i != words.length - 1) result += " ";
        }

        return result;
    }
}
