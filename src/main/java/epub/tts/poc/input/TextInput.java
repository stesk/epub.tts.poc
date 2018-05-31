package epub.tts.poc.input;

import com.google.cloud.texttospeech.v1beta1.SynthesisInput;

import epub.tts.poc.narration.Language;

public abstract class TextInput {
	
	SynthesisInput input;
	Language language;
	
	public TextInput(Language language) {
		this.language = language;
	}
	
	public SynthesisInput getInput() {
		return input;
	}
	
	public Language getLanguage() {
		return language;
	}

}
