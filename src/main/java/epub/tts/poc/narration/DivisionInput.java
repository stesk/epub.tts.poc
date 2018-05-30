package epub.tts.poc.narration;

import java.util.LinkedList;

import net.sf.saxon.s9api.XdmNode;

public class DivisionInput extends LinkedList<BlockInput> {
	
	private XdmNode content;
	
	public DivisionInput(XdmNode contentNode) {
		this.content = contentNode;
	}
	
	public XdmNode getContent() {
		return content;
	}

}
