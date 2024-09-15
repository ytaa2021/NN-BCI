Extension of this work: [https://docs.google.com/document/d/1NiozhT5gRuINy0ba0kMZTmEnKJd8Qv1gfYn__K-k64E/edit](https://docs.google.com/document/d/1NiozhT5gRuINy0ba0kMZTmEnKJd8Qv1gfYn__K-k64E/edit?usp=sharing)
Brain Computer Interface: Processing EEG Data with Machine Learning

## 1. Intro	
Our goal for this project was to build a Brain-Computer-Interface (BCI) from the ground up by employing machine learning to interpret live inputs from an electroencephalogram (EEG) device. We underwent the process of designing an experimental setup for data-collection, data-labelling, data-preprocessing, data-analysis and data-cleaning, model design, model evaluation and selection, and the application and interface of the model for our BCI. Unfortunately, we are unable to demo the live usage of our completed BCI due to issues with the device’s software that have not yet been resolved with the supplier.

## 2. Setup
### 2.1 Data Collection
We produced and collected our own experimental data. Ulas wore a portable electroencephalogram (EEG), and was given instructions to continuously move his right arm, continuously move his left arm, or move neither arm. At seven different nodes, the EEG recorded brain waves, recording values per node to millisecond precision. We collected around 30 minutes worth of data. 

### 2.2 Data Processing
We first had to record the timing of label changes, and process the raw EEG data (VHDR files) using the MNE Python library, which included applying low pass, high pass and notch filtering to reduce signal noise. We then had to merge the continous feature data with the discrete, non-uniformly spaced labels timestamps, converting them into uniform examples. This was done through a tiling algorithm to combine the average value for the 7 EEG features over a chosen interval of time an example would represent with the correct label. We ended up selecting 1-second periods as optimal based on EEG sampling benchmarks, resulting in approximately 1600 usable examples. The figure to the right is a matrix of graphs that model the activation of feature i vs j.
Finally, after visualizing the data in multiple scatterplots, we identified significant outliers and applied a filter using a z-score threshold of 3 to remove those instances.
### 2.3 Machine Learning 
We experimented with a number of machine learning models to find which one could achieve the highest testing accuracy on our dataset. We implemented and optimized our own perceptron model, a KNN algorithm, a dense neural network, a recurrent neural network, and a convolutional neural network for this application. We used Sklearn for our NNs, and Pandas, Numpy, Seaborne and MatPlotLib as relevant python packages. 
Our models had the following parameters: 
1 CNN layer(kernel_size = 3) & 1 dense layer(10 nodes) 
1 CNN layer(kernel_size = 3) & 1 dense layer(64 nodes)
2 CNN layers(kernel_size_1 = 3)(kernel_size_2 = 1)
NN with 2 hidden layers(n_1 = 64, n_2 = 10)
RNN with Long-Short Term Memory Units(RNN Unit_1 number = 64 & Dense = 32)
KNN (k = 6)
For all models including CNN’s we tweaked the kernel values spanning 1 to 4 and pool size spanning 1 to 3. The value selections for these parameters are based on trial and error. The models used the adam optimizer which adapts the learning rates using gradients. 
For all models which included Dense Neural Network Layer, we experimented with n=5, n=10, n=20, n=64 as different numbers of neurons. Lastly, we used 10 cross-validations to prevent overfitting, with the number of epochs=10 and the number of examples in each batch = 32. 
2.4 Lab Streaming Layer (LSL)
We wrote the code for the BCI to function using the pyLSL package and our previously implemented models. Unfortunately were never able to pick up any streams over the network despite many attempts and hours of effort using several tools, operating systems, Wi-Fi networks, and broadcasting devices with no avail.

## 3. Results
### 3.1 Choosing a Model
The perceptron model was unsuccessful, yielding 50% accuracy on 2-labels. The neural networks all yielded at least 61% accuracy with our best model, a CNN with one convolutional and one dense neuronal layer, yielding 73% accuracy under sklearn cross-validations. We selected this model for our BCI implementation, featuring a kernel value of 3, and 64 neurons in the dense-network layer. All examples that the model is trained on are split between two classes, L or R, with the largest class representing only 65% of the data.

## 5. Conclusion
The project’s core challenge was to interpret brain signals using computational models, which requires a deep understanding of how machine learning models work and when to apply which models as well as an understanding of biological phenomena and interpreting EEG data.

The Perceptron performed no better than random chance due to it’s inability to capture the non-linear patterns typical in EEG data, as demonstrated in the feature-matrix figure above. The mediocre performance of the RNN, which consisted of LSTM neurons, is due to its being less applicable for our application/dataset. RNNs are generally good at identifying trends in time series data, but they usually need larger datasets and try to capture long-term patterns. Because our data does not capture any temporal patterns, but still requires some kind of temporal consideration, the CNN was especially effective. It ignores the unrelated connections that other models try and make, while still including the necessary temporal considerations due to the nature of our examples. These intervals on their own might not fully capture "a specific motion" in a consistent manner due to a motion’s duration having no impact on how the data was tiled. Therefore, the CNN’s ability to capture temporally adjacent information when understanding an example provided it with a competitive edge over the other NNs. By getting a more general idea across a motion as a whole it, proved to align best with the BCI application both in theory and in practice.

Unfortunately, due to the aforementioned streaming issues, we did not end up with a demonstrable real-time brain-computer interface. However, we did create our own data, find reasonable machine learning models and tweak various hyperparameters to maximize models’ accuracies. We proved, in concept, and wrote the code for real-time classification of left hand versus right hand movement, achieving our initial goal. This project serves as a stepping stone for developing more complex BCI’s using Neural Networks from scratch. Future steps could include the use of more advanced EEG instruments, the collection of a greater volume and better quality data, experimenting further with tweaking NN hyperparameters, applying boosting and bagging techniques, engineering more relevant data, and the successful implementation of a live-classifying component. This project opens door to future possible studies including prosthetic development and artificial speech production. 
