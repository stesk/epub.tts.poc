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
	
	public void mergeInput(TextInput input) {
		if (input instanceof PlainTextInput) mergeInput((PlainTextInput)input);
		else if (input instanceof SsmlTextInput)
			mergeInput((SsmlTextInput)input);
	}
	
	public abstract void mergeInput(PlainTextInput input);
	
	public abstract void mergeInput(SsmlTextInput input);

}
