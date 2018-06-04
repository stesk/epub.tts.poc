package epub.tts.poc.client;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import com.google.cloud.texttospeech.v1beta1.SsmlVoiceGender;

import epub.tts.poc.input.DivisionInput;
import epub.tts.poc.narration.Narrator;
import epub.tts.poc.output.NarrationOutput;
import epub.tts.poc.output.NccSerializer;
import epub.tts.poc.output.SmilSerializer;
import epub.tts.poc.parsers.DtbookPreprocessor;
import epub.tts.poc.parsers.HtmlParser;
import epub.tts.poc.parsers.HtmlPreprocessor;
import epub.tts.poc.xml.XmlUtilities;
import net.sf.saxon.s9api.SaxonApiException;
import net.sf.saxon.s9api.Serializer;
import net.sf.saxon.s9api.XdmNode;

public class Client {
	
	public static void main(String[] args) {
//		String inputFileString = args[0];
		String inputFileString = "files/627012.xml";
		File inputFile = new File(inputFileString);
		try {
			Narrator narrator = new Narrator(SsmlVoiceGender.FEMALE);
			XdmNode document = new DtbookPreprocessor().preprocess(inputFile);
			document = new HtmlPreprocessor().preprocess(document);
			File htmlFile = new File("files/output/627012.html");
			Serializer htmlSerializer = XmlUtilities.getSerializer(htmlFile);
			htmlSerializer.setOutputProperty(Serializer.Property.DOCTYPE_PUBLIC, 
					"-//W3C//DTD XHTML 1.0 Transitional//EN");
			htmlSerializer.setOutputProperty(Serializer.Property.DOCTYPE_SYSTEM,
					"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd");
			htmlSerializer.serializeNode(document);
			htmlSerializer.close();
			new NccSerializer(document).serializeNccToFile(new File(
					"files/output/ncc.html"));
			SmilSerializer smilSerializer = new SmilSerializer(
					document, htmlFile.getName());
			smilSerializer.serializeMasterSmilToFile(new File(
					"files/output/master.smil"));
			long startTime = System.currentTimeMillis();
			int count = 1;
			for (DivisionInput input : new HtmlParser(false).parse(document)) {
				NarrationOutput output = narrator.narrate(input);
				String fileNameBase = String.format("%04d", count++);
				String mp3FileName = fileNameBase + ".mp3";
				File mp3File = new File("files/output/" + mp3FileName);
				Files.createFile(mp3File.toPath());
				Files.newOutputStream(mp3File.toPath()).write(
						output.getBytes());
				File smilFile = new File(
						"files/output/" + fileNameBase + ".smil");
				smilSerializer.serializeSmilToFile(output, mp3File.getName(),
						smilFile);
			}
			long endTime = System.currentTimeMillis();
			System.out.println("Narration completed in "
					+ (endTime - startTime) / 1000 + " seconds");
		} catch (SaxonApiException | IOException e) {
			e.printStackTrace();
		}
	}

}
