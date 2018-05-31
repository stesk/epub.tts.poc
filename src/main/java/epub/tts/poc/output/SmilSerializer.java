package epub.tts.poc.output;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class SmilSerializer {
	
	private String htmlReference;
	private String mp3Reference;
	private Iterable<BlockOffset> offsets;
	
	private final String audioXml =
			"<audio id=\"%s\" clip-begin=\"npt=%s\" clip-end=\"npt=%s\""
			+ " src=\"%s\"/>\n";
	private final String parXml =
			"<par id=\"%s\" endsync=\"last\">\n%s</par>\n";
	private final String textXml = "<text id=\"%s\" src=\"%s\"/>\n";
	
	public SmilSerializer(String htmlReference, String mp3Reference,
			Iterable<BlockOffset> offsets) {
		this.htmlReference = htmlReference;
		this.mp3Reference = mp3Reference;
		this.offsets = offsets;
	}
	
	public void serializeToFile(File file) throws IOException {
		try (FileWriter writer = new FileWriter(file)) {
			writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
			writer.write("<!DOCTYPE smil PUBLIC \"-//W3C//DTD SMIL 1.0//EN\""
					+ " \"http://www.w3.org/TR/REC-smil/SMIL10.dtd\">\n");
			writer.write("<smil>\n<head>\n<layout>\n<region id=\"txtView\">\n"
					+ "</layout>\n</head>\n<body>\n<seq id=\"main\">\n");
			int count = 1;
			for (BlockOffset offset : offsets)
				writer.write(serializeBlock(count++, offset));
			writer.write("</seq>\n</body>\n</smil>");
		}
	}
	
	private String serializeBlock(int count, BlockOffset block) {
		String parId = String.format("par%04d", count);
		String audioId = String.format("aud%04d", count);
		String text = String.format(textXml, block.getBlockId(),
				htmlReference + "#" + block.getBlockId());
		String audio = String.format(audioXml, audioId,
				block.getStartOffsetTruncated(), block.getEndOffsetTruncated(),
				mp3Reference);
		return String.format(parXml, parId, text + audio);
	}

}
