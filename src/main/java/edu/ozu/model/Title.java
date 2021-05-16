package edu.ozu.model;

import lombok.Data;

import java.util.ArrayList;

@Data
public class Title {
    private String title;
    private ArrayList<Paragraph> paragraphs;

    public ArrayList<String> getQuestion() {
        ArrayList<String> questions = new ArrayList<>();
        for (Paragraph paragraph : paragraphs) {
            for (QuestionAnswer questionAnswer : paragraph.getQas()) {
                questions.add(questionAnswer.getQuestion());
            }
        }
        return questions;
    }
}