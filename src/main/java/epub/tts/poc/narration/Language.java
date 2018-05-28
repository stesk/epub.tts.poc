package epub.tts.poc.narration;

public enum Language {
	
	DE_DE,
	EN_AU,
	EN_GB,
	EN_US,
	ES_ES,
	FR_CA,
	FR_FR,
	IT_IT,
	JA_JP,
	NL_NL,
	PT_BR,
	SV_SE,
	TR_TR;
	
	public String getLanguageCode() {
		switch (this) {
		case DE_DE: return "de-DE";
		case EN_AU: return "en-AU";
		case EN_GB: return "en-GB";
		case EN_US: return "en-US";
		case ES_ES: return "es-ES";
		case FR_CA: return "fr-CA";
		case FR_FR: return "fr-FR";
		case IT_IT: return "it-IT";
		case JA_JP: return "ja-JP";
		case NL_NL: return "nl-NL";
		case PT_BR: return "pt-BR";
		case SV_SE: return "sv-SE";
		case TR_TR: return "tr-TR";
		default: return "en-GB";
		}
	}
	
	public static Language getLanguage(String languageCode) {
		if (languageCode.startsWith("de")) return DE_DE;
		else if (languageCode.startsWith("en")) return EN_GB;
		else if (languageCode.startsWith("es")) return ES_ES;
		else if (languageCode.startsWith("fr")) return FR_FR;
		else if (languageCode.startsWith("it")) return IT_IT;
		else if (languageCode.startsWith("ja")) return JA_JP;
		else if (languageCode.startsWith("nl")) return NL_NL;
		else if (languageCode.startsWith("pt")) return PT_BR;
		else if (languageCode.startsWith("sv")) return SV_SE;
		else if (languageCode.startsWith("tr")) return TR_TR;
		else return EN_GB;
	}

}
