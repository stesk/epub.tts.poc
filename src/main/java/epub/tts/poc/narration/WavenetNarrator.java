package epub.tts.poc.narration;

import java.io.IOException;

import com.google.cloud.texttospeech.v1beta1.SsmlVoiceGender;
import com.google.cloud.texttospeech.v1beta1.VoiceSelectionParams;

public class WavenetNarrator extends Narrator {
	
	private String wavenetVoiceName;

	public WavenetNarrator(SsmlVoiceGender gender) throws IOException {
		super(gender);
		switch (gender) {
		case MALE:
			wavenetVoiceName = "en-US-Wavenet-B";
			return;
		case FEMALE:
			wavenetVoiceName = "en-US-Wavenet-C";
			return;
		default:
			wavenetVoiceName = "en-US-Wavenet-B";
		}
	}
	
	@Override
	protected VoiceSelectionParams createVoice(Language language) {
		return VoiceSelectionParams.newBuilder()
				.setLanguageCode("en-US")
				.setName(wavenetVoiceName)
				.build();
	}

}
