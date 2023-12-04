//Ulas Ayyilmaz Assignment 5
package ml.classifiers;

import ml.data.*;
import java.util.*;

/**
 * AVA Classifier Class
 */
public class AVAClassifier implements Classifier {
    
    ClassifierFactory factory;
    //AVA Hashmap
    private HashMap<ArrayList<Integer>, Classifier> AvaHash = new HashMap<ArrayList<Integer>, Classifier>();    
    //Score list
    private ArrayList<Integer> Scorelist = new ArrayList<>(20);
    //num of labels
    private int labelsize = 3;
    
    /**
     * Constructor
     * @param factory
     */
    public AVAClassifier(ClassifierFactory factory){
        this.factory = factory;
    }


    /**
	 * Train this classifier based on the data set
	 * 
	 * @param data
	 */
	public void train(DataSet data){
        //loops for i and j n(n-1)/2 times
        for (int i = 0; i < 3; i++){
            for (int j = i+1; j < 3; j++){
                
                //creates new classifier and dataset
                Classifier myclassifier = factory.getClassifier();
                DataSet newData = new DataSet(data.getFeatureMap());
                
                //loops dataset, changes labels so that they are 1.0 or -1.0
                for (int k = 0; k < data.getData().size(); k++){
                    //not sure if copying correctly
                    Example copy = new Example(data.getData().get(k));
                    //System.out.println("example: "+copy.toString());
                    if (copy.getLabel() == (double)i){
                        copy.setLabel(1.0);
                        newData.addData(copy);
                    }
                    else if (copy.getLabel() == (double)j){
                        copy.setLabel(-1.0);
                        newData.addData(copy);
                    }
                }

                //initializes list to remember which indices
                ArrayList<Integer> mlist = new ArrayList<>();
                mlist.add(i);
                mlist.add(j);
                AvaHash.put(mlist, myclassifier);
                myclassifier.train(newData);
            }
        }

    }
	
	/**
	 * Classify the example.  Should only be called *after* train has been called.
	 * 
	 * @param example
	 * @return the class label predicted by the classifier for this example
	 */
	public double classify(Example example){
        //fill scorelist with all 0's
        for (int i = 0; i < labelsize; i++){
            Scorelist.add(0);
        }
        
        //AVAlist has all the x,y combinations of two labels starting from 
        //0,1 (0th idx)-> 0,19 (18th idx) -> 1,2(19th idx)
        for (int i = 0; i < labelsize; i++){
            for (int j = i+1; j < labelsize; j++){
                
                //initialize i and j as indices
                ArrayList<Integer> mylist = new ArrayList<>();
                mylist.add(i);
                mylist.add(j);
                //System.out.println(mylist);
                
                //increases or decreases confidence scores depending on if prediction is positive or negative
                if (AvaHash.get(mylist).classify(example) > 0){
                    Scorelist.set(mylist.get(0), Scorelist.get(mylist.get(0))+1);
                    Scorelist.set(mylist.get(1), Scorelist.get(mylist.get(1))-1);
                }
                else{
                    Scorelist.set(mylist.get(0), Scorelist.get(mylist.get(0))-1);
                    Scorelist.set((mylist.get(1)), Scorelist.get(mylist.get(1))+1);
                }
            }
        }
        
        double max = Double.MIN_VALUE;
        double classifylabel = 0.0;
        
        //finds the maximum scored label
        for (int m = 0; m < Scorelist.size(); m++){
            if (Scorelist.get(m) > max){
                max = Scorelist.get(m);
                classifylabel = m;
            }
        }
        Scorelist.clear();
        
        return classifylabel;
    }
	
    /**
     * @param example
     * @returns confidence
     */
	public double confidence(Example example){
        return 0.0;
    }
}
