package epub.tts.poc.parsers;

public class DtbookParser extends XmlParser {
	
	public static final String[] DTBOOK_TEXT_BLOCK_ELEMENTS = new String[] {
			"dd",
			"dt",
			"hd",
			"levelhd",
			"li",
			"p",
			"table"
	};
	
	public DtbookParser() {
		super("/dtbook/book/(frontmatter|bodymatter|rearmatter)/level", String
				.join("|", DTBOOK_TEXT_BLOCK_ELEMENTS));
	}

}
