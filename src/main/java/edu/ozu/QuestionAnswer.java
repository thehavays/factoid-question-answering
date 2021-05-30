package edu.ozu;

import edu.ozu.model.MorphologicalQuestion;
import edu.ozu.model.Title;

import java.util.ArrayList;

/**
 * Hello world!
 */
public class QuestionAnswer {


    public static void main(String[] args) {
        ArrayList<Title> titles = Util.getData(false);
        PostgreSqlAdapter adapter = new PostgreSqlAdapter();
        for (Title title : titles) {
            for (String question : title.getQuestion()) {
                MorphologicalQuestion morphologicalQuestion = new MorphologicalQuestion(question, Util.getQuestionAnalysis(question));
                ArrayList<String> wordRoots = new ArrayList<>();
                ArrayList<String> wordSuffixes = new ArrayList<>();
                for(String word : morphologicalQuestion.getAnalyses()){
                    wordRoots.add(word.substring(0, word.indexOf("+")));
                    wordSuffixes.add(word.substring(word.indexOf("+") + 1));
                    adapter.getMostSimilarQuestion(wordRoots);
                }
                System.out.println(wordRoots);
                System.out.println(wordSuffixes);
            }
        }
    }
}