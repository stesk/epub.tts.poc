package epub.tts.poc.parsers;

import java.io.File;
import java.util.LinkedList;

import epub.tts.poc.narration.DivisionInput;
import net.sf.saxon.s9api.SaxonApiException;

public class DtbookParser extends HtmlParser {
	
	@Override
	public LinkedList<DivisionInput> parseFile(File htmlFile)
			throws SaxonApiException {
		return parseDocument(getDocumentNode(htmlFile,
					"/epub/tts/poc/parsers/xslt/preprocess-dtbook.xsl"));
	}

}
