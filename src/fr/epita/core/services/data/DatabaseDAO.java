package fr.epita.core.services.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;
import fr.epita.core.services.ConfigurationService;
import fr.epita.core.datamodel.Difficulty;
import fr.epita.core.datamodel.Question;
import fr.epita.core.datamodel.QuestionType;
import fr.epita.core.exception.SQLExceptionHandler;

/**
 * Contains methods related to the database
 */
public class DatabaseDAO {
    private static final Logger LOGGER =  
                Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    private static ConfigurationService conf = new ConfigurationService();
    //test some methods still in development
    public static void main(String[] arg) throws SQLException {
        
        Connection conn = null;
        try {
            conn = connectDatabase();
            List<String> topics = getTopics(conn, "MCQQUESTION");
            System.out.println(topics);

            topics = getTopics(conn, "OPENQUESTION");
            System.out.println(topics);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, e.toString(), e);
        } finally{
            closeDatabase(conn);
        } 
    }

    /**
     * Open the connection to the database
     * @return Connection object
     * @throws SQLException, thrown by getConnection
     */
    public static Connection connectDatabase() throws SQLException {
        Connection conn = null;
        final String USER = conf.getConfigurationValue("db.user");
        final String PASS = conf.getConfigurationValue("db.password");
        final String DB_URL = conf.getConfigurationValue("db.url");

        conn = DriverManager.getConnection(DB_URL, USER, PASS);
        return conn;
    }

    /**
     * Close the connection to the database with the given Connection object conn
     * @param conn Connection object
     */
    public static void closeDatabase(Connection conn){
        if(conn != null)
            try {
                conn.close();
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, e.getMessage(), e);
            }
    }


    /**
     * Get the unique values of topics in database with the connection conn,
     * in the table tableName
     * @param conn, Connection to the database
     * @param tableName, table of questions that we want to extract topics
     * @return  ArrayList of String, unique values of topics
     */
    public static List<String> getTopics(Connection conn, String tableName){
        ArrayList<String> result = new ArrayList<>(5);
        String getTopicsString = "SELECT DISTINCT TOPICS FROM " + tableName;
        PreparedStatement prestmt = null;
        ResultSet rs = null;
        
        try {
            prestmt = conn.prepareStatement(getTopicsString);
            rs = prestmt.executeQuery();
            while(rs.next()){
                String topic = rs.getString("TOPICS");
                result.add(topic);
            }
        } catch (SQLException se) {
            SQLExceptionHandler.printSQLException(se); 
        } finally{
            if(prestmt != null)
                try {
                    prestmt.close();
                } catch (SQLException e) {
                    // Nothing we can do
                    LOGGER.log(Level.SEVERE, e.getMessage(), e);
                }
            if(rs != null)
            {
                try {
                    rs.close();
                } catch (SQLException e) {
                    LOGGER.log(Level.SEVERE, e.getMessage(), e);
                }
            }
        }
        return result;
    }

    /**
     * Assemble questions randomly 
     * based on the given question type, topic and difficulty 
     * @param conn, Connection, to the database
     * @param questionType, enum Questiontype, either OPENQUESTION or MCQQUESTION
     * @param topic, String, use getTopics to show available topics in the database
     * @param difficulty, enum Difficulty, either EASY, MEDIUM or HARD
     * @return List of Question of the assembled questions
     */
    public static List<Question> assembleQuestion(Connection conn, QuestionType questionType, String topic, Difficulty difficulty){
        List<Question> questionList = new ArrayList<>(5);
        String getQuestionString = 
            "SELECT * FROM " + questionType.getQuestionType()
            + " WHERE TOPICS= ? AND DIFFICULTY = ?" 
            + " ORDER BY RAND() LIMIT 5 ";
        PreparedStatement prestmt = null;
        ResultSet rs = null;
        try {
            // get question from database (5 questions) randomly based on topic and difficulty
            prestmt = conn.prepareStatement(getQuestionString);
            prestmt.setString(1, topic);
            prestmt.setInt(2, difficulty.getDifficulty().intValue());
            rs = prestmt.executeQuery();
            // populate the questionList
            while(rs.next()){
                Integer id = rs.getInt("ID");
                String questionText = rs.getString("QUESTION");
                String correctAnswer = rs.getString("ANSWER");
                Question question = new Question(
                    id, questionText, topic, 
                    difficulty.getDifficulty(), correctAnswer);
                questionList.add(question);
            }
        } catch (SQLException e1) {
            LOGGER.log(Level.SEVERE, e1.getMessage(), e1);
            SQLExceptionHandler.printSQLException(e1); 
        } finally{
            if(prestmt != null)
                try {
                    prestmt.close();
                } catch (SQLException e2) {
                    // Nothing we can do
                    LOGGER.log(Level.SEVERE, e2.getMessage(), e2);
                    SQLExceptionHandler.printSQLException(e2); 
                }
            if(rs != null){
                try {
                    rs.close();
                } catch (SQLException e2) {
                    // Nothing we can do
                    LOGGER.log(Level.SEVERE, e2.getMessage(), e2);
                    SQLExceptionHandler.printSQLException(e2); 
                }
            }
        }
        return questionList;
    }
}