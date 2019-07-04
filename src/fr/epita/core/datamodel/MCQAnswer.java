package fr.epita.core.datamodel;

import fr.epita.core.datamodel.Answer;
import fr.epita.core.datamodel.Question;

/**
 * Extended from Answer class
 */
public class MCQAnswer extends Answer {

    public MCQAnswer(String answerText, Question question){
        super.question = question;
        super.text = answerText;
    }

    public MCQAnswer() {
    }

}