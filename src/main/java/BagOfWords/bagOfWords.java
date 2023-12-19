package BagOfWords;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import BagOfWords.Language.language;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealMatrixFormat;

import static BagOfWords.SimpleTokenizer.ListToStringLines;
import static BagOfWords.SimpleTokenizer.StringArrayToList;
import static BagOfWords.SimpleTokenizer.getAnnotations;
import static BagOfWords.SimpleTokenizer.getLines;
import static BagOfWords.SimpleTokenizer.getLinesWithOutAnnotations;

public class bagOfWords {
    private String separator;
    private Path filePath;
    private final Charset charset = StandardCharsets.UTF_8;
    /* create a dictionary of all terms */
    private TermDictionary termDictionary;
    /* need a basic tokenizer to parse text */
    private SimpleTokenizer tokenizer;
    private Vectorizer vectorizer;
    private RealMatrix counts;
    private List<String> linesWithOutAnnotations, cleanLines;
    private List<String> document;
    private ArrayList<String> Annotations;
    private language lang;

    public bagOfWords(Path filePath, SimpleTokenizer tokenizer, language lang, String sep) {
        this.lang = lang;
        this.filePath = filePath;
        this.tokenizer = tokenizer;
        this.separator = sep;
        try {
            document = Files.readAllLines(this.filePath, charset);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String lines = ListToStringLines(document);
        linesWithOutAnnotations = StringArrayToList(getLinesWithOutAnnotations(lines, separator));
        Annotations = getAnnotations(lines, separator);
        termDictionary = CreateDictionary();
        /* create of matrix of word counts for each sentence */
        vectorizer = new Vectorizer(termDictionary, this.tokenizer, false, this.lang);
        counts = vectorizer.getCountMatrix(linesWithOutAnnotations);
    }

    public bagOfWords(Path filePath, SimpleTokenizer tokenizer, language lang, String sep, bagOfWords OriginalBag) {
        this.lang = lang;
        this.filePath = filePath;
        this.tokenizer = tokenizer;
        this.separator = sep;
        try {
            document = Files.readAllLines(this.filePath, charset);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String lines = ListToStringLines(document);
        cleanLines = StringArrayToList(getLines(lines));
        Annotations = OriginalBag.getAnnotationsObject();
        termDictionary = OriginalBag.getTermDictionary();
        /* create of matrix of word counts for each sentence */
        vectorizer = new Vectorizer(termDictionary, this.tokenizer, false, this.lang);
        counts = vectorizer.getCountMatrix(cleanLines);
    }

    public static bagOfWords getBagOfPrediction(File fileName, language lang, bagOfWords OriginalBag) {
        File file = fileName;
        bagOfWords bow = new bagOfWords(file.toPath(), new SimpleTokenizer(1), lang, ",", OriginalBag);
        return bow;
    }

    private TermDictionary CreateDictionary() {
        TermDictionary termDictionary = new TermDictionary();
        /* add all terms in sentiment dataset to dictionary */
        for (String line : linesWithOutAnnotations) {
            String[] tokens = tokenizer.getWords(line, lang);
            termDictionary.addTerms(tokens);
        }
        return termDictionary;
    }

    public String getBinaryData() {
        String bianryData = "";
        for (int i = 0; i < counts.getRowDimension(); i++) {
            RealMatrixFormat matrixFormat = new RealMatrixFormat("", "", "", "," + Annotations.get(i), "", ",");
            bianryData += matrixFormat.format(counts.getRowMatrix(i)) + "\n";
        }
        return bianryData;
    }

    public String getBinaryDataForPredict() {
        String bianryData = "";
        for (int i = 0; i < counts.getRowDimension(); i++) {
            RealMatrixFormat matrixFormat = new RealMatrixFormat("", "", "", "," + "?", "", ",");
            bianryData += matrixFormat.format(counts.getRowMatrix(i)) + "\n";
        }
        return bianryData;
    }

    public String getAttributesArff() {
        return termDictionary.getAttributesArff();
    }

    public void createFileArff(String relationName, File outputFile) {
        String classAttribute = getClassesAsStringForArff();
        String relation = "@relation " + relationName + "\n\n";
        String BinaryData = "@data\n" + getBinaryData();
        String Attributes = getAttributesArff() + classAttribute + "\n\n";

        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile));
            bw.write(relation + Attributes + BinaryData);
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String GetFileArffAsString(String relationName) {
        String classAttribute = getClassesAsStringForArff();
        String relation = "@relation " + relationName + "\n\n";
        String BinaryData = "@data\n" + getBinaryDataForPredict();
        String Attributes = getAttributesArff() + classAttribute + "\n\n";
        return relation + Attributes + BinaryData;
    }

    public String getClassesAsStringForArff() {
        // @attribute class string you could change it after the creation
        ArrayList<String> classes = getClasses();
        int size = classes.size();
        String classesToString = "@attribute class {";
        for (int i = 0; i < size; i++) {
            if (i == size - 1)
                classesToString += classes.get(i) + "}";
            else
                classesToString += classes.get(i) + ",";
        }
        return classesToString;
    }

    public ArrayList<String> getClasses() {
        ArrayList<String> annotations = new ArrayList<>();
        for (String anno : Annotations) {
            if (!annotations.contains(anno))
                annotations.add(anno);
        }
        return annotations;
    }

    public void CreateAttributesFileArff(String pathname) {
        //This is method is optional if you need it
        termDictionary.AttributesFileArff(pathname);
    }

    public TermDictionary getTermDictionary() {
        return termDictionary;
    }

    public ArrayList<String> getAnnotationsObject() {
        return Annotations;
    }

    public List<String> getcleanLines() {
        return cleanLines;
    }
}
