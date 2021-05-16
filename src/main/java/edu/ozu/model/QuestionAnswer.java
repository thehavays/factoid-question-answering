package edu.ozu.model;

import lombok.Data;

import java.util.ArrayList;

@Data
public class QuestionAnswer {
    private int id;
    private String question;
    private ArrayList<Answer> answers;
}
