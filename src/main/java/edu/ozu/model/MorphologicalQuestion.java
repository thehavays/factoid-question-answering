package edu.ozu.model;

import lombok.Data;

import java.util.ArrayList;

@Data
public class MorphologicalQuestion {
    private String question;
    private ArrayList<String> analyses;

    public MorphologicalQuestion(String question, ArrayList<String> analyses) {
        this.question = question;
        this.analyses = analyses;
    }
}
