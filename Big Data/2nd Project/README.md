# Big Data – 2nd Project: Telecom Churn Analysis with PySpark

Analysis of a telecom customer churn dataset (~10,000 records) using **Apache Spark (PySpark)**, implemented in Google Colab notebooks.

---

## 📁 Repository Structure

| Folder | Contents |
|--------|----------|
| `/notebooks` | Jupyter notebooks (`.ipynb`) for each topic |
| `/src` | Source dataset: `telecom_churn_10k.csv` |
| `/out` | Output of Topic 1: `telecom_new_col.csv` (cleaned & enriched dataset) |

---

## 📊 Dataset

**`telecom_churn_10k.csv`** – Telecom customer data with ~10,000 rows.

Key columns include:
- **Demographics**: `AGE`, `COUNTRY`
- **Subscription**: `TENURE_MONTHS`, `CONTRACT_TYPE`, `PAYMENT_METHOD`
- **Services**: `HAS_INTERNET`, `HAS_MOBILE`, `HAS_TV`
- **Charges**: `MONTHLY_CHARGES`, `TOTAL_CHARGES`
- **Support**: `NUM_COMPLAINTS`, `SUPPORT_CALLS`
- **Target**: `CHURN` (0 = retained, 1 = churned)

---

## 📓 Notebooks

### Θέμα 1ο – Data Preprocessing & Feature Engineering (`Θέμα 1ο.ipynb`)

Handles data loading, cleaning, and feature creation using **PySpark DataFrames**.

**Steps:**
1. **Load CSV** into a PySpark DataFrame via Google Colab file upload.
2. **Missing value analysis** – checks for nulls across all columns; focuses on numeric fields (`AGE`, `TENURE_MONTHS`, `MONTHLY_CHARGES`, `TOTAL_CHARGES`).
3. **Cleaning strategy:**
   - Rows where `CHURN` is null are dropped.
   - Missing values in numeric columns are replaced with the **median** (robust to outliers).
4. **Descriptive statistics** – `describe()` on numeric columns after cleaning.
5. **Feature engineering** – Two new columns are added:
   - `NUM_SERVICES` = `HAS_INTERNET + HAS_MOBILE + HAS_TV` (count of active services per customer)
   - `AVG_CHARGE_PER_MONTH` = `TOTAL_CHARGES / TENURE_MONTHS` (handles division by zero safely)
6. **Export** – The enriched DataFrame is saved to `telecom_new_col.csv` for use in subsequent topics.

---

### Θέμα 2ο – Spark SQL Queries (`Θέμα 2ο.ipynb`)

Runs analytical SQL queries using **Spark SQL** on the enriched dataset from Topic 1.

**Steps:**
1. Load `telecom_new_col.csv` and create a temporary SQL view `churn_view`.
2. **Query 2.1 – Churn by contract type:**
   - Month-to-Month: **53.21%** churn rate
   - One-Year: **20.61%** churn rate
   - Two-Year: **13.65%** churn rate
3. **Query 2.2 – Churn by number of services:**
   - Churn rate grouped by `NUM_SERVICES` (0–3).
4. **Query 2.3 – Churn and monthly charges:**
   - Average `MONTHLY_CHARGES` by churn status; further broken down by `CONTRACT_TYPE`.
5. **Query 3 – Geographic analysis:**
   - Top 5 countries by churn rate, filtered to countries with more than 100 customers.

Results are discussed with comments on observed patterns.

---

### Θέμα 3ο – Machine Learning: Decision Tree Regressor (`Θέμα 3ο.ipynb`)

Trains a **Decision Tree Regression** model using **PySpark MLlib** to predict `MONTHLY_CHARGES`.

**Steps:**
1. Load the enriched dataset (`telecom_new_col.csv`) from Topic 1.
2. **Feature selection:**
   - Numeric: `AGE`, `TENURE_MONTHS`, `NUM_COMPLAINTS`, `SUPPORT_CALLS`, `HAS_INTERNET`, `HAS_MOBILE`, `HAS_TV`, `NUM_SERVICES`
   - Categorical: `CONTRACT_TYPE`, `PAYMENT_METHOD`, `COUNTRY`
   - Target label: `MONTHLY_CHARGES`
3. **Pipeline:**
   - `StringIndexer` converts categorical columns to numeric indices.
   - `VectorAssembler` combines all features into a single input vector.
   - `DecisionTreeRegressor` is the final estimator.
4. **Training:** 70% / 30% train-test split (random seed = 42).
5. **Evaluation:**
   - **RMSE: 7.93** (~8€ average error per bill)
   - **R²: 0.49** (model explains ~49% of charge variance)
   - Mean monthly charge in the dataset: **36.54€**
6. **Visualizations:**
   - Scatter plot of predicted vs. actual `MONTHLY_CHARGES` (100-sample subset).
   - Horizontal bar chart of feature importances from the trained Decision Tree.

---

## 🛠️ Technologies Used

- **Apache Spark / PySpark** (DataFrames, Spark SQL, MLlib)
- **Google Colab** (execution environment)
- **Python** (pandas, matplotlib for visualizations)

---

## 📝 Notes

- **Google Gemini** was used to assist with chart generation in Topic 3 and for some inline code comments (via autocomplete).
- Code in all three topics is based on course material combined with external sources, cited in the project report (`HW2-Report.pdf`).
- The full assignment specification is available in `2ηεργασία_BigData_January_2025.pdf`.