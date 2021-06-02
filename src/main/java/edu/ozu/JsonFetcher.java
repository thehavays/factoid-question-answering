package edu.ozu;

import edu.ozu.model.MorphologicalQuestion;
import edu.ozu.model.Paragraph;
import edu.ozu.model.QuestionAnswer;
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
            for (Paragraph paragraph : title.getParagraphs()) {
                String context = paragraph.getContext();
                ArrayList<String> contentAnalysis = Util.getContentAnalysis(context);
                int contextId = adapter.insertContext(context);
                if (contextId != -1) {
                    for (String word : contentAnalysis) {
                        int wordId = adapter.insertWord(word);
                        if ((wordId != -1) && (!adapter.isExistContextWord(contextId, wordId))) {
                            adapter.insertContextWord(contextId, wordId);
                        } else {
                            System.out.println("word cannot insert or context-word pair already exist");
                        }
                    }
                    for (QuestionAnswer questionAnswer : paragraph.getQas()) {
                        MorphologicalQuestion morphologicalQuestion = new MorphologicalQuestion(questionAnswer.getQuestion(), Util.getContentAnalysis(questionAnswer.getQuestion()));
                        int questionId = adapter.insertQuestion(morphologicalQuestion.getQuestion(), questionAnswer.getAnswers().get(0).getText());
                        if ((questionId != -1) && (!adapter.isExistContextQuestion(contextId, questionId))) {
                            adapter.insertContextQuestion(contextId, questionId);
                            for (String word : morphologicalQuestion.getAnalyses()) {
                                int wordId = adapter.insertWord(word);
                                if ((wordId != -1) && (!adapter.isExistQuestionWord(questionId, wordId))) {
                                    adapter.insertQuestionWord(questionId, wordId);
                                } else {
                                    System.out.println("word cannot insert or question-word pair already exist");
                                }
                            }
                        } else {
                            System.out.println("question cannot insert or context-question pair already exist");
                        }
                    }
                } else {
                    System.out.println("Context cannot added = " + context);
                }
            }
        }
    }
}