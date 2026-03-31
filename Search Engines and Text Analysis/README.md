# Sentiment Analysis on Movie Reviews

**Course:** Search Engines and Text Analysis  
**Department:** Applied Informatics, University of Macedonia  
**Author:** Apostolos Siniori (ics24123)  
**Supervisor:** Georgia Koloniari

---

## Overview

This project tackles a **binary Sentiment Analysis** (Opinion Mining) problem: classifying movie reviews as **Positive** or **Negative**. It is a text classification task within the field of Natural Language Processing (NLP) and Machine Learning, making use of probabilistic **Naïve Bayes** classifiers.

---

## Dataset

The dataset is the **Polarity Dataset v2.0** from the Cornell University Computer Science department, introduced in _Pang/Lee ACL 2004_.

- **Source:** http://www.cs.cornell.edu/people/pabo/movie-review-data/
- **Size:** 2,000 movie reviews (1,000 Positive, 1,000 Negative)
- **Format:** Pre-tokenised plain text files, organised into two subfolders (`pos` / `neg`)
- Labels were originally derived from IMDB star ratings by the dataset creators.

---

## Project Structure

```
Search Engines and Text Analysis/
├── code/
│   ├── PP2.ipynb          # Jupyter Notebook (Google Colab)
│   └── pp2.py             # Equivalent Python script
├── out/
│   ├── results_all.xlsx   # Evaluation results (Part A & Part B)
│   ├── binary_performance.png
│   ├── term-freq_performance.png
│   └── tf-idf_performance.png
├── src/
│   └── review_polarity.tar.gz   # Raw dataset archive
├── pp2.pdf                # Assignment sheet (in Greek)
└── Report-PP2.pdf         # Project report (in Greek)
```

---

## Methodology

### Part A – Baseline Classifiers

#### Text Pre-processing (Baseline Pipeline)
1. **Removal of non-alphabetic characters** (HTML tags, punctuation, digits)
2. **Lower casing** – normalise word capitalisation
3. **Tokenisation** – split text into individual tokens
4. **Stopword removal** – remove common English words (the, is, at, …)
5. **Stemming** – reduce words to their root form using the **Porter Stemmer**

#### Classifiers & Document Representations
Five classifier–representation combinations were evaluated:

| Pipeline | Classifier | Representation |
|---|---|---|
| K1_E2 | Multinomial NB | Term Frequency (Counts) |
| K1_E3 | Multinomial NB | TF-IDF |
| K2_E1 | Bernoulli NB | Binary |
| K3_E1 | Boolean (Binarized) Multinomial NB | Binary |
| K1_E4 | Multinomial NB | Word2Vec + Mean Pooling + MinMaxScaler |

For **Word2Vec**, a custom `Word2VecVectorizer` transformer was implemented using **Gensim**. Each document is represented as the mean of its word vectors (Mean Pooling), followed by **MinMaxScaler** normalisation to the [0, 1] range (required by Naïve Bayes, which cannot handle negative values).

#### Evaluation
All models were evaluated using **5-Fold Cross-Validation**, measuring:
- Accuracy
- Precision, Recall, F1-Score (macro average)
- Confusion Matrix

#### Part A Results

| Pipeline | Accuracy | Precision | Recall | F1-Score |
|---|---|---|---|---|
| K3_E1 – Boolean MNB + Binary | **81.95%** | 82.01% | 83.25% | **81.94%** |
| K1_E3 – MNB + TF-IDF | 81.30% | 81.40% | 81.30% | 81.20% |
| K1_E2 – MNB + Counts | 80.80% | 80.86% | 80.80% | ~80.8% |
| K2_E1 – Bernoulli NB + Binary | ~81% | — | — | — |
| K1_E4 – MNB + Word2Vec | 56.50% | — | — | 56.40% |

