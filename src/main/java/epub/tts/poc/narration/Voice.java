package epub.tts.poc.narration;

import com.google.cloud.texttospeech.v1beta1.SsmlVoiceGender;

public class Voice {
	
	private SsmlVoiceGender gender;
	private Language language;
	
	public Voice(SsmlVoiceGender gender, Language language) {
		this.gender = gender;
		this.language = language;
	}
	
	public SsmlVoiceGender getGender() {
		return gender;
	}
	
	public Language getLanguage() {
		return language;
	}

}
