package ml.data;

import java.util.Collections;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.lang.Math;

/**
 * A class to represent an ExampleNormalizer
 * 
 * @author Ayelet Kleinerman & Colin Kirkpatrick
 */
public class ExampleNormalizer implements DataPreprocessor {
    /**
	 * Preprocess the training data
	 * 
	 * @param train
	 */
	public void preprocessTrain(DataSet train){
        
        // hashmap of features
        HashMap<Integer, String> features = train.getFeatureMap();
        
        // iterate through examples
        for (Example example : train.getData()) {
            
            // helper variables
            Double currentLength;
            double sumOfSquares = 0;
            
            // loops through features in example, squares and adds each value
            for (Integer feature : features.keySet()) {
                sumOfSquares += example.getFeature(feature) * example.getFeature(feature);
            }

            // finds euclidian distance of each example and normalizes 
            currentLength = Math.sqrt(sumOfSquares);
            for (Integer feature : features.keySet()) {
                example.setFeature(feature, example.getFeature(feature)/currentLength);
            }
        }
    }	
	/**
	 * Preprocess the testing data
	 * SAME AS ABOVE!
	 * @param test
	 */
	public void preprocessTest(DataSet test){
        HashMap<Integer, String> features = test.getFeatureMap();
        for (Example example : test.getData()) {
            Double currentLength;
            double sumOfSquares = 0;
            
            for (Integer feature : features.keySet()) {
                sumOfSquares += example.getFeature(feature) * example.getFeature(feature);
            }
            currentLength = Math.sqrt(sumOfSquares);
            for (Integer feature : features.keySet()) {
                example.setFeature(feature, example.getFeature(feature)/currentLength);
            }
        }
    }	
}