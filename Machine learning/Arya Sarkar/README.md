
# Glass Type Prediction

![My image](https://ak.picdn.net/shutterstock/videos/14522083/thumb/1.jpg)



## AIM

To Develop a machine learning model that predict the type of glass with highest accuracy.
## Dataset
The dataset for this project is taken from the UCI dataset website. Here is the link for the dataset, https://archive.ics.uci.edu/ml/datasets/glass+identification

## Solution

* Imported the libraries and dataset.
* Performed data preprocessing and renaming of columns.
* Performed Exploratory Data Analysis and Visualization with different elements of the dataset.
* Find the correlation of variables with Refractive Index.
* Plotting variables to find skewness in the dataset.
* Plotted the outliers in the dataset.
* For Model Building:  
        1. Split the Dataset into 70:30 ratio for training and testing  
        2. Applied Logistic Regression  
        3. Applied Random Forest Classifier  
        4. Applied K-Nearest Neighbors  
        5. Applied MLP Classifier  
        6. Applied XGBoost Classifier  
* Show Model Accuracy Overview  
* Exporting the Model

## Stack Used

* Numpy
* Pandas
* Sklearn
* Seaborn
* Matplotlib
* Scipy
 

## Model Build

XGBoost Classifier was the best algorithm that performed best on the dataset which produce an accuracy of around 82%.

As Our XGBoost Classifier produced the best accuracy on test data, therefore we store our model for furthur inferencing. It is stored in folder /Model/xgboost_glass_pred.pkl



## Explanatory Video
 <a href="https://youtu.be/gGDnMJYIJWM8">Click Here!</a>
## Contributor

Code Contributor: **Arya Sarkar**

[![LinkedIn](https://img.shields.io/badge/LinkedIn-0077B5?style=for-the-badge&logo=linkedin&logoColor=white)](https://www.linkedin.com/in/aryasarkar03/)

[![Github](https://img.shields.io/badge/GitHub-100000?style=for-the-badge&logo=github&logoColor=white)](https://github.com/aryacodez)

