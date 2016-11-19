package bean;

import java.util.ArrayList;

import java.util.List;

public class Document_tone {
	private List<Tone_categories> tone_categories;
	
	Document_tone(){
		
	}
	public void setTone_categories(List<Tone_categories> tone_categories) {
		this.tone_categories = tone_categories;
	}

	public List<Tone_categories> getTone_categories() {
		return this.tone_categories;
	}

}
