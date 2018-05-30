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

public class XmlParser {
	
	protected String blockXpath;
	protected String divisionXpath;
	protected Processor processor = new Processor(false);
	protected XPathCompiler xpathCompiler;
	
	public XmlParser(String divisionXpath, String blockXpath) {
		this.blockXpath = blockXpath;
		this.divisionXpath = divisionXpath;
		xpathCompiler = processor.newXPathCompiler();
		xpathCompiler.declareNamespace("html", "http://www.w3.org/1999/xhtml");
	}
	
	protected XdmNode getDocumentNode(File file, boolean preprocess)
			throws SaxonApiException {
		DocumentBuilder documentBuilder = processor.newDocumentBuilder();
		XdmNode document = documentBuilder.build(file);
		if (!preprocess) return document;
		Xslt30Transformer preprocessor = processor.newXsltCompiler().compile(
				new StreamSource(getClass().getResourceAsStream(
						"/epub/tts/poc/parsers/xslt/preprocess.xsl"))).load30();
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
	
	public LinkedList<DivisionInput> parse(File htmlFile)
			throws SaxonApiException {
		// Generic implementation of file parsing. Subclasses can call this,
		// supplying an appropriate XPath expression for their specific format,
		// when overriding parse(File)
		LinkedList<DivisionInput> divisions = new LinkedList<DivisionInput>();
		XdmNode documentNode = getDocumentNode(htmlFile, true);
		XdmSequenceIterator divisionIterator = iterateXpathOnNode(
				divisionXpath, documentNode);
		while (divisionIterator.hasNext())
			divisions.add(parseDivision((XdmNode)divisionIterator.next()));
		return divisions;
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
				".//(" + blockXpath + ")", divisionNode);
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
