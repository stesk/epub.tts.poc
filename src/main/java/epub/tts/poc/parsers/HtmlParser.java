package epub.tts.poc.parsers;

import java.io.File;
import java.util.LinkedList;

import epub.tts.poc.narration.Language;
import net.sf.saxon.s9api.SaxonApiException;
import net.sf.saxon.s9api.XPathSelector;
import net.sf.saxon.s9api.XdmItem;
import net.sf.saxon.s9api.XdmNode;
import net.sf.saxon.s9api.XdmSequenceIterator;

public class HtmlParser extends XmlParser {
	
	public Iterable<TextFragment> parse(File htmlFile)
			throws SaxonApiException {
		LinkedList<TextFragment> fragments = new LinkedList<TextFragment>();
		XdmNode documentNode = getDocumentNode(htmlFile);
		// WARNING! Significant whitespace may disappear
		// TODO: Handle interstitial whitespace between inline elements
		XPathSelector textNodeSelector = xpathCompiler.compile(
				"/html:html/html:body//text()[normalize-space()]").load();
		XPathSelector langSelector = xpathCompiler.compile(
				"ancestor-or-self::*[@xml:lang][1]/@xml:lang").load();
		textNodeSelector.setContextItem(documentNode);
		XdmSequenceIterator iterator = textNodeSelector.evaluate().iterator();
		while (iterator.hasNext()) {
			XdmItem item = iterator.next();
			langSelector.setContextItem(item);
			String langString = langSelector.evaluateSingle().getStringValue();
			Language language = langString.length() > 0 ?
					Language.getLanguage(langString) : Language.EN_GB;
			String text = item.getStringValue().replaceAll("\\s+", " ");
			fragments.add(new TextFragment(language, text));
			System.out.println(langString + ": " + text);
		}
		return fragments;
	}
	
	public static void main(String[] args) {
		HtmlParser htmlParser = new HtmlParser();
		try {
			htmlParser.parse(new File("input/simple-block.html"));
		} catch (SaxonApiException e) {
			e.printStackTrace();
		}
	}

}
