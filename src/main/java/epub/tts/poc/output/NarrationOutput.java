package epub.tts.poc.output;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class NarrationOutput {
	
	private ByteArrayOutputStream audioData = new ByteArrayOutputStream();
	private double duration = 0;
	private String id;
	
	public NarrationOutput(String id) {
		this.id = id;
	}
	
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
	
	public String getId() {
		return id;
	}
	
	public void mark(String id) {
		System.out.println("Id: " + id + ", at " + duration + "s");
	}

}
