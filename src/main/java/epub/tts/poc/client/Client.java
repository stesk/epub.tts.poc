package epub.tts.poc.client;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.LinkedList;

import com.google.cloud.texttospeech.v1beta1.SsmlVoiceGender;

import epub.tts.poc.narration.NarrationInput;
import epub.tts.poc.narration.Narrator;
import epub.tts.poc.narration.Voice;
import epub.tts.poc.parsers.HtmlParser;
import net.sf.saxon.s9api.SaxonApiException;

public class Client {
	
	public static void main(String[] args) {
//		String inputFileString = args[0];
		String inputFileString = "files/english-continuous-block.html";
		File inputFile = new File(inputFileString);
		try {
			Narrator narrator = new Narrator();
			HtmlParser parser = new HtmlParser();
			LinkedList<NarrationInput> inputs = new LinkedList<NarrationInput>();
			parser.parse(inputFile).forEach(
					fragment -> inputs.add(new NarrationInput(fragment.getText(),
							new Voice(SsmlVoiceGender.MALE, fragment
									.getLanguage()))));
			File outputFile = new File(inputFileString.replaceFirst("\\..*?$",
					".mp3"));
			Files.createFile(outputFile.toPath());
			long startTime = System.currentTimeMillis();
			writeByteBufferToFile(narrator.narrate(inputs), outputFile);
			long endTime = System.currentTimeMillis();
			System.out.println("Narration completed in "
					+ (endTime - startTime) / 1000 + " seconds");
		} catch (IOException | SaxonApiException e) {
			e.printStackTrace();
		}
	}
	
	public static void writeByteBufferToFile(ByteBuffer buffer, File file)
			throws IOException {
		Files.newByteChannel(file.toPath(), StandardOpenOption.WRITE)
			.write(buffer);
	}

}
