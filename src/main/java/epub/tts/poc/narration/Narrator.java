package epub.tts.poc.narration;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import com.google.cloud.texttospeech.v1beta1.AudioConfig;
import com.google.cloud.texttospeech.v1beta1.AudioEncoding;
import com.google.cloud.texttospeech.v1beta1.SsmlVoiceGender;
import com.google.cloud.texttospeech.v1beta1.SynthesizeSpeechResponse;
import com.google.cloud.texttospeech.v1beta1.TextToSpeechClient;
import com.google.cloud.texttospeech.v1beta1.VoiceSelectionParams;

public class Narrator {
	
	private AudioConfig audioConfig;
	private SsmlVoiceGender gender;
	private TextToSpeechClient ttsClient;
	
	public Narrator(SsmlVoiceGender gender) throws IOException {
		audioConfig = AudioConfig.newBuilder()
				.setAudioEncoding(AudioEncoding.MP3)
				.build();
		this.gender = gender;
		ttsClient = TextToSpeechClient.create();
	}
	
	private VoiceSelectionParams createVoice(Language language) {
		return VoiceSelectionParams.newBuilder()
				.setLanguageCode(language.getLanguageCode())
				.setSsmlGender(gender)
				.build();
	}
	
	public TextToSpeechClient getTtsClient() {
		return ttsClient;
	}
	
	public ByteBuffer narrate(Iterable<TextInput> inputs) {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		for (TextInput input : inputs) {
			ByteBuffer byteBuffer = narrate(input);
			while (byteBuffer.hasRemaining())
				outputStream.write(byteBuffer.get());
		}
		return ByteBuffer.wrap(outputStream.toByteArray());
	}
	
	public ByteBuffer narrate(TextInput input) {
		SynthesizeSpeechResponse response = ttsClient.synthesizeSpeech(
				input.getInput(), createVoice(input.getLanguage()),
				audioConfig);
		return response.getAudioContent().asReadOnlyByteBuffer();
	}

}
