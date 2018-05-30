package epub.tts.poc.parsers;

import java.io.File;
import java.util.LinkedList;

import javax.xml.transform.stream.StreamSource;

import epub.tts.poc.narration.BlockInput;
import epub.tts.poc.narration.DivisionInput;
import epub.tts.poc.narration.Language;
import epub.tts.poc.narration.TextInput;
import net.sf.saxon.s9api.DocumentBuilder;
import net.sf.saxon.s9api.Processor;
import net.sf.saxon.s9api.QName;
import net.sf.saxon.s9api.SaxonApiException;
import net.sf.saxon.s9api.XPathCompiler;
import net.sf.saxon.s9api.XPathSelector;
import net.sf.saxon.s9api.XdmDestination;
import net.sf.saxon.s9api.XdmNode;
import net.sf.saxon.s9api.XdmSequenceIterator;
import net.sf.saxon.s9api.Xslt30Transformer;

public class HtmlParser {
	
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
	
	private Processor processor = new Processor(false);
	private XPathCompiler xpathCompiler;
	
	public HtmlParser() {
		xpathCompiler = processor.newXPathCompiler();
		xpathCompiler.declareNamespace("html", "http://www.w3.org/1999/xhtml");
	}
	
	protected XdmNode getDocumentNode(File file, String preprocessorXslt)
			throws SaxonApiException {
		DocumentBuilder documentBuilder = processor.newDocumentBuilder();
		XdmNode document = documentBuilder.build(file);
		Xslt30Transformer preprocessor = processor.newXsltCompiler().compile(
				new StreamSource(getClass().getResourceAsStream(
						preprocessorXslt)))
				.load30();
		XdmDestination preprocessorDestination = new XdmDestination();
		preprocessor.applyTemplates(document, preprocessorDestination);
		return preprocessorDestination.getXdmNode();
	}
	
	private XdmSequenceIterator iterateXpathOnNode(String xpath, XdmNode node)
			throws SaxonApiException {
		XPathSelector selector = xpathCompiler.compile(xpath).load();
		selector.setContextItem(node);
		return selector.evaluate().iterator();
	}
	
	protected LinkedList<DivisionInput> parseDocument(XdmNode documentNode)
			throws SaxonApiException {
		LinkedList<DivisionInput> divisions = new LinkedList<DivisionInput>();
		XdmSequenceIterator divisionIterator = iterateXpathOnNode(
				"/html:html/html:body/html:section", documentNode);
		while (divisionIterator.hasNext())
			divisions.add(parseDivision((XdmNode)divisionIterator.next()));
		return divisions;
	}
	
	public LinkedList<DivisionInput> parseFile(File htmlFile)
			throws SaxonApiException {
		return parseDocument(getDocumentNode(htmlFile,
				"/epub/tts/poc/parsers/xslt/preprocess-xhtml.xsl"));
	}
	
	private DivisionInput parseDivision(XdmNode divisionNode)
			throws SaxonApiException {
		// Generic implementation of division parsing. Subclasses can call this,
		// supplying an appropriate XPath expression for their specific format,
		// when overriding parse(XdmNode)
		DivisionInput division = new DivisionInput(divisionNode);
		// We want the innermost block elements, e.g. block elements with no
		// other block elements as their children
		// TODO: Test this to make sure it works as intended
		XdmSequenceIterator blockIterator = iterateXpathOnNode(
				".//(" + String.join("|", HTML_TEXT_BLOCK_ELEMENTS) + ")",
				divisionNode);
		while (blockIterator.hasNext())
			division.add(parseBlock((XdmNode)blockIterator.next()));
		return division;
	}

	private BlockInput parseBlock(XdmNode blockNode) throws SaxonApiException {
		BlockInput block = new BlockInput(blockNode.getAttributeValue(
				new QName("id")));
		XPathSelector langSelector = xpathCompiler.compile(
				"ancestor-or-self::*[@lang][1]/@lang").load();
		XdmSequenceIterator textIterator = iterateXpathOnNode(
				"descendant::text()", blockNode);
		while (textIterator.hasNext()) {
			XdmNode textNode = (XdmNode)textIterator.next();
			langSelector.setContextItem(textNode);
			String langValue = langSelector.evaluateSingle()
					.getStringValue();
			block.add(new TextInput(textNode.getStringValue().replaceAll(
					"\\s+", " "), Language.getLanguage(langValue)));
		}
		return block;
	}

}
