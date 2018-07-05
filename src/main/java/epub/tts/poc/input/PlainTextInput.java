package epub.tts.poc.input;

import epub.tts.poc.narration.Language;
import net.sf.saxon.s9api.Axis;
import net.sf.saxon.s9api.XdmNode;
import net.sf.saxon.s9api.XdmNodeKind;

public class PlainTextInput extends TextInput {
	
	public PlainTextInput(String text, Language language) {
		super(language);
		input = input.toBuilder()
				.setText(text)
				.build();
	}
	
	private void addText(String text) {
		input = input.toBuilder()
				.setText(input.getText() + text)
				.build();
	}
	
	public String getText() {
		return input.getText();
	}

	@Override
	public void mergeInput(PlainTextInput other) {
		addText(other.getInput().getText());
	}

	@Override
	public void mergeInput(SsmlTextInput input) {
		StringBuilder stringBuilder = new StringBuilder(" ");
		XdmNode ssmlNode = input.getSsmlNode();
		ssmlNode.axisIterator(Axis.DESCENDANT).forEachRemaining(
				item -> {
					XdmNodeKind kind = ((XdmNode)item).getNodeKind();
					if (kind == XdmNodeKind.TEXT)
						stringBuilder.append(item.getStringValue());
				});
		addText(stringBuilder.toString());
	}

}
