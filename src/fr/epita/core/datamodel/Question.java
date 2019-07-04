package fr.epita.core.datamodel;

import java.util.Objects;

/**
 * Contain the fields that are stored in the database:
 * id, question's content, topic, difficulty and the correct answer
 */
public class Question {
	private Integer id;
	private String  questionText;
	private String topic;
	private Integer difficulty;
	private String correctAnswer;

	public Question(Integer id, String questionText, String topic, Integer difficulty, String correctAnswer) {
		this.id = id;
		this.questionText = questionText;
		this.topic = topic;
		this.difficulty = difficulty;
		this.correctAnswer = correctAnswer;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getQuestion() {
		return this.questionText;
	}

	public void setQuestion(String questionText) {
		this.questionText = questionText;
	}

	public String getTopic() {
		return this.topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public Integer getDifficulty() {
		return this.difficulty;
	}

	public void setDifficulty(Integer difficulty) {
		this.difficulty = difficulty;
	}

	public String getCorrectAnswer() {
		return this.correctAnswer;
	}

	public void setCorrectAnswer(String correctAnswer) {
		this.correctAnswer = correctAnswer;
	}


	@Override
	public boolean equals(Object o) {
		if (o == this)
			return true;
		if (!(o instanceof Question)) {
			return false;
		}
		Question question = (Question) o;
		return Objects.equals(id, question.id) && Objects.equals(questionText, question.questionText) && Objects.equals(topic, question.topic) && Objects.equals(difficulty, question.difficulty) && Objects.equals(correctAnswer, question.correctAnswer);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, questionText, topic, difficulty, correctAnswer);
	}


	@Override
	public String toString() {
		return "{" +
			" id='" + getId() + "'" +
			", questionText='" + getQuestion() + "'" +
			", topic='" + getTopic() + "'" +
			", difficulty='" + getDifficulty() + "'" +
			", correctAnswer='" + getCorrectAnswer() + "'" +
			"}";
	}

	public Question() {
	}

}
