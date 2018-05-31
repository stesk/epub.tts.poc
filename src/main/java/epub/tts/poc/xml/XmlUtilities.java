package epub.tts.poc.xml;

import java.io.File;

import javax.xml.transform.stream.StreamSource;

import net.sf.saxon.s9api.DocumentBuilder;
import net.sf.saxon.s9api.Processor;
import net.sf.saxon.s9api.SaxonApiException;
import net.sf.saxon.s9api.Serializer;
import net.sf.saxon.s9api.XPathCompiler;
import net.sf.saxon.s9api.XPathSelector;
import net.sf.saxon.s9api.XdmNode;
import net.sf.saxon.s9api.Xslt30Transformer;
import net.sf.saxon.s9api.XsltCompiler;

public class XmlUtilities {
	
	private static Processor processor = new Processor(false);
	private static XPathCompiler xpathCompiler;
	private static XsltCompiler xsltCompiler;
	
	static {
		xpathCompiler = processor.newXPathCompiler();
		xpathCompiler.declareNamespace("html", "http://www.w3.org/1999/xhtml");
		xsltCompiler = processor.newXsltCompiler();
	}
	
	public static DocumentBuilder getDocumentBuilder() {
		return processor.newDocumentBuilder();
	}
	
	public static XdmNode getDocumentNode(File file) throws SaxonApiException {
		return getDocumentBuilder().build(file);
	}
	
	public static Serializer getSerializer() {
		return processor.newSerializer();
	}
	
	public static Serializer getSerializer(File file) {
		return processor.newSerializer(file);
	}
	
	public static XPathSelector getXpathSelector(String xpath)
			throws SaxonApiException {
		return xpathCompiler.compile(xpath).load();
	}
	
	public static Xslt30Transformer getXsltTransformer(String source)
			throws SaxonApiException {
		return xsltCompiler.compile(new StreamSource(XmlUtilities.class
				.getResourceAsStream(source))).load30();
	}

}
