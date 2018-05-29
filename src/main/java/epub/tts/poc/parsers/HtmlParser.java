package epub.tts.poc.parsers;

public class HtmlParser extends XmlParser {
	
	public static final String[] HTML_TEXT_BLOCK_ELEMENTS = new String[] {
			"html:dd",
			"html:dt",
			"html:figcaption",
			"html:h1",
			"html:h2",
			"html:h3",
			"html:h4",
			"html:h5",
			"html:h6",
			"html:li",
			"html:p",
			"html:table"
	};
	
	public HtmlParser() {
		super("/html:html/html:body/html:section", String.join("|",
				HTML_TEXT_BLOCK_ELEMENTS));
	}

}
