package epub.tts.poc.input;

import java.util.LinkedList;

public class TitleInput extends LinkedList<DivisionInput> {
	
	private String pid;
	
	public TitleInput(String pid) {
		this.pid = pid;
	}

}
