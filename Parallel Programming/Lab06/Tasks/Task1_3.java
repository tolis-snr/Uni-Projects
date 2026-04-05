/*
    * Παρατηρήσεις:
    * Ο παρών κώδικας χρησιμοποιεί λανθασμένα τον αμοιβαίο αποκλεισμό.
    * Το αποτέλεσμα που παράγεται είναι μεν το σωστό, αλλά επιτυγχάνεται με τρόπο,
    * όπου δεν θα πρέπει να χρησιμοποιείται.
    * Συγκεκριμένα, η μέθοδος getSum στην κλάση Shared διαβάζει δεδομένα,
    * ενώ αυτά μπορεί ταυτόχρονα να γράφονται μέσω της μεθόδου addSum.
    * Για να θεωρείται σωστό θα έπρεπε, είτε να υπάρχουν ReetrantLocks,
    * είτε synchronized μέθοδοι/τμήματα κώδικα, όπως στο Task1_2
*/


public class Task1_3 {

    public static void main(String[] args) {

        long numSteps = 10000;
        int numThreads = 0;

        if (args.length != 2) {
            System.out.println("arguments:  <number of steps> <number of threads>");
            System.exit(1);
        }
        try {
            numSteps = Long.parseLong(args[0]);
            numThreads = Integer.parseInt(args[1]);
            if(numThreads == 0)
                numThreads = Runtime.getRuntime().availableProcessors();
        } catch (NumberFormatException e) {
            System.out.println("argument "+ args[0] +" must be long int");
            System.exit(1);
        }

        /* start timing */
        double sum = 0.0;
        double step = 1.0 / (double) numSteps;
        Shared shared = new Shared(numThreads);

        long startTime = System.currentTimeMillis();
        /* create threads */
        PiThread[] threads = new PiThread[numThreads];
        for (int i = 0; i < numThreads; ++i) {
            threads[i] = new PiThread(i, numSteps, numThreads, step,  shared);
            threads[i].start();
        }

        /* wait for threads to finish */
        for (int i = 0; i < numThreads; ++i) {
            try {
                threads[i].join();
//                sum += threads[i].getSum();
            } catch (InterruptedException e) {
                System.out.println("Thread interrupted: " + e.getMessage());
            }
        }

        double pi = shared.getSum() * step;

        /* end timing and print result */
        long endTime = System.currentTimeMillis() - startTime;
        System.out.printf("sequential program results with %d steps\n", numSteps);
        System.out.printf("computed pi = %22.20f\n", pi);
        System.out.printf("difference between estimated pi and Math.PI = %22.20f\n", Math.abs(pi - Math.PI));
        System.out.printf("time to compute = %d ms\n", endTime);
    }
}

class Shared {
    private double[] sums;

    public Shared(int numThreads) {
        sums = new double[numThreads];
    }

    public void addSum(int threadId, double value) {
        sums[threadId] += value; // it writes to a specific index of the array
    }

    public double getSum() {
        double total = 0.0;
        for(double s : sums) total += s; // race condition, the operation is not atomic
        return total;
    }

}

class PiThread extends Thread {
    private long start, end;
    private double step;
    private double sum = 0.0;
    private Shared shared;
    private int id;

    public PiThread(int id, long numSteps, int numThreads, double step, Shared shared) {
        this.step = step;
        this.start = id * (numSteps / numThreads);
        this.end = (id + 1) * (numSteps / numThreads);
        if (id == numThreads - 1) {
            this.end = numSteps;
        }
        this.shared = shared;
        this.id = id;
    }

    public void run() {
        for (long i = start; i < end; ++i) {
            double x = ((double) i + 0.5) * step;
            sum += 4.0 / (1.0 + x * x);
        }
        shared.addSum(id, sum);
    }

}