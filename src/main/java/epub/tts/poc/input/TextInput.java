package epub.tts.poc.input;

import com.google.cloud.texttospeech.v1beta1.SynthesisInput;

import epub.tts.poc.narration.Language;

public abstract class TextInput {
	
	SynthesisInput input;
	Language language;
	
	public TextInput(Language language) {
		input = SynthesisInput.newBuilder().build();
		this.language = language;
	}
	
	public SynthesisInput getInput() {
		return input;
	}
	
	public Language getLanguage() {
		return language;
	}
	
	public abstract void mergeInput(TextInput input);

}
