package edu.ozu;

import com.google.gson.Gson;
import edu.ozu.model.MorphologicalQuestion;
import edu.ozu.model.Title;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Hello world!
 */
public class App {


    public static void main(String[] args) {
        JSONObject trainData = new JSONObject(Util.readFileFromResources(true));
        JSONArray data = new JSONArray(trainData.get("data").toString());
        ArrayList<Title> titles = new ArrayList<>();
        ArrayList<MorphologicalQuestion> questions = new ArrayList<>();
        for (Object title : data) {
            titles.add(new Gson().fromJson(String.valueOf(title), Title.class));
        }
        for (Title title : titles) {
            for (String question : title.getQuestion()) {
                MorphologicalQuestion morphologicalQuestion = new MorphologicalQuestion(question, Util.getQuestionAnalysis(question));
                questions.add(morphologicalQuestion);
                System.out.println(morphologicalQuestion);
            }
        }
    }
}