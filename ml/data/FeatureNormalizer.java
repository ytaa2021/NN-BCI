package ml.data;

import java.util.Collections;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * A class to represent a Feature Normalizer
 * 
 * @author Ayelet Kleinerman & Colin Kirkpatrick
 */
public class FeatureNormalizer implements DataPreprocessor {
	
	// hashmaps to store means and stds for each feature
	HashMap<Integer, Double> means;
	HashMap<Integer, Double> stds;

	// creates hashmaps
    public FeatureNormalizer() {
        means = new HashMap<>();
        stds = new HashMap<>();
    }

    /**
	 * Preprocess the training data
	 * 
	 * @param train
	 */
	public void preprocessTrain(DataSet train){
		
		// gets hashmap of features
		HashMap<Integer, String> features = train.getFeatureMap();
		
		// iterates through features
        for (Integer feature : features.keySet()) {
			
			// iterates through examples to get sum of all values in a particular feature
			Double sumValues = 0.0;
            for (Example example : train.getData()) {
				sumValues += example.getFeature(feature);
            }

			// calculates mean value in feature and stores
			Double mean = sumValues/features.size();
			means.put(feature, mean);

			// holder for std
			Double std = 0.0;

			// iterates through examples to get std of all values in a particular feature
			for (Example example : train.getData()) {
				example.setFeature(feature, example.getFeature(feature)-mean);
				std += (example.getFeature(feature)) * (example.getFeature(feature));
            }

			// calculates and stores std 
			std = Math.sqrt(std/(features.size()));
			stds.put(feature, std);
			for (Example example : train.getData()) {
				example.setFeature(feature, example.getFeature(feature)/stds.get(feature));
			}
        } 
    }	
	/**
	 * Preprocess the testing data
	 * SAME AS ABOVE
	 * @param test
	 */
	public void preprocessTest(DataSet test){
        HashMap<Integer, String> features = test.getFeatureMap();
		
        for (Integer feature : features.keySet()) {
			// Double sumValues = 0.0;
            // for (Example example : test.getData()) {
			// 	sumValues += example.getFeature(feature);
            // }
			// Double mean = sumValues/features.size();
			// means.put(feature, mean);
            // //Double currentLength = Math.sqrt(sumOfSquares);
            // //feature.put(feature, example.getFeature(feature)/currentLength);
			// Double std = 0.0;
			for (Example example : test.getData()) {
				//System.out.println("featureNum: "+feature);
				//System.out.println("feature: "+example.getFeature(feature));
				//System.out.println("means.get(feature): "+means.get(feature));
				example.setFeature(feature, example.getFeature(feature)-means.get(feature));
				//std += (example.getFeature(feature)) * (example.getFeature(feature));
            }
			//std = Math.sqrt(std/(features.size()));
			//stds.put(feature, std);
			for (Example example : test.getData()) {
				example.setFeature(feature, example.getFeature(feature)/stds.get(feature));
			}
        } 
}

}