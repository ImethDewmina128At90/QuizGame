package db;
import java.sql.*;
import java.util.*;

public class JDBC {

    // Load database configuration from .env file
    private static final String DB_URL = EnvLoader.get("DB_URL", "jdbc:mysql://127.0.0.1:3306/quiz_gui_db");
    private static final String DB_USERNAME = EnvLoader.get("DB_USERNAME", "root");
    private static final String DB_PASSWORD = EnvLoader.get("DB_PASSWORD", "");

    // Initialize database tables if they don't exist
    public static void initializeDatabase() {
        try {
            // Extract base URL without database name for initial connection
            String baseUrl = DB_URL.substring(0, DB_URL.lastIndexOf('/') + 1);
            Connection connection = DriverManager.getConnection(baseUrl, DB_USERNAME, DB_PASSWORD);
            Statement stmt = connection.createStatement();

            // Create database if not exists
            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS quiz_gui_db");
            stmt.executeUpdate("USE quiz_gui_db");

            // Create category table (only if it doesn't exist)
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS category (" +
                    "category_id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "category_name VARCHAR(255) NOT NULL UNIQUE)");

            // Create question table (only if it doesn't exist)
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS question (" +
                    "question_id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "category_id INT NOT NULL, " +
                    "question_text TEXT NOT NULL, " +
                    "FOREIGN KEY (category_id) REFERENCES category(category_id))");

            // Create answer table (only if it doesn't exist)
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS answer (" +
                    "answer_id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "question_id INT NOT NULL, " +
                    "answer_text VARCHAR(255) NOT NULL, " +
                    "is_correct BOOLEAN NOT NULL DEFAULT FALSE, " +
                    "FOREIGN KEY (question_id) REFERENCES question(question_id))");

            connection.close();
            System.out.println("Database initialized successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean insertQuestionToDB(String questionText, String categoryName, String[] answers, int correctIndex) {
        try {
            // Insert category if it is new
            category categoryObj = getcategory(categoryName);
            if (categoryObj == null) {
                categoryObj = insertCategory(categoryName);
            }

            // Check if category was created successfully
            if (categoryObj == null) {
                System.out.println("Failed to create/get category");
                return false;
            }

            // Insert question
            question questionObj = insertQuestion(categoryObj, questionText);

            // Check if question was created successfully
            if (questionObj == null) {
                System.out.println("Failed to create question");
                return false;
            }

            insertAnswers(questionObj, answers, correctIndex);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private static category getcategory(String categoryName) {
        try {
            Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
            String query = "SELECT * FROM category WHERE category_name = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, categoryName);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int categoryID = resultSet.getInt("category_id");
                connection.close();
                return new category(categoryName, categoryID);
            }
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ArrayList<String> getCategories() {
        ArrayList<String> categories = new ArrayList<>();
        try {
            Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
            Statement getCategoriesQuery = connection.createStatement();
            ResultSet resultSet = getCategoriesQuery.executeQuery("SELECT * FROM category");
            while (resultSet.next()) {
                String categoryName = resultSet.getString("category_name");
                categories.add(categoryName);
            }
            connection.close();
            return categories;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return categories;
    }

    public static category getCategoryByName(String categoryName) {
        try {
            Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
            String query = "SELECT * FROM category WHERE category_name = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, categoryName);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int categoryID = resultSet.getInt("category_id");
                connection.close();
                return new category(categoryName, categoryID);
            }
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static question insertQuestion(category categoryObj, String questionText) {
        try {
            Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
            String query = "INSERT INTO question (category_id, question_text) VALUES (?, ?)";
            PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, categoryObj.getCategoryID());
            statement.setString(2, questionText);
            statement.executeUpdate();

            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                int questionID = resultSet.getInt(1);
                connection.close();
                return new question(questionText, questionID, categoryObj.getCategoryID());
            }
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static category insertCategory(String categoryName) {
        try {
            Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
            String query = "INSERT INTO category (category_name) VALUES (?)";
            PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, categoryName);
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Inserting category failed, no rows affected.");
            }
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                int categoryID = generatedKeys.getInt(1);
                connection.close();
                return new category(categoryName, categoryID);
            }
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void insertAnswers(question questionObj, String[] answers, int correctIndex) {
        try {
            Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
            String query = "INSERT INTO answer (question_id, answer_text, is_correct) VALUES (?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);

            for (int i = 0; i < answers.length; i++) {
                statement.setInt(1, questionObj.getQuestionID());
                statement.setString(2, answers[i]);
                statement.setBoolean(3, i == correctIndex);
                statement.addBatch();
            }
            statement.executeBatch();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<answer> getAnswers(question questionObj) {
        ArrayList<answer> answers = new ArrayList<>();
        try {
            Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
            PreparedStatement getAnswersQuery = connection.prepareStatement(
                    "SELECT * FROM answer WHERE question_id = ? ORDER BY RAND()");
            getAnswersQuery.setInt(1, questionObj.getQuestionID());

            ResultSet resultSet = getAnswersQuery.executeQuery();
            while (resultSet.next()) {
                int answerID = resultSet.getInt("answer_id");
                String answerText = resultSet.getString("answer_text");
                boolean isCorrect = resultSet.getBoolean("is_correct");
                answer answerObj = new answer(answerID, questionObj.getQuestionID(), answerText, isCorrect);
                answers.add(answerObj);
            }
            connection.close();
            return answers;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return answers;
    }

    public static ArrayList<question> getQuestionsByCategory(category categoryObj) {
        ArrayList<question> questions = new ArrayList<>();
        try {
            Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
            PreparedStatement getQuestionsQuery = connection.prepareStatement(
                    "SELECT * FROM question WHERE category_id = ? ORDER BY RAND()");
            getQuestionsQuery.setInt(1, categoryObj.getCategoryID());

            ResultSet resultSet = getQuestionsQuery.executeQuery();
            while (resultSet.next()) {
                int questionID = resultSet.getInt("question_id");
                String questionText = resultSet.getString("question_text");
                int categoryID = resultSet.getInt("category_id");
                question questionObj = new question(questionText, questionID, categoryID);
                questions.add(questionObj);
            }
            connection.close();
            return questions;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return questions;
    }
}
