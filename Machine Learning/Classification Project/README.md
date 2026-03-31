# Classification Project – Company Bankruptcy Prediction

**Course:** Machine Learning  
**University:** University of Macedonia, Department of Applied Informatics  
**Semester:** 7th semester, Academic Year 2023 – 2024  
**Author:** Apostolos Sinioris (ics24123)  
**Supervisor:** Eftychios Protopapadakis

---

## Overview

This project addresses a **binary classification** problem in Machine Learning: predicting which companies are likely to go bankrupt based on financial indicators. Companies are classified into two categories:

- **Class 0 – Healthy**: The company is financially stable.
- **Class 1 – Bankrupt**: The company has declared or is likely to declare bankruptcy.

---

## Dataset

The dataset is provided in `Dataset2Use_Assignment1.xlsx` (located in `src/`). It contains **10,716 records** (companies) and **13 columns**:

| Columns | Description |
|---------|-------------|
| A – H   | Financial performance indicators (continuous values) |
| I – K   | Binary activity indicators |
| L (index 11) | **Status**: 1 = Healthy, 2 = Bankrupt (remapped to 0 and 1) |
| M (index 12) | Year of observation |

The dataset is heavily **imbalanced** – healthy companies significantly outnumber bankrupt ones.

---

## Methodology

The project is implemented in Python using **Google Colab**. The main steps are:

1. **Data Loading** – Reading the Excel file using `pandas`.
2. **Exploratory Analysis** – Visualizing healthy vs. bankrupt companies per year (Figure 1) and statistical summaries (min/mean/max) per feature for each class (Figure 2a, 2b).
3. **Missing Value Handling** – Detecting and removing rows with NaN values.
4. **Normalization** – Scaling all feature values to the range [0, 1] using `MinMaxScaler`.
5. **Stratified K-Fold (4 folds)** – Splitting data into 4 balanced folds ensuring the same class ratio in each fold.
6. **Undersampling** – Reducing the number of healthy companies in each training set to achieve a 3:1 (healthy:bankrupt) ratio.
7. **Model Training & Evaluation** – Training and evaluating 8 classifiers across all folds.

---

## Models

| Model | Library |
|-------|---------|
| Linear Discriminant Analysis (LDA) | `sklearn.discriminant_analysis` |
| Logistic Regression | `sklearn.linear_model` |
| Decision Tree | `sklearn.tree` |
| Random Forest | `sklearn.ensemble` |
| K-Nearest Neighbors (KNN) | `sklearn.neighbors` |
| Naïve Bayes | `sklearn.naive_bayes` |
| Support Vector Machine (SVM) | `sklearn.svm` |
| **Gradient Boosting** *(best performer)* | `sklearn.ensemble` |

---

## Evaluation Metrics

Each model is evaluated on both the **training set** (balanced) and the **test set** (unbalanced) using:

- **Accuracy**
- **Precision**
- **Recall**
- **F1 Score**
- **ROC-AUC**

Confusion matrices are generated for every model and fold.

---

## Results

Results are saved in:
- `out/balancedDataOutcomes.csv` / `out/balancedDataOutcomes.xlsx` – Full metrics table for all models, folds and sets.
- `out/confusion matrixes/` – Confusion matrix plots.

The **Gradient Boosting** classifier achieved the best overall performance, attaining the highest Recall score among all models – crucial in a bankruptcy detection scenario where correctly identifying bankrupt companies is the primary objective.

---

## Project Structure

```
Classification Project/
├── code/
│   ├── Classification.ipynb    # Jupyter Notebook (Google Colab)
│   └── classification.py       # Exported Python script
├── out/
│   ├── balancedDataOutcomes.csv
│   ├── balancedDataOutcomes.xlsx
│   ├── F1.png
│   ├── Figure1.png
│   ├── Figure2a.png
│   ├── Figure2b.png
│   ├── Pivot Table.png
│   ├── Recall-TNR.png
│   └── confusion matrixes/
├── src/
│   └── Dataset2Use_Assignment1.xlsx
├── 1η εργασία Classification.pdf   # Assignment description
├── Report.pdf                       # Full project report
└── README.md
```

---

## Technologies Used

- **Python** (Google Colab)
- **pandas** – data loading and manipulation
- **scikit-learn** – models, preprocessing, cross-validation, metrics
- **matplotlib / seaborn** – data visualization
