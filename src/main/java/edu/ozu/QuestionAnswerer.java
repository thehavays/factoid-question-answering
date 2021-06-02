package edu.ozu;

import edu.ozu.model.MorphologicalQuestion;
import edu.ozu.model.Paragraph;
import edu.ozu.model.QuestionAnswer;
import edu.ozu.model.Title;

import java.util.ArrayList;

/**
 * Hello world!
 */
public class QuestionAnswerer {


    public static void main(String[] args) {
        int totalCountRoot = 0, trueCountRoot = 0, falseCountRoot = 0;
        int totalCountSuffix = 0, trueCountSuffix = 0, falseCountSuffix = 0;
        ArrayList<Title> titles = Util.getData(true);
        PostgreSqlAdapter adapter = new PostgreSqlAdapter();
        for (Title title : titles) {
            for (Paragraph paragraph : title.getParagraphs()) {
                String actualContext = paragraph.getContext();
                for (QuestionAnswer questionAnswer : title.getQuestionAnswers()) {
                    MorphologicalQuestion morphologicalQuestion = new MorphologicalQuestion(questionAnswer.getQuestion(), Util.getContentAnalysis(questionAnswer.getQuestion()));
                    ArrayList<String> wordRoots = new ArrayList<>();
                    ArrayList<String> wordSuffixes = new ArrayList<>();
                    for (String word : morphologicalQuestion.getAnalyses()) {
                        wordRoots.add(word.substring(0, word.indexOf("+")));
                        wordSuffixes.add(word.substring(word.indexOf("+") + 1));
                    }
                    String estimatedContextByRoot = adapter.getMostSimilarContextByRoot(wordRoots);
                    String estimatedContextByRootAndSuffix = adapter.getMostSimilarContextRootAndSuffix(wordRoots, wordSuffixes);
                    if (estimatedContextByRoot != null) {
                        totalCountRoot++;
                        if (actualContext.equals(estimatedContextByRoot)) {
                            trueCountRoot++;
                        } else {
                            falseCountRoot++;
                        }
                    }
                    if (estimatedContextByRootAndSuffix != null) {
                        totalCountSuffix++;
                        if (actualContext.equals(estimatedContextByRoot)) {
                            trueCountSuffix++;
                        } else {
                            falseCountSuffix++;
                        }
                    }
                }
            }

            System.out.println("Error root" + (float) (falseCountRoot) / totalCountRoot + "Error suffix" + (float) (falseCountSuffix) / totalCountSuffix);
        }

        System.out.println("True Count By Root = " + trueCountRoot);
        System.out.println("False Count By Root = " + falseCountRoot);
        System.out.println("Total Count By Root = " + totalCountRoot);
        System.out.println("Accuracy Rate By Root = " + (float) (trueCountRoot) / totalCountRoot);
        System.out.println("Error Rate By Root = " + (float) (falseCountRoot) / totalCountRoot);

        System.out.println("True Count By Root and Suffix = " + trueCountSuffix);
        System.out.println("False Count By Root and Suffix = " + falseCountSuffix);
        System.out.println("Total Count By Root and Suffix = " + totalCountSuffix);
        System.out.println("Accuracy Rate By Root and Suffix = " + (float) (trueCountSuffix) / totalCountSuffix);
        System.out.println("Error Rate By Root and Suffix = " + (float) (falseCountSuffix) / totalCountSuffix);
    }
}