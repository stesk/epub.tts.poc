package epub.tts.poc.parsers;

import java.io.File;

import javax.xml.transform.Source;

import epub.tts.poc.xml.XmlUtilities;
import net.sf.saxon.s9api.SaxonApiException;
import net.sf.saxon.s9api.XdmDestination;
import net.sf.saxon.s9api.XdmNode;
import net.sf.saxon.s9api.Xslt30Transformer;

public abstract class Preprocessor {
	
	public abstract XdmNode preprocess(XdmNode document)
			throws SaxonApiException;
	
	public abstract XdmNode preprocess(File file) throws SaxonApiException;
	
	protected XdmNode preprocessWithXslt(Source source, String xslt)
			throws SaxonApiException {
		Xslt30Transformer preprocessor = XmlUtilities.getXsltTransformer(xslt);
		XdmDestination destination = new XdmDestination();
		preprocessor.applyTemplates(source, destination);
		return destination.getXdmNode();
	}

}
