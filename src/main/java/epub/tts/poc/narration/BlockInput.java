package epub.tts.poc.narration;

import java.util.LinkedList;

public class BlockInput extends LinkedList<TextInput> {
	
	private String id;
	
	public BlockInput(String id) {
		this.id = id;
	}
	
	@Override
	public boolean add(TextInput newInput) {
		TextInput lastInput = peekLast();
		if (lastInput == null ||
				lastInput.getLanguage() != newInput.getLanguage())
			return super.add(newInput);
		// Combine inline runs of text if they have the same language
		lastInput.mergeInput(newInput);
		return true;
	}
	
	public String getId() {
		return id;
	}

}
