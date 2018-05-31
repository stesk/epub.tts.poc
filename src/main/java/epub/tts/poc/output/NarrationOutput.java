package epub.tts.poc.output;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.LinkedList;

public class NarrationOutput {
	
	private ByteArrayOutputStream audioData = new ByteArrayOutputStream();
	private double duration = 0;
	private LinkedList<BlockOffset> offsets = new LinkedList<BlockOffset>();
	
	public void addBytes(byte[] audioBytes) throws IOException {
		// Write bytes to the stream
		audioData.write(audioBytes);
		// Calculate duration of submitted bytes and increase total duration
		// WARNING! Method for calculating duration is inexact and assumes a
		// bitrate of 32 kbps
		// TODO: Check audio output in a more robust fashion
		duration += (double)audioBytes.length * 8 / 32 / 1000;
		// Set end offset for current block
		// TODO: Find a more intuitive way of doing this
		offsets.getLast().setEndOffset(duration);
	}
	
	public byte[] getBytes() {
		return audioData.toByteArray();
	}
	
	public double getDuration() {
		return duration;
	}
	
	public LinkedList<BlockOffset> getOffsets() {
		return offsets;
	}
	
	public void mark(String id) {
		// Add new block
		// Only the start offset is known at this point; the end offset will be
		// updated by the addBytes(byte[]) method
		offsets.add(new BlockOffset(id, duration));
	}

}
