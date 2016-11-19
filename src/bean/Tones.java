package bean;

public class Tones {
	private double score;

	private String tone_id;

	private String tone_name;

	Tones() {

	}

	public void setScore(double score) {
		this.score = score;
	}

	public double getScore() {
		return this.score;
	}

	public void setTone_id(String tone_id) {
		this.tone_id = tone_id;
	}

	public String getTone_id() {
		return this.tone_id;
	}

	public void setTone_name(String tone_name) {
		this.tone_name = tone_name;
	}

	public String getTone_name() {
		return this.tone_name;
	}

}