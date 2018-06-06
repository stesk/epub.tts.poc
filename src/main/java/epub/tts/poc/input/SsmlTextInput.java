package epub.tts.poc.input;

import java.io.StringReader;
import java.util.Collections;

import javax.xml.transform.stream.StreamSource;

import epub.tts.poc.narration.Language;
import epub.tts.poc.xml.XmlUtilities;
import net.sf.saxon.s9api.QName;
import net.sf.saxon.s9api.SaxonApiException;
import net.sf.saxon.s9api.SaxonApiUncheckedException;
import net.sf.saxon.s9api.XdmAtomicValue;
import net.sf.saxon.s9api.XdmDestination;
import net.sf.saxon.s9api.XdmNode;
import net.sf.saxon.s9api.XdmValue;
import net.sf.saxon.s9api.Xslt30Transformer;

public class SsmlTextInput extends TextInput {
	
	private XdmNode ssmlNode;
	
	public SsmlTextInput(XdmNode ssml, Language language) {
		super(language);
		this.ssmlNode = ssml;
		setSsml(ssmlNode.toString());
	}
	
	public XdmNode getSsmlNode() {
		return ssmlNode;
	}

	@Override
	public void mergeInput(PlainTextInput input) {
		try {
			mergeWithParam(new QName("PLAIN_TEXT_STRING"), new XdmAtomicValue(
					input.getText()));
		} catch (SaxonApiException e) {
			throw new SaxonApiUncheckedException(e);
		}
		
	}

	@Override
	public void mergeInput(SsmlTextInput other) {
		try {
			mergeWithParam(new QName("SSML_DOCUMENTS"), other.getSsmlNode());
		} catch (SaxonApiException e) {
			throw new SaxonApiUncheckedException(e);
		}
	}
	
	private void mergeWithParam(QName param, XdmValue paramValue)
			throws SaxonApiException {
		Xslt30Transformer ssmlMerger = XmlUtilities.getXsltTransformer(
				"/epub/tts/poc/input/xslt/merge-ssml.xsl");
		ssmlMerger.setStylesheetParameters(Collections.singletonMap(
				param, paramValue));
		XdmDestination mergeDestination = new XdmDestination();
		ssmlMerger.applyTemplates(ssmlNode.asSource(), mergeDestination);
		updateSsmlNode(mergeDestination.getXdmNode());
	}
	
	private void setSsml(String ssml) {
		input = input.toBuilder()
				.setSsml(ssml)
				.build();
	}
	
	private void updateSsmlNode(XdmNode ssmlNode) {
		this.ssmlNode = ssmlNode;
		setSsml(ssmlNode.toString());
	}

}
