package fr.epita.core.datamodel;

/**
 * Define the question type OPENQUESTION and MCQQUESTION
 * These values in String are also the table name in the database.
 */
public enum QuestionType{
    OPENQUESTION("OPENQUESTION"),
    MCQQUESTION("MCQQUESTION");

    private String typeString;

    private QuestionType(String qType){
        this.typeString = qType;
    }

    public String getQuestionType(){
        return this.typeString;
    }
}