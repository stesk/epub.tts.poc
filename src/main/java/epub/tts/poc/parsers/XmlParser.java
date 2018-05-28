package epub.tts.poc.parsers;

import java.io.File;

import net.sf.saxon.s9api.DocumentBuilder;
import net.sf.saxon.s9api.Processor;
import net.sf.saxon.s9api.SaxonApiException;
import net.sf.saxon.s9api.XPathCompiler;
import net.sf.saxon.s9api.XdmNode;

public class XmlParser {
	
	protected Processor processor = new Processor(false);
	protected XPathCompiler xpathCompiler;
	
	public XmlParser() {
		xpathCompiler = processor.newXPathCompiler();
		xpathCompiler.declareNamespace("html", "http://www.w3.org/1999/xhtml");
	}
	
	public XdmNode getDocumentNode(File file) throws SaxonApiException {
		DocumentBuilder documentBuilder = processor.newDocumentBuilder();
		return documentBuilder.build(file);
	}

}
