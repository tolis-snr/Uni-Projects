<div align="center">

# 📊 Big Data

<p>
  <img src="https://img.shields.io/badge/Course-Big%20Data-orange?style=for-the-badge&logo=apache-spark&logoColor=white"/>
  <img src="https://img.shields.io/badge/University-University%20of%20Macedonia-blue?style=for-the-badge"/>
  <img src="https://img.shields.io/badge/Tool-PySpark-E25A1C?style=for-the-badge&logo=apachespark&logoColor=white"/>
  <img src="https://img.shields.io/badge/Platform-Google%20Colab-F9AB00?style=for-the-badge&logo=googlecolab&logoColor=white"/>
  <img src="https://img.shields.io/badge/Language-Python-3776AB?style=for-the-badge&logo=python&logoColor=white"/>
</p>

<p>
  Large-scale data processing and analysis projects using <strong>Apache Spark (PySpark)</strong>,
  covering RDDs, DataFrames, Spark SQL, and MLlib — all running on <strong>Google Colab</strong>.
</p>

</div>

---

## 🗂️ Projects at a Glance

| # | Project | Dataset | Key Techniques | Report |
|---|---------|---------|----------------|--------|
| 1️⃣ | [US Flights Analysis](#1%EF%B8%8F%E2%83%A3-1st-project--us-flights-analysis) | `flights_2000.csv` | RDD API · DataFrame API · Visualisations | [📄 HW1.pdf](1st%20Project/HW1.pdf) |
| 2️⃣ | [Telecom Churn Analysis](#2%EF%B8%8F%E2%83%A3-2nd-project--telecom-churn-analysis) | `telecom_churn_10k.csv` | Spark SQL · MLlib · Feature Engineering | [📄 HW2.pdf](2nd%20Project/HW2-Report.pdf) |

---

## 1️⃣ 1st Project – US Flights Analysis

> 📅 **November 2025** &nbsp;|&nbsp; 📁 [`1st Project/`](1st%20Project/)

Exploration of **domestic US flight records** using the two core Spark APIs — **RDD** and **DataFrame** — followed by rich multi-chart visualisations.

### 🗃️ Dataset – `flights_2000.csv`

> ~2 000 domestic US flight records with 11 columns.

| Column | Type | Description |
|--------|------|-------------|
| `FL_DATE` | date | Flight date |
| `AIRLINE` | string | Airline code |
| `FL_NUM` | integer | Flight number |
| `ORIGIN_AIRPORT` | string | Departure airport code |
| `DEST_AIRPORT` | string | Arrival airport code |
| `SCHED_DEP` | timestamp | Scheduled departure time |
| `DEP_DELAY` | integer | Departure delay (minutes) |
| `SCHED_ARR` | timestamp | Scheduled arrival time |
| `ARR_DELAY` | integer | Arrival delay (minutes) |
| `DIST_KM` | integer | Flight distance (km) |
| `CANCELLED` | integer | 1 = cancelled, 0 = operated |

### 📓 Notebooks

<details>
<summary>✈️ <strong>Topic 1 – Average Departure Delay per Airport (RDD API)</strong></summary>

**File:** `Notebooks/Θέμα 1ο.ipynb`

Uses the **Spark RDD API** to compute the average departure delay for every origin airport.

1. Load CSV → Spark DataFrame → convert to RDD
2. Map rows to `(ORIGIN_AIRPORT, DEP_DELAY)` pairs; filter cancelled / null entries
3. Compute per-airport average with `combineByKey`
4. Sort descending and surface the **top 10** airports

**Top 10 airports by average delay:**

| 🏆 | Airport | Avg Delay (min) |
|----|---------|:--------------:|
| 1 | DFW | 17.36 |
| 2 | SEA | 15.69 |
| 3 | ORD | 15.18 |
| 4 | JFK | 14.57 |
| 5 | LAS | 14.51 |
| 6 | CLT | 14.00 |
| 7 | DEN | 13.96 |
| 8 | PHX | 13.49 |
| 9 | ATL | 13.17 |
| 10 | MCO | 13.16 |

⏱️ **Performance:** avg ~12 s over 5 runs (first run ~27 s due to JVM warm-up)

</details>

<details>
<summary>🛣️ <strong>Topic 2 – Top 10 Routes by Departure Delay (DataFrame API)</strong></summary>

**File:** `Notebooks/Θέμα 2ο.ipynb`

Finds origin → destination pairs with the highest average departure delay using the **DataFrame API**, then writes the result to `top_10_routes.csv`.

1. Load CSV → filter cancelled flights & null delays
2. Group by `(ORIGIN_AIRPORT, DEST_AIRPORT)` → `avg(DEP_DELAY)`
3. Sort descending → top 10 routes → save to `top_10_routes.csv`

⏱️ **Performance:** avg ~12 s over 5 runs (first run ~36 s)

</details>

<details>
<summary>📊 <strong>Topic 4 – Horizontal Bar Chart of Top 10 Routes</strong></summary>

**File:** `Notebooks/Θέμα 4ο.ipynb`

Reads `top_10_routes.csv` into **Pandas** and renders a horizontal bar chart:
- X-axis: average departure delay (minutes)
- Y-axis: route label (`ORIGIN -> DEST`), sorted highest → lowest

</details>

<details>
<summary>🎨 <strong>Topic 5 – Multi-Chart Visualisation</strong></summary>

**File:** `Notebooks/Θέμα 5ο.ipynb`

Produces **three different charts** from the raw dataset using PySpark + Pandas + Matplotlib + Seaborn:

| Chart | Type | What it shows |
|-------|------|---------------|
| **a)** | 📈 Line plot | Average delay by **hour of day** (0–23); red dashed line at y = 0 |
| **b)** | 📊 Bar chart | Average delay by **airline** (sorted descending) |
| **c)** | 🔥 Heatmap | Origin-destination average delay matrix for the **top 10 busiest airports** |

