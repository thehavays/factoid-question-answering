package edu.ozu.model;

import lombok.Data;

import java.util.ArrayList;

@Data
public class Paragraph {
    private ArrayList<QuestionAnswer> qas;
    private String context;
}