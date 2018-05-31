package epub.tts.poc.output;

public class BlockOffset {
	
	private String blockId;
	private double endOffset;
	private double startOffset;
	
	public BlockOffset(String blockId, double startOffset) {
		this.blockId = blockId;
		this.endOffset = startOffset;
		this.startOffset = startOffset;
	}
	
	public String getBlockId() {
		return blockId;
	}
	
	public double getEndOffset() {
		return endOffset;
	}
	
	public String getEndOffsetTruncated() {
		return String.format("%.3f", endOffset);
	}
	
	public double getStartOffset() {
		return startOffset;
	}
	
	public String getStartOffsetTruncated() {
		return String.format("%.3f", startOffset);
	}
	
	public void setEndOffset(double endOffset) {
		this.endOffset = endOffset;
	}

}
