package epub.tts.poc.parsers;

import epub.tts.poc.input.BlockInput;
import epub.tts.poc.input.DivisionInput;
import epub.tts.poc.input.PlainTextInput;
import epub.tts.poc.input.SsmlTextInput;
import epub.tts.poc.narration.Language;
import epub.tts.poc.xml.XmlUtilities;
import net.sf.saxon.s9api.QName;
import net.sf.saxon.s9api.SaxonApiException;
import net.sf.saxon.s9api.XPathSelector;
import net.sf.saxon.s9api.XdmItem;
import net.sf.saxon.s9api.XdmNode;
import net.sf.saxon.s9api.XdmSequenceIterator;
import net.sf.saxon.s9api.Xslt30Transformer;

public class HtmlSentenceParser extends HtmlParser {

	public HtmlSentenceParser(boolean ssmlEnabled) {
		super(ssmlEnabled);
	}
	
	@Override
	protected DivisionInput parseDivision(XdmNode divisionNode)
			throws SaxonApiException {
		XdmItem heading = XmlUtilities.evaluateXpathOnNode(
				"html:h1|html:h2|html:h3|html:h4|html:h5|html:h6",
				divisionNode);
		String title = heading == null? "***" : heading.getStringValue();
		DivisionInput division = new DivisionInput(title);
		XdmSequenceIterator sentenceIterator = XmlUtilities.iterateXpathOnNode(
				"descendant::html:span[@class eq 'sentence']", divisionNode);
		while (sentenceIterator.hasNext())
			division.add(parseBlock((XdmNode)sentenceIterator.next()));
		return division;
	}
	
	@Override
	protected BlockInput parseBlockPlain(XdmNode sentenceNode)
			throws SaxonApiException {
		BlockInput block = new BlockInput(sentenceNode.getAttributeValue(
				new QName("id")));
		XPathSelector langSelector = XmlUtilities.getXpathSelector(
				"ancestor-or-self::*[@lang][1]/@lang");
		langSelector.setContextItem(sentenceNode);
		String langValue = langSelector.evaluateSingle()
				.getStringValue();
		block.add(new PlainTextInput(sentenceNode.getStringValue().replaceAll(
				"\\s+", " "), Language.getLanguage(langValue)));
		return block;
	}
	
	@Override
	protected BlockInput parseBlockSsml(XdmNode sentenceNode)
			throws SaxonApiException {
		BlockInput block = new BlockInput(sentenceNode.getAttributeValue(
				new QName("id")));
		Xslt30Transformer ssmlTransformer = XmlUtilities.getXsltTransformer(
				"/epub/tts/poc/parsers/xslt/generate-ssml.xsl");
		ssmlTransformer.setInitialMode(new QName("SENTENCE_INPUT"));
		ssmlTransformer.applyTemplates(sentenceNode).forEach(
				item -> {
					XdmNode node = (XdmNode)item;
					String langValue = node.getAttributeValue(new QName(
							"lang"));
					block.add(new SsmlTextInput(node,
						Language.getLanguage(langValue)));
				});
		return block;
	}

}
