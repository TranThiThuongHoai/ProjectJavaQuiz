package fr.epita.core.launcher;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level; 
import java.util.logging.Logger; 

import fr.epita.core.datamodel.Answer;
import fr.epita.core.datamodel.Difficulty;
import fr.epita.core.datamodel.MCQAnswer;
import fr.epita.core.datamodel.Question;
import fr.epita.core.datamodel.QuestionType;
import fr.epita.core.datamodel.Quiz;
import fr.epita.core.datamodel.Student;
import fr.epita.core.services.QuizServices;
import fr.epita.core.services.data.DatabaseDAO;

public class Launcher {
    private static Scanner userScanner;
    private static final Logger LOGGER =  
                Logger.getLogger(Logger.GLOBAL_LOGGER_NAME); 
    public static void main(String[] args) {
        userScanner = new Scanner(System.in);
        String userInput;
        Student student = new Student();
        Difficulty difficulty = null;
        Quiz quiz = new Quiz();
        boolean isConnected = false;
        Connection conn = null;
        ArrayList<Answer> userAnswerList = new ArrayList<>(5);
        Integer mark;
        // Connect to the database before starting
        try {
            conn = DatabaseDAO.connectDatabase();
            isConnected = true;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE,e.getMessage(), e);
            isConnected = false;
        }
        // If the connection to the database is good, begin the quiz
        if (isConnected) {
            // Main menu
            clearConsole();
            System.out.println("1. Start\n2. Quit");
            userInput = userScanner.nextLine();
            // The quiz starts here
            if (userInput.equals("1")) {
                // Get student info
                clearConsole();
                inputStudentInfo(student);
                // get the quiz type (open/mcq)
                QuestionType questionType = inputQuestionType();
                quiz.setQuestionType(questionType);
                // Choose topics
                String topic = chooseTopic(conn, questionType);

                // Get the chosen difficulty from user, I think it's better when you white(true) but then it can't work so I used only If else
                clearConsole();
                userScanner = new Scanner(System.in);
                System.out.println("Which level of difficulty you want to choose?\n\n1. EASY\n2. MEDIUM\n3. HARD");
                if (userInput.contentEquals("1")) {
                	difficulty = Difficulty.EASY;
                }
                else if (userInput.contentEquals("2")) {
                	difficulty = Difficulty.MEDIUM;
                }
                else if (userInput.contentEquals("3")) {
                	difficulty = Difficulty.HARD;
                }
                else {System.out.println("Invalid. Please choose again 1, 2 or 3");
                }
                
                // to test, it's necessary but I don't know how to do :p

                // ====================================================
                // Assemble questions from the database,
                // based on the chosen question type, difficulty and topic
                // (1 topic to simplify the problem, we can develop this feature later)
                // ====================================================
                makeQuiz(quiz, conn, questionType, topic, difficulty);

                // ====================================================
                // Show questions and get answers from user.
                // If MCQ question, need a A/B/C/D validator to check if the user input is valid
                // ("A", "B", "C" or "D")
                // ====================================================
                showQuizGetAnswer(quiz, questionType, userAnswerList);

                // ====================================================
                // Evaluation step:just show the question, user's answers, correct answers
                // If MCQ Question, need a method to evaluate, give mark
                // ====================================================
                mark = evaluationQuiz(student, questionType, userAnswerList);
                if(questionType == QuestionType.MCQQUESTION){
                    System.out.println("\n\nYour mark: " + mark.toString());
                }

                // Ask if user want to export quiz to file txt/pdf
                exportQuiz(student, quiz);

            }
            System.out.println("\n\nGoodbye!" + "\nPress ENTER to exit.");
            userScanner.nextLine();
        }
        // Close the connection of database in any case
        DatabaseDAO.closeDatabase(conn);
    }

    /**
     * Export the quiz in plain text, a file name will be asked.
     * If the file is exported with success,  the file path wil be shown.
     * @param student, Student object, containing student name and id
     * @param quiz, Quiz object, containing the list of questions and relevant informations
     */
    private static void exportQuiz(Student student, Quiz quiz) {
        String userInput;
        System.out.println("\n\nDo you want export this quiz? (Y/N)");
        userInput = userScanner.nextLine();
        if (userInput.equalsIgnoreCase("y")) {
            System.out.print("\n\nType in your file name: ");
            String path = userScanner.nextLine();
            try {
                QuizServices.exportQuiz(path + ".txt", quiz, student);
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, e.toString(), e);
                System.out.println("Failed to export the text file.");
            }
        }
    }

    /**
     * If MCQ questions, the user's answers will be evaluated 
     * by comparing the user's answers with the correct answers in the database.
     * In both cases (Open question and MCQ Question), 
     * the correct answers and user's answers will be shown together.
     * @param student, Student object
     * @param questionType, enum QuestiontType
     * @param userAnswerList, List<Answer> of user's answers
     * @return 0 if Open Question, if MCQ question, Integer mark is returned.
     */
    private static Integer evaluationQuiz(Student student, QuestionType questionType,
            List<Answer> userAnswerList) {
        Integer mark = 0;
        Question question;
        Answer answer;
        System.out.println("\n\nBravo student: " + student.getName() + ", ID: " + student.getID()
                    + "\nYou have finished the quiz." + "\nPress ENTER to check the correct answer.");
        userScanner.nextLine();
            
        for (int i = 0; i < userAnswerList.size(); i++) {
            answer = userAnswerList.get(i);
            question = answer.getQuestion();
            System.out.println("\n\nQuestion " + Integer.toString(i + 1) + "\n"
                    + question.getQuestion() + "\nYour answer: " + answer.getText()
                    + "\nCorrect answer: " + question.getCorrectAnswer()
                    + "\n\n--> Press ENTER to check the next question");
            userScanner.nextLine();
            // Evaluation
            if ((questionType == QuestionType.MCQQUESTION)
                && (question.getCorrectAnswer().equalsIgnoreCase(answer.getText()))) {
                mark++;
            }
        }
        return mark;
    }

    /**
     * Show the questions contained in the quiz object,
     * then ask for user's answer and add it to a List<Answer>
     * @param quiz, Quiz object
     * @param questionType, enum QuestionType
     * @param userAnswerList, List<Answer> 
     */
    private static void showQuizGetAnswer(Quiz quiz, QuestionType questionType, ArrayList<Answer> userAnswerList) {
        String userInput;
        Answer userAnswer = null;
        for (int i = 0; i < quiz.getQuestionList().size(); i++) {
            System.out.println("\n\nQuestion " + Integer.toString(i + 1) + "\n"
                    + quiz.getQuestionList().get(i).getQuestion() + "\nType your answer:");
            userInput = userScanner.nextLine();

            if (questionType == QuestionType.OPENQUESTION) {
                userAnswer = new Answer(userInput, quiz.getQuestionList().get(i));
            } else { // MQC Quiz
                // FIX-ME : put a ABCD validator here
                if (userInput.equalsIgnoreCase("A")
                    || userInput.equalsIgnoreCase("B")
                    || userInput.equalsIgnoreCase("C")
                    || userInput.equalsIgnoreCase("D")) {
                    userAnswer = new MCQAnswer(userInput, quiz.getQuestionList().get(i));
                } else{
                    System.out.println("Invalid answer.");
                    userAnswer = new MCQAnswer("INVALID", quiz.getQuestionList().get(i));
                }
            }
            userAnswerList.add(userAnswer);
        }
    }

    /**
     * Make a quiz by using DatabaseDAO.assembleQuestion,
     * a title is generated automatically based on the quiz information.
     * @param quiz, Quiz object to be given a title
     * @param conn, Connection object to the database
     * @param questionType, enum QuestionType, either OPENQUESTION or MCQQUESTION
     * @param topic, String, get available topics by DatabaseDAO.getTopics
     * @param difficulty, enum Difficulty.EASY or MEDIUM or HARD
     */
    private static void makeQuiz(Quiz quiz, Connection conn, QuestionType questionType, String topic,
            Difficulty difficulty) {
        List<Question> questionList = DatabaseDAO.assembleQuestion(conn, questionType, topic, difficulty);
        quiz.setQuestionList(questionList);
        // Make the title of the quiz
        StringBuilder titleBuilder = new StringBuilder();
        titleBuilder.append(topic);
        titleBuilder.append(" Quiz, ");
        if (questionType == QuestionType.OPENQUESTION) {
            titleBuilder.append("Open Questions");
        } else {
            titleBuilder.append("MCQ Questions");
        }
        quiz.setTitle(titleBuilder.toString());
    }


    /**
     * Show the unique available topics in the database
     * by using DatabaseDAO.getTopics
     * then ask for user choice of topic
     * @param conn, Connection object
     * @param questionType
     * @return
     */
    private static String chooseTopic(Connection conn, QuestionType questionType) {
        String topic;
        int intChoice = 0;
        boolean isInputMismatch = false;
        System.out.println("\n\n");
        // Show the list of available topics
        List<String> topicsList = DatabaseDAO.getTopics(conn, questionType.getQuestionType());
        for (int i = 0; i < topicsList.size(); i++) {
            System.out.println(Integer.toString(i + 1) + ". " + topicsList.get(i));
        }
        // Get the choice of user
        while ((intChoice <= 0) || (intChoice) > topicsList.size() || isInputMismatch) {
            try {
                intChoice = userScanner.nextInt();
                isInputMismatch = false;
                if ((intChoice <= 0) || (intChoice) > topicsList.size()) {
                    System.out.print("Invalid choice, please choose again:");
                }
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, e.toString(), e);
                System.out.print("Invalid choice, please choose again:");
                isInputMismatch = true;
            } finally {
                userScanner.nextLine(); // Consume newline left-over so that we can use read nextline in the next time.
            }
        }
        topic = topicsList.get(intChoice - 1);
        System.out.println("You chose " + topic);
        return topic;
    }

    /**
     * Ask for user choice of question type, OPEN QUESTION or MCQ QUESTION
     * @return either QuestionType.OPENQUESTION or QuestionType.MCQQUESTION
     */
    private static QuestionType inputQuestionType() {
        String userInput;
        System.out.println("\n\n");
        while (true) {
            System.out.println("Choose the quiz type: \n1. Open question\n2. MCQ questions");
            userInput = userScanner.nextLine();
            if (userInput.equals("1")) {
                System.out.println("You chose Open Questions.");
                return QuestionType.OPENQUESTION;
            } else if (userInput.equals("2")) {
                System.out.println("You chose MCQ Questions.");
                return QuestionType.MCQQUESTION;
            } else
                System.out.println("Invalid input. Please choose again.");
        }
    }

    /**
     * Ask for user input of student name and id
     * @param student, Student object that will contains the info after having get the user input
     */
    private static void inputStudentInfo(Student student) {
        System.out.println("\n\n");
        System.out.print("Enter student name: ");
        String studentName = userScanner.nextLine();
        System.out.print("Enter student id: ");
        String studentID = userScanner.nextLine();
        student.setName(studentName);
        student.setID(studentID);
        System.out.println(student);
    }

    /**
     * Clear the console screen
     */
    public static void clearConsole() {
        for (int clear = 0; clear < 100; clear++) {
            System.out.println("\n");
        }
    }
}