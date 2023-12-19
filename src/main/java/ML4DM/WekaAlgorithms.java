package ML4DM;

import BagOfWords.Language;
import BagOfWords.SimpleTokenizer;
import BagOfWords.TermDictionary;
import BagOfWords.bagOfWords;
import weka.attributeSelection.CfsSubsetEval;
import weka.attributeSelection.GreedyStepwise;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.functions.SMO;
import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.Filter;
import weka.filters.supervised.attribute.AttributeSelection;
import weka.filters.unsupervised.instance.NonSparseToSparse;
import weka.filters.unsupervised.instance.RemovePercentage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;

public class WekaAlgorithms {

//    public static void testOfweka() throws Exception {
//        SMO smo = new SMO();
//
//        Instances train = new Instances(new BufferedReader(new FileReader("Train.arff")));
//
//        int lastIndex = train.numAttributes() - 1;
//        train.setClassIndex(lastIndex);
//
//        Instances test = new Instances(new BufferedReader(new FileReader("Test.arff")));
//
//        test.setClassIndex(lastIndex);
//        smo.buildClassifier(train);
//
//        Evaluation eval = new Evaluation(train);
//        eval.evaluateModel(smo, test);
//        System.out.println(eval.toSummaryString());
//        System.out.println(eval.toClassDetailsString());
//        System.out.println(eval.toMatrixString());
//    }
//
//    public static void convertsInstancesToSparse(String srcFileLocation, String newFileLocation) {
//        //This method converts all incoming instances into sparse format.
//
//        try {
//            //Load dataset
//            DataSource source = new DataSource(srcFileLocation);
//            Instances dataset = source.getDataSet();
//            //Create NonSparseToSparse object to save in sparse ARFF format
//            NonSparseToSparse nts = new NonSparseToSparse();
//            //specify the dataset
//            nts.setInputFormat(dataset);
//            //apply
//            Instances newData = Filter.useFilter(dataset, nts);
//            //save
//            ArffSaver saver = new ArffSaver();
//            saver.setInstances(newData);
//            saver.setFile(new File(newFileLocation));
//            saver.writeBatch();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//
//    }
//
//    public static void removeSomeWords() throws Exception {
//        DataSource source = new DataSource("Train.arff");
//        Instances dataset = source.getDataSet();
//        //create AttributeSelection object
//        AttributeSelection filter = new AttributeSelection();
//        //create evaluator and search algorithm objects
//        CfsSubsetEval eval = new CfsSubsetEval();
//        GreedyStepwise search = new GreedyStepwise();
//        //set the algorithm to search backward
//        search.setSearchBackwards(true);
//        //set the filter to use the evaluator and search algorithm
//        filter.setEvaluator(eval);
//        filter.setSearch(search);
//        //specify the Q_aet
//        filter.setInputFormat(dataset);
//        //apply
//        Instances newData = Filter.useFilter(dataset, filter);
//        //save
//        ArffSaver saver = new ArffSaver();
//        saver.setInstances(newData);
//        System.out.println(newData.toSummaryString());
//        saver.setFile(new File("new.arff"));
//        saver.writeBatch();
//    }
//
//    public static void ArabicStemmer() {
//        ArabicStemmerKhoja ask = new ArabicStemmerKhoja();
//        System.out.println(ask.stem("البلدان"));
//
//    }

    public static Instances RemoveWithPercentage(String FileName, double Percentage,boolean invertSelection) throws Exception {
        Instances input = new Instances(new BufferedReader(new FileReader(FileName)));
        RemovePercentage rp = new RemovePercentage();
        rp.setPercentage(Percentage);
        rp.setInvertSelection(invertSelection);
        rp.setInputFormat(input);
        Instances incs = Filter.useFilter(input, rp);
        return incs;
    }

    public static String ArffToString(bagOfWords bow) {
        return bow.GetFileArffAsString("test");
    }

}
