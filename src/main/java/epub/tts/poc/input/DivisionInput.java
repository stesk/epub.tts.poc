package epub.tts.poc.input;

import java.util.LinkedList;

public class DivisionInput extends LinkedList<BlockInput> {
	
	private String title;
	
	public DivisionInput(String title) {
		this.title = title;
	}
	
	public String getTitle() {
		return title;
	}

}
