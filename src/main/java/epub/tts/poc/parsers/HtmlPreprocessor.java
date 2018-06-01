package epub.tts.poc.parsers;

import epub.tts.poc.xml.XmlUtilities;
import net.sf.saxon.s9api.SaxonApiException;
import net.sf.saxon.s9api.XdmNode;

public class HtmlPreprocessor extends Preprocessor {

	@Override
	public XdmNode preprocess(XdmNode document) throws SaxonApiException {
		return XmlUtilities.applyXslt(document.asSource(),
				"/epub/tts/poc/parsers/xslt/preprocess-html.xsl");
	}
	
	

}
