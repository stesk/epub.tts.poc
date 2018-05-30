package epub.tts.poc.narration;

import com.google.cloud.texttospeech.v1beta1.SynthesisInput;

public class TextInput {
	
	private SynthesisInput input;
	private Language language;
	
	public TextInput(String text, Language language) {
		input = SynthesisInput.newBuilder()
				.setText(text)
				.build();
		this.language = language;
	}
	
	public SynthesisInput getInput() {
		return input;
	}
	
	public Language getLanguage() {
		return language;
	}
	
	public void mergeInput(TextInput other) {
		input = input.toBuilder()
				.setText(input.getText() + other.getInput().getText())
				.build();
	}

}
