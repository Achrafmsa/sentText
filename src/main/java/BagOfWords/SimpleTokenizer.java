package BagOfWords;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import BagOfWords.Language.language;

import static BagOfWords.Language.getRegex;

public class SimpleTokenizer implements Tokenizer {

    private final int minTokenSize;

    public SimpleTokenizer(int minTokenSize) {
        this.minTokenSize = minTokenSize;
    }

    public static ArrayList<String> getAnnotations(String document, String separator) {
        String[] lines = document.trim().split("\n");
        ArrayList<String> array = new ArrayList<String>();
        for (String line : lines) {
            if (line.isBlank()) {
            } else {
                int lastChar = line.length();
                int sep = line.lastIndexOf(separator);
                String annotation = line.substring(sep + 1, lastChar).trim();
                array.add(annotation);
            }
        }
        return array;
    }

    public static String[] getLinesWithOutAnnotations(String document, String separator) {
        String[] lines = document.trim().split("\n");
        List<String> cleanLines = new ArrayList<>();
        for (String line : lines) {
            if (line.isBlank()) {
            } else {
                int lastChar = line.length();
                int sep = line.lastIndexOf(separator);
                String comment = line.substring(0, sep).trim();
                cleanLines.add(comment);
            }
        }
        return cleanLines.toArray(new String[0]);
    }
    public static String[] getLines(String document) {
        String[] lines = document.trim().split("\n");
        List<String> cleanLines = new ArrayList<>();
        for (String line : lines) {
            if (line.isBlank()) {
            } else {
                cleanLines.add(line);
            }
        }
        return cleanLines.toArray(new String[0]);
    }

    public static List<String> StringArrayToList(String[] stringArray){
        return Arrays.stream(stringArray).toList();
    }

    public static String ListToStringLines(List<String> documents) {
        String[] document = documents.toArray(new String[0]);
        String doc = "";
        for (String line : document) {
            doc += line +"\n" ;
        }
        return doc;
    }

    public String[] getWords(String document, language lang) {
        //Token = word
        String[] tokens = document.trim().split(getRegex(lang));
        List<String> cleanTokens = new ArrayList<>();
        for (String token : tokens) {
            if (token.length() > minTokenSize) {
                cleanTokens.add(token);
            }
        }
        return cleanTokens.toArray(new String[0]);
    }
}