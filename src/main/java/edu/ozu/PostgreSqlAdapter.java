package edu.ozu;

import java.sql.*;
import java.util.ArrayList;

public class PostgreSqlAdapter {

    Connection con;

    public PostgreSqlAdapter() {
        con = getConnection();
    }

    private Connection getConnection() {

        String url = "jdbc:postgresql://localhost:5432/fqa";
        String user = "nlp";
        String password = "asd.1234";

        Connection con = null;
        try {
            con = DriverManager.getConnection(url, user, password);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return con;
    }

    public int insertContext(String context) {
        context = context.replace("'", "''");
        ResultSet insertContextResult = executeQuery("INSERT INTO fqa.context(context) VALUES ('" + context + "') RETURNING id;");
        try {
            if (insertContextResult != null) {
                return insertContextResult.getInt("id");
            } else {
                return -1;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return -1;
    }

    public int insertQuestion(String question, String answer) {
        question = question.replace("'", "''");
        answer = answer.replace("'", "''");
        ResultSet insertQuestionResult = executeQuery("INSERT INTO fqa.questions(question,answer) VALUES ('" + question + "','" + answer + "') RETURNING id;");
        try {
            if (insertQuestionResult != null) {
                return insertQuestionResult.getInt("id");
            } else {
                return -1;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return -1;
    }

    public int insertWord(String word) {
        String root = word.substring(0, word.indexOf("+"));
        String suffix = word.substring(word.indexOf("+") + 1);
        root = root.replace("'", "''");
        suffix = suffix.replace("'", "''");
        int id = checkWord(root, suffix);
        if (id == -1) {
            ResultSet insertResult = executeQuery("INSERT INTO fqa.words(root,suffix) VALUES ('" + root + "','" + suffix + "') RETURNING id;");
            try {
                id = insertResult.getInt("id");
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return id;
    }

    public int checkWord(String root, String suffix) {
        ResultSet checkResult = executeQuery("SELECT id FROM fqa.words WHERE root = '" + root + "' AND suffix = '" + suffix + "';");
        int id = -1;
        try {
            if (checkResult != null) {
                id = checkResult.getInt("id");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            id = -1;
        }
        return id;
    }

    public boolean isExistContextQuestion(int contextId, int questionId) {
        ResultSet checkResult = executeQuery("SELECT * FROM fqa.context_question WHERE context_id = " + contextId + " AND question_id = " + questionId + ";");
        return checkResult != null;
    }

    public boolean isExistContextWord(int contextId, int wordId) {
        ResultSet checkResult = executeQuery("SELECT * FROM fqa.context_word WHERE context_id = " + contextId + " AND word_id = " + wordId + ";");
        return checkResult != null;
    }

    public boolean isExistQuestionWord(int questionId, int wordId) {
        ResultSet checkResult = executeQuery("SELECT * FROM fqa.question_word WHERE question_id = " + questionId + " AND word_id = " + wordId + ";");
        return checkResult != null;
    }

    public boolean insertContextQuestion(int contextId, int questionId) {
        int checkResult = executeUpdate("INSERT INTO fqa.context_question(context_id, question_id) VALUES (" + contextId + "," + questionId + ");");
        return checkResult == 1;
    }

    public boolean insertContextWord(int contextId, int wordId) {
        int checkResult = executeUpdate("INSERT INTO fqa.context_word(context_id, word_id) VALUES (" + contextId + "," + wordId + ");");
        return checkResult == 1;
    }

    public boolean insertQuestionWord(int questionId, int wordId) {
        int checkResult = executeUpdate("INSERT INTO fqa.question_word(question_id, word_id) VALUES (" + questionId + "," + wordId + ");");
        return checkResult == 1;
    }

    public String getMostSimilarContextByRoot(ArrayList<String> wordRoots) {
        StringBuilder rootCauseString = new StringBuilder();
        for (int i = 0; i < wordRoots.size(); i++) {
            rootCauseString.append("'");
            rootCauseString.append(wordRoots.get(i));
            rootCauseString.append("'");
            if (i != wordRoots.size() - 1) {
                rootCauseString.append(",");
            }
        }
        String query = "SELECT most_similar.word_count, context.* FROM (SELECT context_word.context_id, COUNT(DISTINCT words.root) AS word_count FROM fqa.words LEFT JOIN fqa.context_word ON words.id = context_word.word_id WHERE words.root IN (" + rootCauseString + ") GROUP BY context_word.context_id ORDER BY word_count DESC) as most_similar LEFT JOIN fqa.context ON most_similar.context_id = context.id ORDER BY word_count DESC LIMIT 1;";
        System.out.println( "getMostSimilarContextRoot" + query);
        ResultSet getMostSimilarContextRoot = executeQuery(query);

        try {
            if (getMostSimilarContextRoot != null) {
                return getMostSimilarContextRoot.getString("context");
            } else {
                return "not found";
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    public String getMostSimilarContextRootAndSuffix(ArrayList<String> wordRoots, ArrayList<String> wordSuffixes) {
        StringBuilder rootCauseString = new StringBuilder();
        StringBuilder suffixCauseString = new StringBuilder();
        for (int i = 0; i < wordRoots.size(); i++) {
            rootCauseString.append("'");
            rootCauseString.append(wordRoots.get(i));
            rootCauseString.append("'");
            if (i != wordRoots.size() - 1) {
                rootCauseString.append(",");
            }
        }
        for (int i = 0; i < wordSuffixes.size(); i++) {
            suffixCauseString.append("'");
            suffixCauseString.append(wordSuffixes.get(i));
            suffixCauseString.append("'");
            if (i != wordSuffixes.size() - 1) {
                suffixCauseString.append(",");
            }
        }
        String query = "SELECT most_similar.word_count, context.* FROM (SELECT context_word.context_id, COUNT(DISTINCT words.root) AS word_count FROM fqa.words LEFT JOIN fqa.context_word ON words.id = context_word.word_id WHERE words.root IN (" + rootCauseString + ")  AND words.suffix IN (" + suffixCauseString + ") GROUP BY context_word.context_id ORDER BY word_count DESC) as most_similar LEFT JOIN fqa.context ON most_similar.context_id = context.id ORDER BY word_count DESC LIMIT 1;";
        System.out.println( "getMostSimilarContextRootAndSuffix" + query);
        ResultSet getMostSimilarContextRootAndSuffix = executeQuery(query);

        try {
            if (getMostSimilarContextRootAndSuffix != null) {
                return getMostSimilarContextRootAndSuffix.getString("context");
            } else {
                return "not found";
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    public String getMostSimilarContextRootSuffixPair(ArrayList<String> wordRoots, ArrayList<String> wordSuffixes) {
        int wordSize = wordRoots.size();
        StringBuilder whereCauseString = new StringBuilder();
        for (int i = 0; i < wordSize; i++) {
            whereCauseString.append("(words.root = '");
            whereCauseString.append(wordRoots.get(i));
            whereCauseString.append("' AND words.suffix = '");
            whereCauseString.append(wordSuffixes.get(i));
            whereCauseString.append("')");
            if (i != wordRoots.size() - 1) {
                whereCauseString.append(" OR ");
            }
        }
        String query = "SELECT most_similar.word_count, context.* FROM (SELECT context_word.context_id, COUNT(DISTINCT words.root) AS word_count FROM fqa.words LEFT JOIN fqa.context_word ON words.id = context_word.word_id WHERE " + whereCauseString + " GROUP BY context_word.context_id ORDER BY word_count DESC) as most_similar LEFT JOIN fqa.context ON most_similar.context_id = context.id ORDER BY word_count DESC LIMIT 1;";
        System.out.println( "getMostSimilarContextRootSuffixPair" + query);
        ResultSet getMostSimilarContextRootAndSuffix = executeQuery(query);

        try {
            if (getMostSimilarContextRootAndSuffix != null) {
                return getMostSimilarContextRootAndSuffix.getString("context");
            } else {
                return "not found";
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    public String getMostSimilarQuestion(ArrayList<String> word_roots) {
        StringBuilder rootCauseString = new StringBuilder();
        for (int i = 0; i < word_roots.size(); i++) {
            rootCauseString.append("'");
            rootCauseString.append(word_roots.get(i));
            rootCauseString.append("'");
            if (i != word_roots.size() - 1) {
                rootCauseString.append(",");
            }
        }
        String query = "SELECT most_similar.word_count, questions.* FROM (SELECT question_word.question_id, COUNT(DISTINCT words.root) AS word_count FROM fqa.words LEFT JOIN fqa.question_word ON words.id = question_word.word_id WHERE words.root IN (" + rootCauseString + ") GROUP BY question_word.question_id ORDER BY word_count DESC) as most_similar LEFT JOIN fqa.questions ON most_similar.question_id = questions.id ORDER BY word_count DESC LIMIT 1;";
        ResultSet getMostSimilarQuestionResult = executeQuery(query);

        try {
            if (getMostSimilarQuestionResult != null) {
                return getMostSimilarQuestionResult.getString("answer");
            } else {
                return "not found";
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    public ResultSet executeQuery(String query) {
        Statement st;
        ResultSet rs;
        try {
            st = con.createStatement();
            rs = st.executeQuery(query);
            if (rs != null && rs.next()) {
                return rs;
            } else {
                return null;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }

    public int executeUpdate(String query) {
        Statement st;
        int rs;
        try {
            st = con.createStatement();
            rs = st.executeUpdate(query);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return -1;
        }
        return rs;
    }
}