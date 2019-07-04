package fr.epita.core.services;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import fr.epita.core.datamodel.Question;
import fr.epita.core.datamodel.Quiz;
import fr.epita.core.datamodel.Student;

public class QuizServices {


    private QuizServices() {
    }


    /**
     * Export quiz to a text file.
     * The student name, id and questions will be stored in the file.
     * The file will be stored in the directory ./quiz_export/
     * 
     * @param path, String of a file path quiz, Quiz object contains questions
     * @param quiz, Quiz object
     * @param student, Student object
     * @return result, boolean True if success
     * @throws IOException, can be thrown by FileWriter
     */
    public static Boolean exportQuiz(String path, Quiz quiz, Student student) throws IOException {
        Boolean result = false;
        FileWriter fileWriter = new FileWriter("./quiz_export/" + path);
        PrintWriter printWriter = new PrintWriter(fileWriter);
        printWriter.println(quiz.getTitle());
        printWriter.println();
        printWriter.printf("Student name: %s\t ID: %s", student.getName(), student.getID());
        printWriter.println();
        List<Question> questionList = quiz.getQuestionList();
        for (int i = 0; i < quiz.getQuestionList().size(); i++) {
            printWriter.println();
            printWriter.println();
            printWriter.println("Question " + Integer.toString(i+1));
            printWriter.println(questionList.get(i).getQuestion() + "\n\n");
        }
        printWriter.close();
        System.out.println("File exported to " + "./quiz_export/" + path);
        return result;
    }
}