package fr.epita.core.datamodel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Quiz{
    private String title;
    private QuestionType questionType;
    private List<Question> questionList = new ArrayList<>(10);
    private Student student;

    public Quiz(String title, QuestionType questionType, List<Question> questionList) {
        this.title = title;
        this.questionType = questionType;
        this.questionList = questionList;
    }

    public Quiz() {
    }


    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public QuestionType getQuestionType() {
        return this.questionType;
    }

    public void setQuestionType(QuestionType questionType) {
        this.questionType = questionType;
    }

    public List<Question> getQuestionList() {
        return this.questionList;
    }

    public void setQuestionList(List<Question> questionList) {
        this.questionList = questionList;
    }


    @Override
    public String toString() {
        return "{" +
            " title='" + getTitle() + "'" +
            ", questionType='" + getQuestionType() + "'" +
            "}";
    }


    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Quiz)) {
            return false;
        }
        Quiz quiz = (Quiz) o;
        return Objects.equals(title, quiz.title) && Objects.equals(questionType, quiz.questionType) && Objects.equals(questionList, quiz.questionList) && Objects.equals(student, quiz.student);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, questionType, questionList, student);
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

}