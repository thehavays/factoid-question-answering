package edu.ozu;

import edu.ozu.model.MorphologicalQuestion;
import edu.ozu.model.Title;

import java.util.ArrayList;

/**
 * Hello world!
 */
public class JsonFetcher {


    public static void main(String[] args) {
        ArrayList<Title> titles = Util.getData(true);
        PostgreSqlAdapter adapter = new PostgreSqlAdapter();
        for (Title title : titles) {
            for (String question : title.getQuestion()) {
                MorphologicalQuestion morphologicalQuestion = new MorphologicalQuestion(question, Util.getQuestionAnalysis(question));
                int questionId = adapter.insertQuestion(morphologicalQuestion.getQuestion());
                for (String word : morphologicalQuestion.getAnalyses()) {
                    int wordId = adapter.insertWord(word);
                    if (!adapter.isExistQuestionWord(questionId, wordId)) {
                        boolean insertQuestionWord = adapter.insertQuestionWord(questionId, wordId);
                        System.out.println(questionId + "\t" + wordId);
                        System.out.println(insertQuestionWord);
                    } else {
                        System.out.println("existed question-word pair" + questionId + "\t" + wordId); //TODO: remove
                    }
                }
            }
        }
    }
}