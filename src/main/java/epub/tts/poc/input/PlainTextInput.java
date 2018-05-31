package epub.tts.poc.input;

import epub.tts.poc.narration.Language;

public class PlainTextInput extends TextInput {
	
	public PlainTextInput(String text, Language language) {
		super(language);
		input = input.toBuilder()
				.setText(text)
				.build();
	}
	
	public void mergeInput(TextInput other) {
		input = input.toBuilder()
				.setText(input.getText() + other.getInput().getText())
				.build();
	}

}
