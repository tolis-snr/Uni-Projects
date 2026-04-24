# 📊 Big Data – 1st Project

> **Course:** Big Data  
> **University:** University of Macedonia – Applied Informatics  
> **Tool:** Apache Spark (PySpark) · Google Colab

---

## 📁 Project Structure

```
1st Project/
├── Notebooks/
│   ├── flights_2000.csv          # Source dataset
│   ├── top_10_routes.csv         # Output produced by Topic 2
│   ├── Θέμα 1ο.ipynb             # Topic 1 – RDD processing
│   ├── Θέμα 2ο.ipynb             # Topic 2 – DataFrame processing
│   ├── Θέμα 4ο.ipynb             # Topic 4 – Bar chart visualisation
│   └── Θέμα 5ο.ipynb             # Topic 5 – Multi-chart visualisation
├── HW1.pdf                       # Assignment specification
└── 1ηεργασία_BigData_November_2025.pdf  # Submitted report
```

---

## 🗃️ Dataset – `flights_2000.csv`

The dataset contains domestic US flight records and has the following schema:

| Column | Type | Description |
|---|---|---|
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
| `CANCELLED` | integer | 1 if cancelled, 0 otherwise |

---

## 📓 Notebooks

### Topic 1 – Average Departure Delay per Airport (RDD API)

**File:** `Θέμα 1ο.ipynb`

Uses the **Spark RDD API** to compute the average departure delay for every origin airport.

**Steps:**
1. Load `flights_2000.csv` into a Spark DataFrame, then convert to an RDD.
2. Map each row to a `(ORIGIN_AIRPORT, DEP_DELAY)` pair and filter out cancelled/null entries.
3. Compute the per-airport average using `combineByKey` (with custom `create_comb`, `merge_val`, and `merge_comb` functions).
4. Sort descending and take the **top 10** airports by mean delay.

**Sample output (top 10):**

| Airport | Avg Delay (min) |
|---------|----------------|
| DFW | 17.36 |
| SEA | 15.69 |
| ORD | 15.18 |
| JFK | 14.57 |
| LAS | 14.51 |
| CLT | 14.00 |
| DEN | 13.96 |
| PHX | 13.49 |
| ATL | 13.17 |
| MCO | 13.16 |

**Performance (5 runs):** average ≈ **12 s** (first run ~27 s due to JVM startup).

---

### Topic 2 – Top 10 Routes by Departure Delay (DataFrame API)

**File:** `Θέμα 2ο.ipynb`

Uses the **Spark DataFrame API** to find the routes (origin → destination pairs) with the highest average departure delay, and saves the result to CSV.

**Steps:**
1. Load `flights_2000.csv` into a Spark DataFrame.
2. Filter out cancelled flights (`CANCELLED == 0`) and rows with null `DEP_DELAY`.
3. Group by `(ORIGIN_AIRPORT, DEST_AIRPORT)` and compute `avg(DEP_DELAY)`.
4. Sort descending and take the **top 10** routes.
5. Write the result to `top_10_routes.csv` (used as input by Topic 4).

**Performance (5 runs):** average ≈ **12 s** (first run ~36 s).

---

### Topic 4 – Horizontal Bar Chart of Top 10 Routes

**File:** `Θέμα 4ο.ipynb`

Reads the `top_10_routes.csv` produced by Topic 2 into a **Pandas** DataFrame and visualises the results.

**Steps:**
1. Load `top_10_routes.csv` with `pandas.read_csv`.
2. Combine `ORIGIN_AIRPORT` and `DEST_AIRPORT` into a `Route` label (e.g. `DFW -> LAX`).
3. Plot a **horizontal bar chart** (`matplotlib.pyplot.barh`) with:
   - X-axis: average departure delay (minutes)
   - Y-axis: route label (sorted with the highest delay at the top)

---

### Topic 5 – Multi-Chart Visualisation

**File:** `Θέμα 5ο.ipynb`

Uses **PySpark + Pandas + Matplotlib + Seaborn** to produce three different visualisations from the original dataset.

**Chart a) – Average Delay by Hour of Day (Line Plot)**
- Extracts the scheduled departure hour from `SCHED_DEP`.
- Groups by hour and computes `avg(DEP_DELAY)`.
- Plots a **line chart** (hours 0–23 on the X-axis, average delay on Y-axis).
- A dashed red line at y = 0 marks the boundary between early and late departures.

**Chart b) – Average Delay by Airline (Bar Chart)**
- Filters out cancelled flights and null delays.
- Groups by `AIRLINE` and computes `avg(DEP_DELAY)`, sorted descending.
- Plots a **vertical bar chart** (airline code on X-axis, delay on Y-axis).

**Chart c) – Origin–Destination Heatmap (Seaborn)**
- Identifies the top 10 busiest origin airports by flight count.
- Filters flights that depart from **and** arrive at one of those 10 airports.
- Groups by `(ORIGIN_AIRPORT, DEST_AIRPORT)` and computes average delay.
- Pivots the result into a matrix and renders it as a **heatmap** using `seaborn` (`coolwarm` palette, annotated with delay values).

> **Note:** The `seaborn` heatmap implementation was assisted by the **Gemini** AI model, as the `seaborn` library was not previously familiar to the team.

---

## ▶️ How to Run

All notebooks are designed to run on **Google Colab**:

1. Open the desired `.ipynb` file in [Google Colab](https://colab.research.google.com/).
2. Run the first cell — it installs PySpark automatically via `pip`.
3. When prompted by the file-upload widget, upload `flights_2000.csv` (Topics 1, 2, 5) or `top_10_routes.csv` (Topic 4).
4. Execute the remaining cells in order.

> The notebooks use `google.colab.files.upload()` for data ingestion, so the file-upload widget **only works inside Google Colab**.

---

## 📦 Dependencies

| Library | Purpose |
|---------|---------|
| `pyspark` | Distributed data processing (RDD & DataFrame) |
| `pandas` | In-memory data manipulation |
| `matplotlib` | Bar charts and line plots |
| `seaborn` | Heatmap visualisation |
