package epub.tts.poc.narration;

import java.io.IOException;

import com.google.cloud.texttospeech.v1beta1.AudioConfig;
import com.google.cloud.texttospeech.v1beta1.AudioEncoding;
import com.google.cloud.texttospeech.v1beta1.SsmlVoiceGender;
import com.google.cloud.texttospeech.v1beta1.SynthesizeSpeechResponse;
import com.google.cloud.texttospeech.v1beta1.TextToSpeechClient;
import com.google.cloud.texttospeech.v1beta1.VoiceSelectionParams;

import epub.tts.poc.input.BlockInput;
import epub.tts.poc.input.DivisionInput;
import epub.tts.poc.input.TextInput;
import epub.tts.poc.output.NarrationOutput;

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
	
	protected VoiceSelectionParams createVoice(Language language) {
		return VoiceSelectionParams.newBuilder()
				.setLanguageCode(language.getLanguageCode())
				.setSsmlGender(gender)
				.build();
	}
	
	public TextToSpeechClient getTtsClient() {
		return ttsClient;
	}
	
	public NarrationOutput narrate(DivisionInput division) throws IOException {
		NarrationOutput output = new NarrationOutput();
		for (BlockInput block : division) {
			output.mark(block.getId());
			for (TextInput text : block) output.addBytes(narrateText(text));
		}
		return output;
		
	}
	
	private byte[] narrateText(TextInput text) {
		SynthesizeSpeechResponse response = ttsClient.synthesizeSpeech(
				text.getInput(), createVoice(text.getLanguage()), audioConfig);
		return response.getAudioContent().toByteArray();
	}

}
