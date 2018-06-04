package epub.tts.poc.output;

import java.io.File;

import epub.tts.poc.xml.XmlUtilities;
import net.sf.saxon.s9api.SaxonApiException;
import net.sf.saxon.s9api.XdmNode;

public class NccSerializer extends XmlSerializer {

	public NccSerializer(XdmNode document) {
		super(document);
	}
	
	public File serializeNccToFile(File file) throws SaxonApiException {
		document = XmlUtilities.applyXslt(document.asSource(),
				"/epub/tts/poc/output/xslt/generate-ncc.xsl");
		return super.serializeToFile(file);
	}

}
