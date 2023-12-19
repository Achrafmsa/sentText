package BagOfWords;

import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealMatrixFormat;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;

import static BagOfWords.SimpleTokenizer.ListToStringLines;

public class TEST {

	public static void main(String[] args) throws IOException {
		/* need a basic tokenizer to parse text */
		SimpleTokenizer tokenizer = new SimpleTokenizer(1);
		TermDictionary termDictionary = new TermDictionary();
		File file = new File("C:\\Users\\tariq\\Desktop\\our tests\\train.txt");
		List<String> document = null;
		try {
			document = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);
		} catch (IOException e) {
			e.printStackTrace();
		}
		/* add all terms in sentiment dataset to dictionary */
		for (String line : document) {
			String[] tokens = tokenizer.getWords(line, Language.language.Arabic);
			termDictionary.addTerms(tokens);
		}
		/* create of matrix of word counts for each sentence */
		Vectorizer vectorizer = new Vectorizer(termDictionary, tokenizer, false);
		RealMatrix counts = vectorizer.getCountMatrix(document);

		/* ... or create a binary counter */
		Vectorizer binaryVectorizer = new Vectorizer(termDictionary, tokenizer, true);
		RealMatrix binCounts = binaryVectorizer.getCountMatrix(document);

		/* ... or create a matrix TFIDF  */
		TFIDFVectorizer tfidfVectorizer = new TFIDFVectorizer(termDictionary, tokenizer);
		RealMatrix tfidf = tfidfVectorizer.getTFIDF(document);

	}

}
