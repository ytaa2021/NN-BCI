package ml.classifiers;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ml.data.DataSet;
import ml.data.Example;
import ml.data.TextDataReader;
import ml.classifiers.OVAClassifier;



/**
 * OVA (One Vs. All) classifier that supports multiclass classification.
 * 
 * The classifier creates a factory of binary models, in order to classify multi class classifications.
 * Each model within the factory checks on one label - is it this label or not.
 * 
 * @author Ayelet Kleinerman, Colin Kirkpatrick
 *
 * THIS IS ASSIGNMENT 5 
 *
 */
public class OVAClassifier implements Classifier {
    private HashMap<Double, Classifier> classifiers = new HashMap<Double, Classifier>();
    private ClassifierFactory factory;

    /**
     * constuctor for OVAclassifier
     * @param factory is a ClassifierFactory object which decides that type of classifier to run (?) AYELET
     */
    public OVAClassifier(ClassifierFactory factory){
        this.factory = factory;
    }
    	
	@Override
	public void train(DataSet data) {
        // System.out.println("Start OVA Train");
        // mske new copy of dataset
        // Train one-versus-all classifiers using the factory
        // Add the trained classifiers to the list
        Set<Double> classes = data.getLabels();
        for (Double c : classes) {
            //System.out.println(c);
            Classifier classifier = factory.getClassifier();
            DataSet newSet = new DataSet(data.getFeatureMap());
            for (Example e : data.getData()){
                Example e2 = new Example(e);
                // System.out.println("Example: " + e2);
                newSet.addData(e2);
            }
           for (Example ex : newSet.getData()){
                if (ex.getLabel() == c) {
                    ex.setLabel(1);
                } else {
                    ex.setLabel(-1);
                }
            }
            classifiers.put(c, classifier);
            classifier.train(newSet);
            // code for question 7
            if (c == 10.0) {
                System.out.println(classifier.toString());
            }
        }	
	}

	@Override
	public double classify(Example example) {
        return calcLabelConfidence(example).get(0);
    }

    @Override
	public double confidence(Example example) {
        return calcLabelConfidence(example).get(1);
    }

    // HELPER FUNCTIONS

    /**
     * gets the classification and the condifence for a specific example
     * 
     * @param example the example we want to classify and get the confidence for
     * @return an arrayList of size 2, containing in location 0 the label, and in location 1 the confidence.
     */
    public ArrayList<Double> calcLabelConfidence(Example example) {
        double highestConfidenceLabel = -1.0;
        double smallestConfidenceLabel = -1.0;
        double highestConfidence = Integer.MIN_VALUE;
        double smallestConfidence = Integer.MAX_VALUE;
        ArrayList<Double> toReturn = new ArrayList<Double>(2);
        for (Map.Entry<Double, Classifier> classifier : classifiers.entrySet()){
            if (classifier.getValue().classify(example) > 0){
                if (classifier.getValue().confidence(example) > highestConfidence) {
                    highestConfidence = classifier.getValue().confidence(example);
                    highestConfidenceLabel = classifier.getKey();
                }
            } else {
                if (classifier.getValue().confidence(example) < smallestConfidence) {
                    smallestConfidence = classifier.getValue().confidence(example);
                    smallestConfidenceLabel = classifier.getKey();
                }
            }
        }
        if (highestConfidence != Integer.MIN_VALUE) {
            toReturn.add(highestConfidenceLabel);
            toReturn.add(highestConfidence);
            return toReturn;
        } else {
            toReturn.add(smallestConfidenceLabel);
            toReturn.add(smallestConfidence);
            return toReturn;
        }
    }
}
