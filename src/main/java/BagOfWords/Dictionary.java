package BagOfWords;
import java.io.IOException;

public interface Dictionary {
    Integer getTermIndex(String term);
    int getNumTerms();
	void AttributesFileArff(String path) throws IOException;
}