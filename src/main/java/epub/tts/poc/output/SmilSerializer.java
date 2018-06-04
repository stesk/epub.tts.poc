package epub.tts.poc.output;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import epub.tts.poc.xml.XmlUtilities;
import net.sf.saxon.s9api.QName;
import net.sf.saxon.s9api.SaxonApiException;
import net.sf.saxon.s9api.XdmAtomicValue;
import net.sf.saxon.s9api.XdmNode;
import net.sf.saxon.s9api.XdmValue;
import net.sf.saxon.s9api.Xslt30Transformer;

public class SmilSerializer extends XmlSerializer {
	
	private String htmlFileName;
	
	public SmilSerializer(XdmNode document, String htmlFileName) {
		super(document);
		this.htmlFileName = htmlFileName;
	}
	
	public File serializeMasterSmilToFile(File file) throws SaxonApiException {
		Xslt30Transformer masterSmilTransformer = XmlUtilities
				.getXsltTransformer(
						"/epub/tts/poc/output/xslt/generate-master-smil.xsl");
		serializer.setOutputFile(file);
		masterSmilTransformer.applyTemplates(document.asSource(), serializer);
		serializer.close();
		return file;
	}
	
	public File serializeSmilToFile(NarrationOutput output, String mp3FileName,
			File file) throws SaxonApiException {
		Xslt30Transformer smilTransformer = XmlUtilities.getXsltTransformer(
				"/epub/tts/poc/output/xslt/generate-smil.xsl");
		List<XdmAtomicValue> idValues = new LinkedList<XdmAtomicValue>();
		List<XdmAtomicValue> startValues = new LinkedList<XdmAtomicValue>();
		List<XdmAtomicValue> endValues = new LinkedList<XdmAtomicValue>();
		output.getOffsets().forEach(
				offset -> {
					idValues.add(new XdmAtomicValue(offset.getBlockId()));
					startValues.add(new XdmAtomicValue(offset
							.getStartOffsetTruncated()));
					endValues.add(new XdmAtomicValue(offset
							.getEndOffsetTruncated()));
				});
		HashMap<QName,XdmValue> parameters = new HashMap<QName,XdmValue>();
		parameters.put(new QName("HTML_FILE_NAME"), new XdmAtomicValue(
				htmlFileName));
		parameters.put(new QName("MP3_FILE_NAME"), new XdmAtomicValue(
				mp3FileName));
		parameters.put(new QName("ID_VALUES"), new XdmValue(idValues));
		parameters.put(new QName("START_VALUES"), new XdmValue(startValues));
		parameters.put(new QName("END_VALUES"), new XdmValue(endValues));
		smilTransformer.setStylesheetParameters(parameters);
		serializer.setOutputFile(file);
		smilTransformer.applyTemplates(document.asSource(), serializer);
		serializer.close();
		return file;
	}

}
