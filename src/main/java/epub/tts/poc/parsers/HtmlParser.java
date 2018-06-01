package epub.tts.poc.parsers;

import epub.tts.poc.input.BlockInput;
import epub.tts.poc.input.DivisionInput;
import epub.tts.poc.input.PlainTextInput;
import epub.tts.poc.input.TitleInput;
import epub.tts.poc.narration.Language;
import epub.tts.poc.xml.XmlUtilities;
import net.sf.saxon.s9api.QName;
import net.sf.saxon.s9api.SaxonApiException;
import net.sf.saxon.s9api.XPathSelector;
import net.sf.saxon.s9api.XdmItem;
import net.sf.saxon.s9api.XdmNode;
import net.sf.saxon.s9api.XdmSequenceIterator;

public class HtmlParser {
	
	private boolean ssmlEnabled = false;
	
	public static final String[] HTML_TEXT_BLOCK_ELEMENTS = new String[] {
			"html:dd",
			"html:dt",
			"html:figcaption",
			"html:h1",
			"html:h2",
			"html:h3",
			"html:h4",
			"html:h5",
			"html:h6",
			"html:li",
			"html:p",
			"html:table"
	};
	
	public HtmlParser(boolean ssmlEnabled) {
		this.ssmlEnabled = ssmlEnabled;
	}
	
	public TitleInput parse(XdmNode document)
			throws SaxonApiException {
		XdmItem pidValue = XmlUtilities.evaluateXpathOnNode(
				"/dtbook/head/meta[@name eq 'dc:identifier']/@content",
				document);
		TitleInput title = new TitleInput(pidValue == null ? "dk-nota-000000"
				: pidValue.getStringValue());
		XdmSequenceIterator divisionIterator = XmlUtilities.iterateXpathOnNode(
				"/html:html/html:body/html:section", document);
		while (divisionIterator.hasNext())
			title.add(parseDivision((XdmNode)divisionIterator.next()));
		return title;
	}
	
	private DivisionInput parseDivision(XdmNode divisionNode)
			throws SaxonApiException {
		XdmItem heading = XmlUtilities.evaluateXpathOnNode(
				"html:h1|html:h2|html:h3|html:h4|html:h5|html:6",
				divisionNode);
		String title = heading == null? "***" : heading.getStringValue();
		DivisionInput division = new DivisionInput(title);
		// We want the innermost block elements, e.g. block elements with no
		// other block elements as their children
		// TODO: Test this to make sure it works as intended
		XdmSequenceIterator blockIterator = XmlUtilities.iterateXpathOnNode(
				".//(" + String.join("|", HTML_TEXT_BLOCK_ELEMENTS) + ")",
				divisionNode);
		while (blockIterator.hasNext())
			division.add(parseBlock((XdmNode)blockIterator.next()));
		return division;
	}

	private BlockInput parseBlock(XdmNode blockNode) throws SaxonApiException {
		BlockInput block = new BlockInput(blockNode.getAttributeValue(
				new QName("id")));
		XPathSelector langSelector = XmlUtilities.getXpathSelector(
				"ancestor::*[@lang][1]/@lang");
		XdmSequenceIterator textIterator = XmlUtilities.iterateXpathOnNode(
				"descendant::text()", blockNode);
		while (textIterator.hasNext()) {
			XdmNode textNode = (XdmNode)textIterator.next();
			langSelector.setContextItem(textNode);
			String langValue = langSelector.evaluateSingle()
					.getStringValue();
			if (ssmlEnabled) {
				
			} else block.add(new PlainTextInput(textNode.getStringValue()
					.replaceAll("\\s+", " "), Language.getLanguage(langValue)));
		}
		return block;
	}

}
