package epub.tts.poc.parsers;

import java.util.LinkedList;

import net.sf.saxon.s9api.XdmNode;

public class TextDivision extends LinkedList<TextBlock> {
	
	private XdmNode contentNode;
	
	public TextDivision(XdmNode contentNode) {
		this.contentNode = contentNode;
	}

}
