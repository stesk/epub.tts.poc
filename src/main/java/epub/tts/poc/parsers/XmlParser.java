package epub.tts.poc.parsers;

import java.io.File;

import javax.xml.transform.stream.StreamSource;

import net.sf.saxon.s9api.DocumentBuilder;
import net.sf.saxon.s9api.Processor;
import net.sf.saxon.s9api.SaxonApiException;
import net.sf.saxon.s9api.XPathCompiler;
import net.sf.saxon.s9api.XdmDestination;
import net.sf.saxon.s9api.XdmNode;
import net.sf.saxon.s9api.Xslt30Transformer;

public class XmlParser {
	
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

}
