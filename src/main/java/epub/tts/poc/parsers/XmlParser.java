package epub.tts.poc.parsers;

import java.io.File;

import javax.xml.transform.stream.StreamSource;

import epub.tts.poc.narration.Language;
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

public abstract class XmlParser {
	
	protected Processor processor = new Processor(false);
	protected XPathCompiler xpathCompiler;
	
	public XmlParser() {
		xpathCompiler = processor.newXPathCompiler();
		xpathCompiler.declareNamespace("html", "http://www.w3.org/1999/xhtml");
	}
	
	public XdmNode getDocumentNode(File file, boolean preprocess)
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
	
	protected TextBlock parseBlock(XdmNode blockNode) throws SaxonApiException {
		TextBlock textBlock = new TextBlock(blockNode.getAttributeValue(
				new QName("id")));
		XPathSelector textSelector = xpathCompiler.compile(
				"descendant::text()").load();
		XPathSelector langSelector = xpathCompiler.compile(
				"ancestor-or-self::*[@lang][1]/@lang").load();
		textSelector.setContextItem(blockNode);
		textSelector.setContextItem(blockNode);
		XdmSequenceIterator textIterator = textSelector.evaluate()
				.iterator();
		while (textIterator.hasNext()) {
			XdmNode textNode = (XdmNode)textIterator.next();
			langSelector.setContextItem(textNode);
			String langValue = langSelector.evaluateSingle()
					.getStringValue();
			textBlock.add(new TextFragment(Language.getLanguage(langValue),
					textNode.getStringValue().replaceAll("\\s+", " ")));
		}
		return textBlock;
	}

}