> 🤖 The Seaborn heatmap was assisted by **Google Gemini**.

</details>

### 📦 Dependencies

| Library | Role |
|---------|------|
| `pyspark` | Distributed processing (RDD & DataFrame) |
| `pandas` | In-memory data manipulation |
| `matplotlib` | Bar charts & line plots |
| `seaborn` | Heatmap visualisation |

---

## 2️⃣ 2nd Project – Telecom Churn Analysis

> 📅 **January 2025** &nbsp;|&nbsp; 📁 [`2nd Project/`](2nd%20Project/)

End-to-end analysis of a **telecom customer churn** dataset (~10 000 records) covering data cleaning, Spark SQL analytics, and machine-learning regression — all with **PySpark**.

### 🗃️ Dataset – `telecom_churn_10k.csv`

> ~10 000 customer records stored in `src/`.

| Column group | Columns |
|---|---|
| 👤 Demographics | `AGE`, `COUNTRY` |
| 📋 Subscription | `TENURE_MONTHS`, `CONTRACT_TYPE`, `PAYMENT_METHOD` |
| 📡 Services | `HAS_INTERNET`, `HAS_MOBILE`, `HAS_TV` |
| 💳 Charges | `MONTHLY_CHARGES`, `TOTAL_CHARGES` |
| 🎧 Support | `NUM_COMPLAINTS`, `SUPPORT_CALLS` |
| 🎯 Target | `CHURN` (0 = retained · 1 = churned) |

### 📓 Notebooks

<details>
<summary>🧹 <strong>Topic 1 – Data Preprocessing & Feature Engineering</strong></summary>

**File:** `notebooks/Θέμα 1ο.ipynb` &nbsp;|&nbsp; **Output:** `out/telecom_new_col.csv`

| Step | What happens |
|------|-------------|
| ① Load | CSV → PySpark DataFrame via Colab upload |
| ② Analyse | Null-check across all columns |
| ③ Clean | Drop rows where `CHURN` is null; fill numeric nulls with the **median** |
| ④ Stats | `describe()` on numeric columns |
| ⑤ Engineer | Add `NUM_SERVICES` = internet + mobile + TV; `AVG_CHARGE_PER_MONTH` = total / tenure |
| ⑥ Export | Save enriched DataFrame to `telecom_new_col.csv` |

</details>

<details>
<summary>🔍 <strong>Topic 2 – Spark SQL Analytical Queries</strong></summary>

**File:** `notebooks/Θέμα 2ο.ipynb`

Registers a temporary SQL view (`churn_view`) and runs four analytical queries:

| Query | Finding |
|-------|---------|
| **2.1** Churn by contract type | Month-to-Month **53.21%** · One-Year **20.61%** · Two-Year **13.65%** |
| **2.2** Churn by # of services | Churn rate grouped by `NUM_SERVICES` (0–3) |
| **2.3** Charges vs churn | Avg `MONTHLY_CHARGES` by churn status, further split by contract type |
| **3** Geographic analysis | Top 5 countries by churn rate (countries with > 100 customers) |

</details>

<details>
<summary>🤖 <strong>Topic 3 – Decision Tree Regressor (PySpark MLlib)</strong></summary>

**File:** `notebooks/Θέμα 3ο.ipynb`

Predicts `MONTHLY_CHARGES` using a **Decision Tree Regression** model.

**Pipeline:**

```
StringIndexer (categorical columns)
      ↓
VectorAssembler (all features → single vector)
      ↓
DecisionTreeRegressor (label = MONTHLY_CHARGES)
```

**Features used:** `AGE`, `TENURE_MONTHS`, `NUM_COMPLAINTS`, `SUPPORT_CALLS`, `HAS_INTERNET`, `HAS_MOBILE`, `HAS_TV`, `NUM_SERVICES`, `CONTRACT_TYPE`, `PAYMENT_METHOD`, `COUNTRY`

**Results (70 / 30 split, seed = 42):**

| Metric | Value | Interpretation |
|--------|-------|----------------|
| RMSE | **7.93** | ~8 € avg error per bill |
| R² | **0.49** | ~49 % of charge variance explained |
| Mean charge | **36.54 €** | Dataset baseline |

**Visualisations:**
- 🔵 Scatter plot – predicted vs. actual `MONTHLY_CHARGES` (100-sample subset)
- 📊 Horizontal bar chart – feature importances from the trained Decision Tree

> 🤖 Chart generation was assisted by **Google Gemini**.

</details>

### 📦 Technologies Used

| Tool | Purpose |
|------|---------|
| `pyspark` (DataFrames, Spark SQL, MLlib) | Core big-data processing & ML |
| `pandas` | In-memory manipulation |
| `matplotlib` | Visualisations |
| `google.colab` | Execution environment & file upload |

---

## ▶️ How to Run

All notebooks target **Google Colab**:

1. 📂 Open the `.ipynb` file in [Google Colab](https://colab.research.google.com/)
2. ▶️ Run the first cell — it installs **PySpark** automatically via `pip`
3. 📤 Upload the required CSV when prompted by the file-upload widget
4. ⬇️ Execute remaining cells in order

> ⚠️ The `google.colab.files.upload()` widget **only works inside Google Colab**.

---

<div align="center">

Made with ❤️ by **Apostolos Sinioris** · Applied Informatics, University of Macedonia

</div>
