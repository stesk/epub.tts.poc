package epub.tts.poc.parsers;

import java.util.LinkedList;

public class TextBlock extends LinkedList<TextFragment> {
	
	private String id;
	
	public TextBlock(String id) {
		super();
		this.id = id;
	}
	
	@Override
	public boolean add(TextFragment fragment) {
		TextFragment lastFragment = peekLast();
		if (lastFragment == null ||
				lastFragment.getLanguage() != fragment.getLanguage())
			return super.add(fragment);
		// Combine inline runs of text if they have the same language
		lastFragment.setText(lastFragment.getText() + fragment.getText());
		return true;
	}
	
	public String getId() {
		return id;
	}

}
