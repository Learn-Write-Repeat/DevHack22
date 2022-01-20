
# Glass Type Prediction

![My image](https://images.unsplash.com/photo-1612189459328-13f9d03623b6?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1000&q=80)



## Goal

To Build a Machine Learning Algorithm to predict the type of the glass with highest accuracy.
## Dataset
The dataset for this project is taken from the UCI dataset website. Here is the link for the dataset, https://archive.ics.uci.edu/ml/datasets/glass+identification

## Solution

* Imported the libraries and dataset.
* Performed data preprocessing and renaming of columns and dropping of duplicated values.
* Performed Exploratory Data Analysis and Visualization with different columns of the dataset.
* Performed Feature Transformation on the columns of the dataset.
* Found out the correlation matrix.
* Found out the skewness in the dataset using kdeplot.
* Plotted the outliers in the dataset.
* For Model Building:  
        1. Split the Dataset into 80:20 ratio for training and testing  
        2. Applied Logistic Regression  
        3. Applied Random Forest Classifier  
        4. Applied Xgboost Classifier
        5. Applied Adaboost Classifier with Random Forest Classifier 
        
* Compared the Model Accuracy
* Exported the Model

## Stack Used

* Numpy
* Pandas
* Sklearn
* Seaborn
* Matplotlib

 

## Model Build
Random Forest Classifier is the best algorithm that has performed the best on the dataset which provided an accuracy of 81.39%

As Our Random Forest Classifier produced the best accuracy on test data, therefore we store our model for furthur testing. It is stored in folder /Model/rfc_glass_model.pkl



## Explanatory Video


## Contributor

Code Contributor: **Surya Sarkar**

[![LinkedIn](https://img.shields.io/badge/LinkedIn-0077B5?style=for-the-badge&logo=linkedin&logoColor=white)](https://www.linkedin.com/in/suryasarkar1/)

[![Github](https://img.shields.io/badge/GitHub-100000?style=for-the-badge&logo=github&logoColor=white)](https://github.com/Suryageeks)

