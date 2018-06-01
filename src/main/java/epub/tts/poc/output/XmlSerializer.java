package epub.tts.poc.output;

import java.io.File;

import epub.tts.poc.xml.XmlUtilities;
import net.sf.saxon.s9api.SaxonApiException;
import net.sf.saxon.s9api.Serializer;
import net.sf.saxon.s9api.XdmNode;

public class XmlSerializer {
	
	protected XdmNode document;
	protected Serializer serializer;
	
	public XmlSerializer(XdmNode document) {
		this.document = document;
		serializer = XmlUtilities.getSerializer();
	}
	
	public File serializeToFile(File file) throws SaxonApiException {
		serializer.setOutputFile(file);
		serializer.serializeNode(document);
		return file;
	}

}