> **Best model:** Boolean (Binarized) Multinomial NB with Binary representation achieved the highest accuracy (81.95%). Word2Vec performed worst (56.5%), likely because the 2,000-document corpus is too small for high-quality embeddings, and Mean Pooling may neutralise sentiment signals.

---

### Part B – Linguistic Pre-processing Techniques

Three domain-specific text pre-processing techniques were implemented on top of the baseline and compared across five scenarios:

#### Techniques
1. **Negation Handling** – Words following a negation trigger (`not`, `no`, `never`, `n't`, `cannot`, `nothing`, `nowhere`) receive a `NOT_` prefix until the next punctuation mark.  
   _Example:_ `"not good movie"` → `"NOT_good NOT_movie"`

2. **POS (Part-of-Speech) Weighting** – Adjectives and adverbs receive a weight of **2** (by duplicating their tokens); all other words retain weight **1**. Implemented using NLTK's POS tagger.

3. **Position Weighting** – Tokens from the **first** and **last** sentence of a review receive a weight of **2** (by appending those sentences again to the text). Other tokens retain weight **1**.

#### Scenarios Evaluated

| Scenario | Description |
|---|---|
| 1 | Baseline (Simple Clean) |
| 2 | Baseline + Negation Handling |
| 3 | Baseline + POS Weighting |
| 4 | Baseline + Position Weighting |
| 5 | Baseline + All Combined |

#### Part B Results (Baseline classifier: Multinomial NB)

| Scenario | Accuracy | F1-Score |
|---|---|---|
| Baseline + POS Weighting | **83.25%** | **83.24%** |
| Baseline + All Combined | ~83% | ~83% |
| Baseline + Negation | ~82% | ~82% |
| Baseline + Position Weighting | ~82% | ~82% |
| Baseline (Simple Clean) | 81.10% | 81.09% |

> **Key finding:** POS Weighting provided the greatest improvement, confirming that adjectives and adverbs are the most informative tokens for Sentiment Analysis. All linguistic techniques outperformed the basic baseline.

---

### Part C – Comparison with RapidMiner

The custom Python models were compared against **RapidMiner** using equivalent pre-processing (baseline) and three document representations (Binary, Counts/TF, TF-IDF) with 5-Fold Cross-Validation.

| Representation | Python Model Accuracy | RapidMiner Accuracy |
|---|---|---|
| Binary | 81.95% | significantly lower |
| Counts (TF) | 80.80% | significantly lower |
| TF-IDF | 81.30% | significantly lower |

The custom implementation consistently outperformed RapidMiner across all representations, demonstrating the advantage of targeted, domain-specific pre-processing pipelines built in Python.

---

## Technologies & Libraries

| Library | Purpose |
|---|---|
| `scikit-learn` | Naïve Bayes classifiers, CountVectorizer, TfidfVectorizer, cross-validation |
| `nltk` | Stopword removal, stemming (Porter), POS tagging, tokenisation |
| `gensim` | Word2Vec embeddings |
| `numpy` / `pandas` | Data manipulation |
| `openpyxl` | Exporting results to Excel |
| RapidMiner | Baseline comparison tool |

---

## How to Run

1. Open `code/PP2.ipynb` in **Google Colab** (recommended) or a local Jupyter environment.
2. The notebook will automatically download and extract the dataset from the Cornell website.  
   Alternatively, place the `review_polarity.tar.gz` archive from the `src/` folder in the working directory and extract it manually.
3. Run all cells in order. Results are saved to `results_all.xlsx`.

---

## Results Summary

- The **Boolean (Binarized) Multinomial NB** model achieved the best performance in Part A (~82% accuracy), showing that binary presence/absence of terms is more informative than raw frequency for this dataset.
- Adding **POS Weighting** in Part B pushed accuracy above **83%**, proving that sentiment-specific linguistic features improve classification.
- The custom Python implementation **significantly outperformed RapidMiner** for this task.
- **Word2Vec** underperformed (56.5%) due to the limited dataset size and information loss from Mean Pooling.
