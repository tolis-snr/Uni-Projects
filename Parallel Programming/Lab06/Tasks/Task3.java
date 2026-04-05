/*
 * ==============================================================================
 * ΈΛΕΓΧΟΣ ΙΚΑΝΟΠΟΙΗΣΙΜΟΤΗΤΑΣ ΚΥΚΛΩΜΑΤΟΣ - Παράλληλη Υλοποίηση (Map-Reduce)
 * ==============================================================================
 *
 * ΣΚΟΠΟΣ:
 * Το πρόγραμμα επιλύει το πρόβλημα ικανοποιησιμότητας κυκλώματος χρησιμοποιώντας
 * πολλαπλά νήματα (multi-threading). Ελέγχει όλες τις 2^size δυνατές ανάθεσης
 * τιμών στις boolean μεταβλητές για να βρει ποιες ικανοποιούν ένα σύνθετο
 * boolean κύκλωμα (λογική έκφραση).
 *
 * ΛΕΙΤΟΥΡΓΙΑ:
 * 1. Δέχεται δύο παραμέτρους:
 *    - Μέγεθος διανύσματος (αριθμός boolean μεταβλητών)
 *    - Αριθμό νημάτων για παράλληλη επεξεργασία
 *
 * 2. Διαιρεί το χώρο αναζήτησης (2^size δυνατές αναθέσεις) ανάμεσα στα νήματα
 *    ώστε να λειτουργούν παράλληλα.
 *
 * 3. Κάθε νήμα έχει τη δική του τοπική λίστα (localOutput) για αποθήκευση
 *    των αποτελεσμάτων χωρίς να χρειάζεται συγχρονισμός.
 *
 * 4. Μετά την ολοκλήρωση όλων των νημάτων, συνδυάζονται τα αποτελέσματα από
 *    όλα τα νήματα σε μία τελική λίστα (REDUCE φάση - Map-Reduce pattern).
 *
 * 5. Εμφανίζει τον συνολικό χρόνο εκτέλεσης σε χιλιοστά του δευτερολέπτου.
 *
 * ΚΎΡΙΑ ΣΥΣΤΑΤΙΚΆ:
 * - Task3: Κύρια κλάση που διευθύνει τη δημιουργία και εκτέλεση των νημάτων
 * - CircuitThread: Κλάση νήματος που ελέγχει το εκχωρημένο εύρος αναθέσεων
 *
 * ΠΛΕΟΝΕΚΤΗΜΑΤΑ ΑΥΤΗΣ ΤΗΣ ΥΛΟΠΟΙΗΣΗΣ (Map-Reduce):
 * Κάθε νήμα έχει τη δική του τοπική μνήμη για αποθήκευση, αποφεύγοντας τις
 * επιβαρύνσεις συγχρονισμού που υπάρχουν στη χρήση ConcurrentLinkedQueue.
 *
 * ==============================================================================
 */

import java.lang.Math;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Task3 {

    public static void main(String[] args) {

        int size = 0;
        if (args.length != 2) {
            System.out.println("Usage: java SimpleSat <vector size> <number of threads>");
            System.exit(1);
        }

        try {
            size = Integer.parseInt(args[0]);
        } catch (NumberFormatException nfe) {
            System.out.println("Integer argument expected");
            System.exit(1);
        }
        if (size <= 0) {
            System.out.println("size should be positive integer");
            System.exit(1);
        }
        int iterations = (int) Math.pow(2, size);

        int numThreads = Integer.parseInt(args[1]);
        if (numThreads == 0)
            numThreads = Runtime.getRuntime().availableProcessors();

        // No shared queue needed anymore
        CircuitThread[] threads = new CircuitThread[numThreads];

        long startTime = System.currentTimeMillis();

        int block = iterations / numThreads;
        int start = 0;
        int end = 0;

        // MAP: Create threads with their own lists
        for (int i = 0; i < numThreads; i++) {
            start = i * block;
            end = start + block;
            if (i == (numThreads - 1))
                end = iterations;
            threads[i] = new CircuitThread(start, end, size);
            threads[i].start();
        }

        // Join threads
        for (int i = 0; i < numThreads; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {}
        }

        // REDUCE: Merge all results from all threads
        LinkedList<String> finalOutput = new LinkedList<>();
        for (int i = 0; i < numThreads; i++) {
            finalOutput.addAll(threads[i].getResults());  // Combine results
        }

        long elapsedTimeMillis = System.currentTimeMillis() - startTime;

        System.out.println(finalOutput);

        System.out.println("All done\n");
        System.out.println("time in ms = " + elapsedTimeMillis);
    }
}

class CircuitThread extends Thread {
    private int start, end;
    private int size;
    private LinkedList<String> localOutput;

    public CircuitThread(int start, int end, int size) {
        this.start = start;
        this.end = end;
        this.size = size;
        this.localOutput = new LinkedList<>();
    }

    public void run() {
        boolean[] v = new boolean[size];
        for (int i = start; i < end; i++) {
            check_circuit(i, size, v);
        }
    }

    private void check_circuit(int start, int size, boolean[] v) {

        for (int i = size - 1; i >= 0; i--)
            v[i] = (start & (1 << i)) != 0;

        boolean value = (v[0] || v[1])
                && (!v[1] || !v[3])
                && (v[2] || v[3])
                && (!v[3] || !v[4])
                && (v[4] || !v[5])
                && (v[5] || !v[6])
                && (v[5] || v[6])
                && (v[6] || !v[15])
                && (v[7] || !v[8])
                && (!v[7] || !v[13])
                && (v[8] || v[9])
                && (v[8] || !v[9])
                && (!v[9] || !v[10])
                && (v[9] || v[11])
                && (v[10] || v[11])
                && (v[12] || v[13])
                && (v[13] || !v[14])
                && (v[14] || v[15])
                && (v[14] || v[16])
                && (v[17] || v[1])
                && (v[18] || !v[0])
                && (v[19] || v[1])
                && (v[19] || !v[18])
                && (!v[19] || !v[9])
                && (v[0] || v[17])
                && (!v[1] || v[20])
                && (!v[21] || v[20])
                && (!v[22] || v[20])
                && (!v[21] || !v[20])
                && (v[22] || !v[20]);

        if (value) {
            saveResult(v, size, start);
        }
    }

    private void saveResult(boolean[] v, int size, int z) {
        String result = String.valueOf(z);

        for (int i = 0; i < size; i++)
            if (v[i])
                result += " 1";
            else
                result += " 0";

        localOutput.add("\n" + result);
    }

    public LinkedList<String> getResults() {
        return localOutput;
    }
}
