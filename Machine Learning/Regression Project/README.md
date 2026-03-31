# Regression Project – Diabetes Disease Progression Prediction

**Course:** Machine Learning  
**University:** University of Macedonia, Department of Applied Informatics  
**Semester:** 7th semester, Academic Year 2025 – 2026  
**Author:** Apostolos Sinioris (ics24123)  
**Supervisor:** Eftychios Protopapadakis

---

## Overview

This project addresses a **regression** problem in Machine Learning: predicting the progression of diabetes in patients based on demographic and biochemical measurements. The goal is to compare different regression techniques and explain their predictions using the **SHAP** library.

---

## Dataset

The dataset is the built-in **Diabetes dataset** from `scikit-learn`, loaded with:

```python
diabetes_data = load_diabetes(as_frame=True, scaled=False)
```

It contains **442 patients** and **11 columns**:

| Feature | Description |
|---------|-------------|
| age     | Age of the patient |
| sex     | Sex of the patient |
| bmi     | Body Mass Index |
| bp      | Average blood pressure |
| s1 – s6 | Six blood serum measurements |
| **target** | **Quantitative measure of disease progression after one year (mg/dL)** |

---

## Methodology

The project is implemented in Python using **Google Colab**. The main steps are:

1. **Data Loading** – Loading the Diabetes dataset from `sklearn.datasets`.
2. **Range Analysis** – Printing the value range of the `target` column.
3. **Missing Value Handling** – Checking for and removing NaN values.
4. **Frequency Plot** – Plotting a histogram of the `target` variable distribution.
5. **Normalization** – Scaling both input features (`X`) and the target (`y`) to [0, 1] using `MinMaxScaler`.
6. **Seed Definition** – Setting `SEED = 42` for reproducibility.
7. **K-Fold (6 splits)** – Splitting data into 6 folds using `KFold`, storing only indices (no data replication).
8. **Model Training with Hyperparameter Tuning** – Using `RandomizedSearchCV` (n_iter=10, inner cv=3) to find optimal hyperparameters for each model on each fold.
9. **Prediction & Denormalization** – Predicting on train and test sets, then reversing the scaling to get real-world values.
10. **Error Metrics** – Computing RMSE, MAE, Max Error, and MAPE for each model/fold.
11. **Actual vs. Predicted Plots** – Visualizing predictions against ground truth (generated for Fold 0).
12. **SHAP Explainability** – Generating SHAP Summary plots and Waterfall plots to interpret model decisions.

---

## Models

| Model | Hyperparameters Tuned |
|-------|-----------------------|
| **Random Forest** | `n_estimators`, `max_depth`, `min_samples_split` |
| **Gaussian Process Regression (GPR)** | `alpha`, `n_restarts_optimizer` |
| Support Vector Regression (SVR) | `C`, `kernel`, `gamma` |
| K-Nearest Neighbors (kNN) | `n_neighbors`, `weights` |

---

## Evaluation Metrics

Each model is evaluated on both **training** and **test** sets using:

- **RMSE** – Root Mean Squared Error
- **MAE** – Mean Absolute Error
- **Max Error** – Maximum single prediction error
- **MAPE** – Mean Absolute Percentage Error

Metrics are computed both on normalized values (`_n`) and on real (denormalized) values.

---

## Results

Results are saved in:
- `out/diabetes_model_results.csv` / `out/diabetes_model_results.xlsx` – Full metrics table for all models and folds.
- `out/plots/` – Actual vs. Predicted plots and SHAP plots.

The **Gaussian Process Regression (GPR)** model achieved the best performance, attaining the lowest RMSE on the test set. Models using kernel functions (GPR, SVR) proved more effective at capturing the non-linear patterns present in the medical data. The **SHAP analysis** confirmed that BMI and blood serum indicators are among the most influential features for predicting diabetes progression.

---

## Project Structure

```
Regression Project/
├── code/
│   ├── Regression.ipynb    # Jupyter Notebook (Google Colab)
│   └── regression.py       # Exported Python script
├── out/
│   ├── diabetes_model_results.csv
│   ├── diabetes_model_results.xlsx
│   ├── Average_MAE_Test.png
│   ├── Average_MAPE_Test.png
│   ├── Average_RMSE_Test.png
│   ├── Overfitting_Check.png
│   ├── PivotTableMAE.png
│   ├── PivotTableMAPE.png
│   ├── PivotTableRMSE.png
│   ├── PivotTable_Test-Train.png
│   ├── Κατανομή_target.png
│   └── plots/              # Actual vs Predicted & SHAP plots
├── 3η εργασία Regression.pdf   # Assignment description
├── Report.pdf                   # Full project report
└── README.md
```

---

## Technologies Used

- **Python** (Google Colab)
- **pandas** – data manipulation
- **scikit-learn** – models, preprocessing, cross-validation, hyperparameter search, metrics
- **matplotlib** – data visualization
- **SHAP** – model explainability (Summary plots & Waterfall plots)
