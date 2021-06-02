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
        int totalCountRootSuffix = 0, trueCountRootSuffix = 0, falseCountRootSuffix = 0;
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
                    String estimatedContextBySuffix = adapter.getMostSimilarContextRootAndSuffix(wordRoots, wordSuffixes);
                    String estimatedContextByRootAndSuffix = adapter.getMostSimilarContextRootSuffixPair(wordRoots, wordSuffixes);
                    if (estimatedContextByRoot != null) {
                        totalCountRoot++;
                        if (actualContext.equals(estimatedContextByRoot)) {
                            trueCountRoot++;
                        } else {
                            falseCountRoot++;
                        }
                    }
                    if (estimatedContextBySuffix != null) {
                        totalCountSuffix++;
                        if (actualContext.equals(estimatedContextBySuffix)) {
                            trueCountSuffix++;
                        } else {
                            falseCountSuffix++;
                        }
                    }
                    if (estimatedContextByRootAndSuffix != null) {
                        totalCountRootSuffix++;
                        if (actualContext.equals(estimatedContextByRootAndSuffix)) {
                            trueCountRootSuffix++;
                        } else {
                            falseCountRootSuffix++;
                        }
                    }
                    System.out.println("Accuracy root" + (float) (trueCountRoot) / totalCountRoot + "\tAccuracy suffix" + (float) (trueCountSuffix) / totalCountSuffix + "\tAccuracy root and suffix" + (float) (trueCountRootSuffix) / totalCountRootSuffix);
                }
            }
        }

        System.out.println("True Count By Root = " + trueCountRoot);
        System.out.println("False Count By Root = " + falseCountRoot);
        System.out.println("Total Count By Root = " + totalCountRoot);
        System.out.println("Accuracy Rate By Root = " + (float) (trueCountRoot) / totalCountRoot);
        System.out.println("Error Rate By Root = " + (float) (falseCountRoot) / totalCountRoot);

        System.out.println("True Count By Suffix = " + trueCountSuffix);
        System.out.println("False Count By Suffix = " + falseCountSuffix);
        System.out.println("Total Count By Suffix = " + totalCountSuffix);
        System.out.println("Accuracy Rate By Suffix = " + (float) (trueCountSuffix) / totalCountSuffix);
        System.out.println("Error Rate By Suffix = " + (float) (falseCountSuffix) / totalCountSuffix);

        System.out.println("True Count By Root and Suffix = " + trueCountRootSuffix);
        System.out.println("False Count By Root and Suffix = " + falseCountRootSuffix);
        System.out.println("Total Count By Root and Suffix = " + totalCountRootSuffix);
        System.out.println("Accuracy Rate By Root and Suffix = " + (float) (trueCountRootSuffix) / totalCountRootSuffix);
        System.out.println("Error Rate By Root and Suffix = " + (float) (falseCountRootSuffix) / totalCountRootSuffix);
    }
}