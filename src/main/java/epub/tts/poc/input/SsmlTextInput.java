package epub.tts.poc.input;

import epub.tts.poc.narration.Language;

public class SsmlTextInput extends TextInput {
	
	public SsmlTextInput(String ssml, Language language) {
		super(language);
		input = input.toBuilder()
				.setSsml(ssml)
				.build();
	}

}
