package epub.tts.poc.parsers;

import java.io.File;
import java.util.LinkedList;

import net.sf.saxon.s9api.SaxonApiException;
import net.sf.saxon.s9api.XPathSelector;
import net.sf.saxon.s9api.XdmNode;
import net.sf.saxon.s9api.XdmSequenceIterator;

public class DtbookParser extends XmlParser {
	
	public static final String[] DTBOOK_TEXT_BLOCK_ELEMENTS = new String[] {
			"dd",
			"dt",
			"hd",
			"levelhd",
			"li",
			"p",
			"table"
	};
	
	private TextDivision parseDivision(XdmNode divisionNode)
			throws SaxonApiException {
		TextDivision division = new TextDivision();
		String blockXpath = String.join("|", DTBOOK_TEXT_BLOCK_ELEMENTS);
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
	
	public LinkedList<TextDivision> parse(File dtbookFile)
			throws SaxonApiException {
		LinkedList<TextDivision> divisions = new LinkedList<TextDivision>();
		XdmNode documentNode = getDocumentNode(dtbookFile, true);
		XPathSelector divisionSelector = xpathCompiler.compile(
				"/dtbook/book/(frontmatter|bodymatter|rearmatter)/level")
				.load();
		divisionSelector.setContextItem(documentNode);
		XdmSequenceIterator divisionIterator = divisionSelector.evaluate()
				.iterator();
		while (divisionIterator.hasNext())
			divisions.add(parseDivision((XdmNode)divisionIterator.next()));
		return divisions;
	}
	
	public static void main(String[] args) {
		DtbookParser dtbookParser = new DtbookParser();
		try {
			LinkedList<TextDivision> divisions = dtbookParser.parse(new File(
					"files/624460.xml"));
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
