<h1 align="center"> Glass Type Prediction </h1>
<h2 align="center"> DevHack'22 </h2>
<h3> Problem Statement:</h3>
Build a machine learning model using the given dataset, which can be used to predict the type of glass.

<h3> Dataset:</h3>
UCI dataset: https://archive.ics.uci.edu/ml/datasets/glass+identification

<hr>

The dataset consists of 214 records in total. It contains 9 (independent) features, namely `Refractive Index (RI), Sodium (Na), Magnesium (Mg), Aluminium (Al), Silicon (Si), Potassium (K), Calcium (Ca), Barium (Ba), and Iron (Fe)` based on which we have to predict the type of glass (dependent feature). The glass can be classified into 7 types:  

1. building_windows_float_processed

2. building_windows_non_float_processed

3. vehicle_windows_float_processed

4. vehicle_windows_non_float_processed (none in this database)

5. containers

6. tableware

7. headlamps

Since, there is no record of type 4 glass in the given dataset, we are left with only 6 types.

<h3> Algorithms used: </h3>
Here, I have used mainly 3 classification algorithms and compared them to select the most suitable one for the case.

1. **Random Forest Classifier**

`Accuracy: 0.813953488372093`

`Confusion Matrix:`
<br><br>
<img src = "rf_cm.PNG">

2. **Decision Tree Classifier**

`Accuracy: 0.7209302325581395`

`Confusion Matrix:`
<br><br>
<img src = "dt_cm.PNG">

3. **K Nearest Neighbors Classifier**

`Accuracy: 0.6976744186046512`

`Confusion Matrix: `
<br><br>
<img src = "knn_cm.PNG">

<h3> Conclusion: </h3>

Looking at the accuracy and confusion matrix of each of the model, we can say that the **random forest classifier suits our case the best**.

<h3> Demo Video Link </h3>

[Click here](https://youtu.be/7u4NLCZtUk0)

Domain: Machine Learning

Submitted by: [Tanvi Bugdani](https://github.com/tanvi355)
