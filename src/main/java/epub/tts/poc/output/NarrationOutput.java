package epub.tts.poc.output;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;

public class NarrationOutput {
	
	private ByteArrayOutputStream audioData = new ByteArrayOutputStream();
	private double duration = 0;
	private HashMap<String,String> idOffsets = new HashMap<String,String>();
	
	public void addBytes(byte[] audioBytes) throws IOException {
		// WARNING! Method for calculating duration is inexact and assumes a
		// bitrate of 32 kbps
		// TODO: Check audio output in a more robust fashion
		duration += (double)audioBytes.length * 8 / 32 / 1000;
		audioData.write(audioBytes);
	}
	
	public byte[] getBytes() {
		return audioData.toByteArray();
	}
	
	public double getDuration() {
		return duration;
	}
	
	public HashMap<String,String> getIdOffsets() {
		return idOffsets;
	}
	
	public void mark(String id) {
		idOffsets.put(id, String.format("%.3f", duration));
	}

}
