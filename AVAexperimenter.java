

import javax.xml.crypto.Data;

import org.omg.CORBA.OBJ_ADAPTER;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.SocketPermission;
import java.util.ArrayList;
import java.util.HashMap;

import ml.classifiers.PerceptronClassifier;
//import ml.classifiers.RandomClassifier;
import ml.data.CrossValidationSet;
import ml.data.DataSet;
import ml.data.Example;
import ml.classifiers.AveragePerceptronClassifier;
import ml.classifiers.Classifier;
import ml.classifiers.ClassifierFactory;
//import ml.classifiers.ClassifierTimer;
import ml.classifiers.DecisionTreeClassifier;
//import ml.classifiers.DecisionTreeClassifier.DataMajority;
//import ml.classifiers.OVAClassifier;
import ml.classifiers.AVAClassifier;
//import ml.classifiers.ClassifierTimer;



/**
 * testing AVA on our brainwave dataset
 */
public class AVAexperimenter {
    
    static int majority;
    static int maxCount = 0;
    static String path = "/Users/colinkirkpatrick/CS158/NN-BCI/newData.csv";
    







    // /**
    //  * Method to calculate accuracy of AVA and OVA. 
    //  * @param train is the training data for the models
    //  * @param test is the testing daata for the models
    //  * @param factory is the factory the classifers should be created based on
    //  * @return arraylist with two doubles: first is the accuracy of OVA, second is the accuracy of AVA
    //  */
    // public static ArrayList<Double> calcAccs(DataSet train, DataSet test, ClassifierFactory factory) {
    //     // arraylist to temporarily hold accuracy for OVA, AVA
    //     System.out.println("training");
    //     ArrayList<Double> accs = new ArrayList<>();
        
    //     Double sum = 0.0;

    //     // create and train new classifiers (depth is set in factory)
    //     AVAClassifier avaClassifier = new AVAClassifier(factory);
        
    //     avaClassifier.train(train);
    //     //System.out.println("calling AVA train");
        


    //     accs.add(sum/test.getData().size());
    //     sum = 0.0;
    //     System.out.println(accs);
        
    //     // calc AVA accuracy, add to accs
    //     for (Example example : test.getData()) {
    //         if (example.getLabel() == avaClassifier.classify(example)) {
    //             sum++;
    //         }
    //     }
    //     accs.add(sum/test.getData().size());
        
        
    //     // returns an arraylist with two Doubles: OVA accuracy and AVA accuracy
    //     return accs;
    // }
    
     /**
     * Method to calculate accuracy of DT
     * @param train is the training data for the model
     * @param test is the testing daata for the model
     * @param factory is the factory the classifer should be created based on
     * @return double which represensts the accuracy of the DT
     */
    public static Double calcAcc(DataSet train, DataSet test, Classifier classifier) {
        System.out.println("training");
        Double eq = 0.0;
        classifier.train(train);
        System.out.println("here");
        for (Example example : test.getData()) {
            
            if (example.getLabel() == classifier.classify(example)) {
                System.out.println("guess: "+classifier.classify(example)+", correct: "+example.getLabel());
                eq++;
            }
        }
        return (eq/test.getData().size());
    }

    public static void main(String[] args) {
        DataSet brainData = new DataSet("/Users/colinkirkpatrick/CS158/NN-BCI/newData.csv",0);
        ClassifierFactory factory = new ClassifierFactory(ClassifierFactory.PERCEPTRON,3); 
        AVAClassifier classifier = new AVAClassifier(factory); 

        // Create validation sets
        int numSplits = 5;
        for (int i = 0; i < numSplits; i++){
            CrossValidationSet ValidationSet = new CrossValidationSet(brainData, numSplits, true);
            DataSet trainData = ValidationSet.getValidationSet(0).getTrain();
            DataSet testData= ValidationSet.getValidationSet(0).getTest();

            System.out.println("accuracy: "+calcAcc(trainData, testData, classifier));

        }
        
        


        // // Code for question 5
        //OVAClassifier ovaClassifier = new OVAClassifier(factory);
        //AVAClassifier avaClassifier = new AVAClassifier(factory);
        //System.out.println("Start OVA Timer");
        //ClassifierTimer.timeClassifier(ovaClassifier, wineDataset, 1);
        //System.out.println("Start AVA Timer");
        //ClassifierTimer.timeClassifier(avaClassifier, wineDataset, 1);

        //code for question 4
        // ten fold data splet
        int cv = 10;
        
    //     for(int i = 1; i < 4; i++){
    //         System.out.println("depth: "+ i);
    //         // factory for dt classifier with depth 1, 2 then 3
    //         //ClassifierFactory factory = new ClassifierFactory(0,i);

    //         // create 10 cross validation sets
    //         CrossValidationSet ValidationSet = new CrossValidationSet(wineDataset, cv);
            
    //         // create a new decision tree object, and set its depth
    //         //DecisionTreeClassifier dtClassifier = new DecisionTreeClassifier();
    //         //dtClassifier.setDepthLimit(14);
            
    //         // arraylists to hold accuracies for each cross validation set
    //         ArrayList<Double> OVAaccScores = new ArrayList<Double>();
    //         ArrayList<Double> AVAaccScores = new ArrayList<Double>();
    //         //ArrayList<Double> DTaccScores = new ArrayList<Double>();
            
    //         // iterate through validation sets
    //         for (int j = 0; j < ValidationSet.getNumSplits(); j++){
    //             System.out.println("set #: "+ j);
    //             // partition data into training and testing sets
    //             DataSet training = ValidationSet.getValidationSet(j).getTrain();
    //             DataSet testing = ValidationSet.getValidationSet(j).getTest();

    //             // calculate accuracy for OVA and AVA. Will return both doubles in an arraylist
    //             // claculate the dt accuracy scores and add them to the arrayList
    //             //System.out.println("calling calcAccs");
    //             ArrayList<Double> accs = calcAccs(training, testing, factory);
    //             //dtClassifier.train(training);
    //             //DTaccScores.add(calcDTAcc(training, testing, dtClassifier));

    //             // adds OVA and AVA accuracies to respective arraylists
    //             OVAaccScores.add(accs.get(0));
    //             AVAaccScores.add(accs.get(1));

    //         }
    //     System.out.println("OVAaccScores: " + OVAaccScores);
    //     System.out.println("AVAaccScores: " + AVAaccScores);
    //    //System.out.println("DTaccScores: " + DTaccScores);
    //     }
        
        // // code for question 1
        // // HashMap<Integer, Integer> wineCounts = findMajorityLabel(path);
        // int count;
        // int maxCount = 0;
        // for (HashMap.Entry<Integer, Integer> entry : wineCounts.entrySet()) {
        //     count = entry.getValue();
        //     if (count > maxCount) {
        //         maxCount = count;
        //         majority = entry.getKey();
        //     }
        // }
        // System.out.println("most commone wine is: " + wineDataset.getFeatureMap().get(majority) + " with " + maxCount + " occurances");


    }
}
