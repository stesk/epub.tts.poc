package epub.tts.poc.parsers;

import java.io.File;
import java.util.LinkedList;

import epub.tts.poc.narration.Language;
import net.sf.saxon.s9api.QName;
import net.sf.saxon.s9api.SaxonApiException;
import net.sf.saxon.s9api.XPathSelector;
import net.sf.saxon.s9api.XdmItem;
import net.sf.saxon.s9api.XdmNode;
import net.sf.saxon.s9api.XdmSequenceIterator;

public class HtmlParser extends XmlParser {
	
	public static final String[] TEXT_BLOCK_ELEMENTS = new String[] {
			"html:dd",
			"html:dt",
			"html:figcaption",
			"html:li",
			"html:p",
			"html:table"
	};
	
	public LinkedList<TextDivision> parse(File htmlFile)
			throws SaxonApiException {
		LinkedList<TextDivision> divisions = new LinkedList<TextDivision>();
		XdmNode documentNode = getDocumentNode(htmlFile, true);
		XPathSelector divisionSelector = xpathCompiler.compile(
				"/html:html/html:body/html:section").load();
		divisionSelector.setContextItem(documentNode);
		XdmSequenceIterator divisionIterator = divisionSelector.evaluate()
				.iterator();
		while (divisionIterator.hasNext())
			divisions.add(parseDivision((XdmNode)divisionIterator.next()));
		return divisions;
	}
	
	private TextDivision parseDivision(XdmNode divisionNode)
			throws SaxonApiException {
		TextDivision division = new TextDivision();
		String blockXpath = String.join("|", TEXT_BLOCK_ELEMENTS);
		// We want the innermost block elements, e.g. block elements with no
		// other block elements as their children
		// TODO: Test this to make sure it works as intended
		XPathSelector textBlockSelector = xpathCompiler.compile(
				".//(" + blockXpath + ")[not(" + blockXpath + ")]").load();
		textBlockSelector.setContextItem(divisionNode);
		XdmSequenceIterator blockIterator = textBlockSelector.evaluate()
				.iterator();
		while (blockIterator.hasNext())
			division.add(parseBlock((XdmNode)blockIterator.next()));
		return division;
	}
	
	public static void main(String[] args) {
		HtmlParser htmlParser = new HtmlParser();
		try {
			LinkedList<TextDivision> divisions = htmlParser.parse(new File(
					"files/english-continuous-block.html"));
			for (TextDivision division : divisions) {
				for (TextBlock block : division) {
					System.out.println(block.getId());
					for (TextFragment fragment : block)
						System.out.println(fragment.getLanguage() + ": "
								+ fragment.getText());
				}
			}
		} catch (SaxonApiException e) {
			e.printStackTrace();
		}
	}

}
