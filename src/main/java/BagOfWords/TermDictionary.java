package BagOfWords;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class TermDictionary implements Dictionary {

    private final Map<String, Integer> indexedTerms;
    private int counter;


    public TermDictionary() {
        indexedTerms = new LinkedHashMap<>();
        counter = 0;
    }

    public void addTerm(String term) {
        if (!indexedTerms.containsKey(term)) {
            indexedTerms.put(term, counter++);
        }
    }

    public void addTerms(String[] terms) {
        for (String term : terms) {
            addTerm(term);
        }
    }

    @Override
    public Integer getTermIndex(String term) {
        return indexedTerms.get(term);
    }

    @Override
    public int getNumTerms() {
        return indexedTerms.size();
    }

    public String toString() {
        return indexedTerms.keySet().toString();
    }

    @Override
    public void AttributesFileArff(String path) {
        //This is method is optional if you need it
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(new File(path)));
            bw.write(getAttributesArff());
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getAttributesArff() {
        String output = "";
        // printing the elements of LinkedHashMap
        for (String key : indexedTerms.keySet())
            output += "@attribute " + key + " numeric" + "\n";
        return output;
    }
}

    
