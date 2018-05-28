package epub.tts.poc.narration;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Map;

import com.google.cloud.texttospeech.v1beta1.SynthesizeSpeechResponse;
import com.google.cloud.texttospeech.v1beta1.TextToSpeechClient;

public class Narrator {
	
	private TextToSpeechClient ttsClient;
	
	public Narrator() throws IOException {
		ttsClient = TextToSpeechClient.create();
	}
	
	public TextToSpeechClient getTtsClient() {
		return ttsClient;
	}
	
	public ByteBuffer narrate(Iterable<NarrationInput> inputs) {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		for (NarrationInput input : inputs) {
			ByteBuffer byteBuffer = narrate(input);
			while (byteBuffer.hasRemaining())
				outputStream.write(byteBuffer.get());
		}
		return ByteBuffer.wrap(outputStream.toByteArray());
	}
	
	public ByteBuffer narrate(NarrationInput input) {
		SynthesizeSpeechResponse response = ttsClient.synthesizeSpeech(
				input.getInput(), input.getVoiceParams(),
				input.getAudioConfig());
		return response.getAudioContent().asReadOnlyByteBuffer();
	}

}
