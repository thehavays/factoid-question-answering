package edu.ozu;

import Corpus.Sentence;
import MorphologicalAnalysis.FsmMorphologicalAnalyzer;
import MorphologicalAnalysis.FsmParse;
import MorphologicalAnalysis.FsmParseList;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Util {

    private static final String DEV_JSON_FILE = "dev-v0.1.json";
    private static final String TRAIN_JSON_FILE = "train-v0.1.json";

    public static String readFileFromResources(boolean isTrain) {
        URL resource;
        if (isTrain) {
            resource = App.class.getClassLoader().getResource(TRAIN_JSON_FILE);
        } else {
            resource = App.class.getClassLoader().getResource(DEV_JSON_FILE);
        }
        byte[] bytes = new byte[0];
        try {
            assert resource != null;
            bytes = Files.readAllBytes(Paths.get(resource.toURI()));
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
        return new String(bytes);
    }

    public static ArrayList<String> getQuestionAnalysis(String question) {
        ArrayList<String> transitionList = new ArrayList<>();
        FsmMorphologicalAnalyzer fsm = new FsmMorphologicalAnalyzer();
        Sentence sentence = new Sentence(question);
        FsmParseList[] parseLists = fsm.morphologicalAnalysis(sentence);
        for (FsmParseList parseList : parseLists) {
            for (int j = 0; j < parseList.size(); j++) {
                FsmParse parse = parseList.getFsmParse(j);
                transitionList.add(parse.transitionList());
            }
        }
        return transitionList;
    }
}
