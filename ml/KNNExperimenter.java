package ml;
import java.util.ArrayList;

import javax.xml.crypto.Data;
import ml.classifiers.Classifier;
import ml.classifiers.KNNClassifier;
import ml.data.CrossValidationSet;
import ml.data.DataSet;
import ml.data.Example;
import ml.classifiers.AveragePerceptronClassifier;
import ml.data.ExampleNormalizer;
import ml.data.FeatureNormalizer;

public class KNNExperimenter {
    /**
     * This function calculates the accuracy of a given Classifer on testing data predictions.
     * 
     * @param train
     * @param test
     * @param classifier
     * @return double, representing the accuracy
     */
    public static Double calcAcc(DataSet train, DataSet test, Classifier classifier) {
        Double eq = 0.0;
        classifier.train(train);
        for (Example example : test.getData()) {
            if (example.getLabel() == classifier.classify(example)) {
                eq++;
            }
        }
        return (eq/test.getData().size());
    }
    
    /**
     * 
     * @param args
     */
    public static void main(String[] args) {
        
        //created instances
        ExampleNormalizer LengthNorm = new ExampleNormalizer();
        FeatureNormalizer FeatureNorm = new FeatureNormalizer();
        
        //getting data
        DataSet dataset = new DataSet("/Users/colinkirkpatrick/CS158/NN-BCI/KNNdata.csv",0);
        

        int cv = 10;

        for(int w = 1; w < 30; w++){
                
            // DataSetSplit all = dataset.split(0.8);
            CrossValidationSet ValidationSet = new CrossValidationSet(dataset, cv);

        
    
            // arraylists to hold accuracy data
            //ArrayList<Double> PerAccScores = new ArrayList<Double>();
            ArrayList<Double> KNNAccScores = new ArrayList<Double>();
        
            // loop through each of the ten splits
            for (int i = 0; i < cv; i++) {
                //Double perSum = 0.0;
                Double KNNSum = 0.0;

                // test and train
                DataSet trainData = ValidationSet.getValidationSet(i).getTrain();
                DataSet testData= ValidationSet.getValidationSet(i).getTest();

                LengthNorm.preprocessTest(testData);
                LengthNorm.preprocessTrain(trainData);
                //FeatureNorm.preprocessTrain(trainData);
                //FeatureNorm.preprocessTest(testData);
            
            
            
                // run 30 times each, count the sum of accuracies 
                for (int j = 0; j < 2; j++) {
                    // sum accuracy for average calculation
                    //perSum = perSum + calcAcc(trainData, testData, new AveragePerceptronClassifier());
                    KNNClassifier classifier = new KNNClassifier();
                    classifier.setK(w);
                    KNNSum = KNNSum + calcAcc(trainData, testData, classifier);
                } 

                // calculate average accuracies and add to results arraylist
                //Double finalPerAcc = perSum/100;
                //System.out.println(KNNSum);
                Double finalKNNAcc = KNNSum/2;
                //System.out.println("run accuracy: "+finalKNNAcc);

                // add average accuracy to report for printing
                //PerAccScores.add(finalPerAcc);
                KNNAccScores.add(finalKNNAcc);
            } 

            // print accuracies
            //System.out.println("Per Acc Scores List (length): " + PerAccScores);
            //System.out.println("KNN Acc Scores List (length):" + KNNAccScores);

            // numbers to hold average accuracy
            //Double PerFinalAccSum = 0.0;
            Double KNNFinalAccSum = 0.0;

            //
            // for (Double num : PerAccScores) {
            //     PerFinalAccSum += num;
            // }
            for (Double num : KNNAccScores) {
                KNNFinalAccSum += num;
            }

            // calc average
            //Double PerFinalAcc = PerFinalAccSum/cv;
            Double KNNFinalAcc = KNNFinalAccSum/cv;
            //System.out.println("Per Final Acc 1: " + PerFinalAcc);
            System.out.println(KNNFinalAcc);

        }



        
    }
}

    
        
