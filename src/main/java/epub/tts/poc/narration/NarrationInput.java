package epub.tts.poc.narration;

import com.google.cloud.texttospeech.v1beta1.AudioConfig;
import com.google.cloud.texttospeech.v1beta1.AudioEncoding;
import com.google.cloud.texttospeech.v1beta1.SynthesisInput;
import com.google.cloud.texttospeech.v1beta1.VoiceSelectionParams;

public class NarrationInput {
	
	private AudioConfig audioConfig;
	private SynthesisInput input;
	private VoiceSelectionParams voiceParams;
	
	public NarrationInput(String text, Voice voice) {
		AudioConfig.Builder audioConfigBuilder = AudioConfig.newBuilder();
		audioConfigBuilder.setAudioEncoding(AudioEncoding.MP3);
		SynthesisInput.Builder inputBuilder = SynthesisInput.newBuilder();
		inputBuilder.setText(text);
		VoiceSelectionParams.Builder voiceParamsBuilder = VoiceSelectionParams
				.newBuilder();
		voiceParamsBuilder.setLanguageCode(voice.getLanguage()
				.getLanguageCode());
		voiceParamsBuilder.setSsmlGender(voice.getGender());
		audioConfig = audioConfigBuilder.build();
		input = inputBuilder.build();
		voiceParams = voiceParamsBuilder.build();
	}
	
	public AudioConfig getAudioConfig() {
		return audioConfig;
	}
	
	public SynthesisInput getInput() {
		return input;
	}
	
	public VoiceSelectionParams getVoiceParams() {
		return voiceParams;
	}

}
