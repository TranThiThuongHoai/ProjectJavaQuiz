package fr.epita.core.datamodel;

/**
 * Define the difficulty of the question, 0, 1 and 2,
 * corresponding from EASY, MEDIUM and HARD
 */
public enum Difficulty {
	EASY(0),
	MEDIUM(1),
	HARD(2),;
	
	private Integer numericDifficulty;
	
	private Difficulty(Integer difficulty) {
		this.numericDifficulty = difficulty;
	}
	
	public Integer getDifficulty() {
		return this.numericDifficulty;
	}
}