package BagOfWords;
import BagOfWords.Language.language;

import java.util.ArrayList;

public interface Tokenizer {
    String[] getWords(String document,language lang);

    static String[] getLinesWithOutAnnotations(String document, String separator) {
        return new String[0];
    }

    static ArrayList<String> getAnnotations(String document, String separator) {
        return null;
    }
}