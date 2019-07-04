package fr.epita.core.datamodel;

import java.util.Objects;
/**
 * Contains informations about Answer: text and Question object
 */
public class Answer {
    protected String text;
    protected Question question;

	public Answer(String text, Question question) {
		this.text = text;
		this.question = question;
	}

	public String getText() {
		return this.text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Question getQuestion() {
		return this.question;
	}

	public void setQuestion(Question question) {
		this.question = question;
	}

	@Override
	public String toString() {
		return "{" +
			" text='" + getText() + "'" +
			", question='" + getQuestion() + "'" +
			"}";
	}

	@Override
	public boolean equals(Object o) {
		if (o == this)
			return true;
		if (!(o instanceof Answer)) {
			return false;
		}
		Answer answer = (Answer) o;
		return Objects.equals(text, answer.text) && Objects.equals(question, answer.question);
	}

	@Override
	public int hashCode() {
		return Objects.hash(text, question);
	}

	public Answer() {
	}


}