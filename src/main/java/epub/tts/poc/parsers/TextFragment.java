package epub.tts.poc.parsers;

import epub.tts.poc.narration.Language;

public class TextFragment {
	
	private Language language;
	private String text;
	
	public TextFragment(Language language, String text) {
		this.language = language;
		this.text = text;
	}
	
	public Language getLanguage() {
		return language;
	}
	
	public String getText() {
		return text;
	}

}
