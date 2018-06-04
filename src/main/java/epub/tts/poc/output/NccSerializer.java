package epub.tts.poc.output;

import java.io.File;

import epub.tts.poc.xml.XmlUtilities;
import net.sf.saxon.s9api.SaxonApiException;
import net.sf.saxon.s9api.Serializer;
import net.sf.saxon.s9api.XdmNode;

public class NccSerializer extends XmlSerializer {

	public NccSerializer(XdmNode document) {
		super(document);
		serializer.setOutputProperty(Serializer.Property.DOCTYPE_PUBLIC, 
				"-//W3C//DTD XHTML 1.0 Transitional//EN");
		serializer.setOutputProperty(Serializer.Property.DOCTYPE_SYSTEM,
				"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd");
	}
	
	public File serializeNccToFile(File file) throws SaxonApiException {
		document = XmlUtilities.applyXslt(document.asSource(),
				"/epub/tts/poc/output/xslt/generate-ncc.xsl");
		return super.serializeToFile(file);
	}

}
