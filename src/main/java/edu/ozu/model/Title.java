package edu.ozu.model;

import lombok.Data;

import java.util.ArrayList;

@Data
public class Title {
    private String title;
    private ArrayList<Paragraph> paragraphs;

    public ArrayList<QuestionAnswer> getQuestionAnswers() {
        ArrayList<QuestionAnswer> questionAnswers = new ArrayList<>();
        for (Paragraph paragraph : paragraphs) {
            questionAnswers.addAll(paragraph.getQas());
        }
        return questionAnswers;
    }
}