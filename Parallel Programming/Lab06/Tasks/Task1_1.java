public class Task1_1 {

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

        long startTime = System.currentTimeMillis();
        /* create threads */
        PiThread[] threads = new PiThread[numThreads];
        for (int i = 0; i < numThreads; ++i) {
            threads[i] = new PiThread(i, numSteps, numThreads, step);
            threads[i].start();
        }

        /* wait for threads to finish */
        for (int i = 0; i < numThreads; ++i) {
            try {
                threads[i].join();
                sum += threads[i].getSum();
            } catch (InterruptedException e) {
                System.out.println("Thread interrupted: " + e.getMessage());
            }
        }

        double pi = sum * step;

        /* end timing and print result */
        long endTime = System.currentTimeMillis() - startTime;
        System.out.printf("sequential program results with %d steps\n", numSteps);
        System.out.printf("computed pi = %22.20f\n", pi);
        System.out.printf("difference between estimated pi and Math.PI = %22.20f\n", Math.abs(pi - Math.PI));
        System.out.printf("time to compute = %d ms\n", endTime);
    }
}

class PiThread extends Thread {
    private long start, end;
    private double step;
    private double sum = 0.0;

    public PiThread(int id, long numSteps, int numThreads, double step) {
        this.step = step;
        this.start = id * (numSteps / numThreads);
        this.end = (id + 1) * (numSteps / numThreads);
        if (id == numThreads - 1) {
            this.end = numSteps;
        }
    }

    public void run() {
        for (long i = start; i < end; ++i) {
            double x = ((double) i + 0.5) * step;
            sum += 4.0 / (1.0 + x * x); // Η sum είναι local variable
        }
    }

    public double getSum() {
        return sum;
    }

}