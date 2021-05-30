package edu.ozu;

import edu.ozu.model.MorphologicalQuestion;
import edu.ozu.model.QuestionAnswer;
import edu.ozu.model.Title;

import java.util.ArrayList;

/**
 * Hello world!
 */
public class QuestionAnswerer {


    public static void main(String[] args) {
        ArrayList<Title> titles = Util.getData(false);
        PostgreSqlAdapter adapter = new PostgreSqlAdapter();
        for (Title title : titles) {
            for (QuestionAnswer questionAnswer : title.getQuestionAnswers()) {
                MorphologicalQuestion morphologicalQuestion = new MorphologicalQuestion(questionAnswer.getQuestion(), Util.getQuestionAnalysis(questionAnswer.getQuestion()));
                ArrayList<String> wordRoots = new ArrayList<>();
                ArrayList<String> wordSuffixes = new ArrayList<>();
                for (String word : morphologicalQuestion.getAnalyses()) {
                    wordRoots.add(word.substring(0, word.indexOf("+")));
                    wordSuffixes.add(word.substring(word.indexOf("+") + 1));
                }
                System.out.println("Question = " + questionAnswer.getQuestion());
                System.out.println("Actual answer = " + questionAnswer.getAnswers().get(0).getText());
                System.out.println("System answer = " + adapter.getMostSimilarQuestion(wordRoots));
            }
        }
    }
}