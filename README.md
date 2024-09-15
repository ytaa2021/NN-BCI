Extension of this work: [https://docs.google.com/document/d/1NiozhT5gRuINy0ba0kMZTmEnKJd8Qv1gfYn__K-k64E/edit](https://docs.google.com/document/d/1NiozhT5gRuINy0ba0kMZTmEnKJd8Qv1gfYn__K-k64E/edit?usp=sharing)
##Intro
The first thing we did was we had to label our data from the EEG recordings. This meant going through the footage and assessing the timestamps of label changes over 30 minutes. From there we put that into an excel file and used MNE, a python library for neuroscience, to filter and preprocess the feature data from the EEG headset. From there, we built a python tiling algorithm that creates our labeled examples by combining the labels from a timestamp to the average corresponding EEG node (feature) values over a given period we set. We experimented with different period sizes of our data using EEG data sampling benchmarks to determine 1 second intervals would be the most optimal for providing a variety of examples with an ok sample rate. After that, we experimented with building different types of models and tweaking our optimizations and hyperparameters as outlined below. We attempted to implement our AVA model first, and then moved onto some sklearn models as well.
Different Neural network models worked on in order:
1 cnn layer(kernel_size = 3) & 1 dense layer(10 nodes)
1 cnn layer(kernel_size = 3) & 1 dense layer(64 nodes)
2 cnn layers(kernel_size_1 = 3)(kernel_size_2 = 1)
NN with 2 hidden layers(n_1 = 64, n_2 = 10)
RNN with Long-Short Term Memory Units(RNN Unit_1 number = 64 & Dense = 32)
Moreover, we tried with various combinations of features and labels. Among 4 original labels we have “N”, “L”, “R”, “Done”, we either used all four, or only “R” and “L”. When we tried to predict among all 4 labels instead of 2, we used One-Hot Encoding. Furthermore, we experimented with including or excluding other features(provided by the EEG system) that don’t represent electrodes’ recordings such as accX and accY.

##Results:
1 Layer CNN and 1 Layer NN(n=10) with 4 labels –> acc=0.44(there are 4 labels!)
1 Layer CNN and 1 Layer NN(n=10) with 2 labels –> acc = 0.65
2 Layer CNN(kernel_1 = 3, kernel_2 = 1) with 2 labels –> acc = 0.63
1 Layer CNN and 1 Layer NN(n = 64) with 2 labels –> acc = 0.73

##Problems
Difficulty adapting the AVA to the current dataset. Troubleshooting needed. Also we would like to evaluate a KNN model to see if it is effective on this dataset. We also would like to integrate our most accurate model into an LSL, so that we can have a live brain-computer interface. We anticipate this step being the most difficult because it is the most unfamiliar. Keeping that in mind, we will put forward an honest effort, but acknowledge we may not be able to complete it within the time constraints.

##Example Properties of models:
###1D CNN with 4 labels, Acc 0.48
 My data has 1500+ entries, 4 labels. Accuracy: 0.48
Input Layer:
Type: 1D Convolutional Layer
Filters: 32
Kernel Size: 3
Activation Function: ReLU
Input Shape: (number_of_features, 1) (resulting from reshaping)
Pooling Layer:
Type: 1D Max Pooling Layer
Pool Size: 2
Flatten Layer:
Type: Flatten Layer
Output Layer:
Type: Dense (Fully Connected) Layer
Number of Neurons: Equal to the number of classes (determined by the shape of y_train)
Activation Function: Softmax (for multi-class classification)
Compilation:
Optimizer: Adam
Loss Function: Categorical Crossentropy
Metrics: Accuracy
Training:
Epochs: 10
Batch Size: 32
Validation Split: 10% of the training data
Data Preprocessing:
Reshaping: Necessary for 1D Convolutional Layer
Label Encoding:
Label Encoder: Used to transform non-numeric class labels to numeric format for one-hot encoding
One-Hot Encoding: Utilized to_categorical from Keras to convert labels to one-hot encoded format
1D CNN & 1 Dense(10) with 2 labels, Acc 0.65
Removed N and Done labeled data entries. Removed one hot encoding. 
How to check what I want to learn

###2D CNN with 2 labels, Acc 0.63
First layer kernel size= 3, second layer kernel size = 1

###1 CNN & 1 Dense(64), Acc 0.73
The first layer is a Conv1D layer with 32 filters, a kernel size of 3, and ReLU activation.
The second layer is a MaxPooling1D layer with a pool size of 2, which reduces the spatial dimension.
The third layer is a Flatten layer, which flattens the output before passing it to the Dense layer.
The fourth layer is a Dense layer with 64 neurons and ReLU activation.
The fifth layer is the output layer with a single neuron and a sigmoid activation function for binary classification.
2 CNN’s directly 
Trial Error: 65%. Having strides as 1, helps
I've added strides=2 to the Conv1D layer. This means that the convolutional filter will move two steps at a time along the input sequence instead of the default single step. The strides parameter controls the step size for the convolution operation.
