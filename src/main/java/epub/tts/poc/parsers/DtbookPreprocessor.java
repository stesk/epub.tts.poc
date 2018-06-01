package epub.tts.poc.parsers;

import java.io.File;

import epub.tts.poc.xml.XmlUtilities;
import net.sf.saxon.s9api.SaxonApiException;
import net.sf.saxon.s9api.XdmNode;

public class DtbookPreprocessor extends Preprocessor {

	@Override
	public XdmNode preprocess(File file) throws SaxonApiException {
		return preprocess(XmlUtilities.getDocumentNode(file));
	}

	@Override
	public XdmNode preprocess(XdmNode document) throws SaxonApiException {
		return preprocessWithXslt(document,
				"/epub/tts/poc/parsers/xslt/preprocess-dtbook.xsl");
	}

}
