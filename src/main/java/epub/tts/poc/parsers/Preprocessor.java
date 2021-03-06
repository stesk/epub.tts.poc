package epub.tts.poc.parsers;

import java.io.File;

import epub.tts.poc.xml.XmlUtilities;
import net.sf.saxon.s9api.SaxonApiException;
import net.sf.saxon.s9api.XdmNode;

public abstract class Preprocessor {
	
	public abstract XdmNode preprocess(XdmNode document)
			throws SaxonApiException;
	
	public XdmNode preprocess(File file) throws SaxonApiException {
		return preprocess(XmlUtilities.getDocumentNode(file));
	}

}
