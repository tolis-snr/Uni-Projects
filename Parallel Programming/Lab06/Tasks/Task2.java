import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;


public class Task2 {

    public static void main(String args[]) throws IOException {
        String fileString = "";
        String patternString = "";
        int numThreads = 0;
        int matchCount = 0;

        if (args.length != 3) {
            System.out.println("BruteForceStringMatch  <file name> <pattern> <num of threads>");
            System.exit(1);
        }

        try {
            fileString = new String(Files.readAllBytes(Paths.get(args[0])));//, StandardCharsets.UTF_8);
            patternString = new String(args[1]);
            numThreads = Integer.parseInt(args[2]);

            if(numThreads == 0) numThreads = Runtime.getRuntime().availableProcessors();
        } catch(IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
            System.exit(1);
        } catch(NumberFormatException e) {
            System.out.println("argument "+ args[2] +" must be int");
            System.exit(1);
        }

        char[] text = new char[fileString.length()];
        int n = fileString.length();
        for (int i = 0; i < n; i++) {
            text[i] = fileString.charAt(i);
        }

        char[] pattern = new char[patternString.length()];
        int m = patternString.length();
        for (int i = 0; i < m; i++) {
            pattern[i] = patternString.charAt(i);
        }

        int matchLength = n - m;
        char[] match = new char[matchLength+1];
        for (int i = 0; i <= matchLength; i++) {
            match[i] = '0';
        }

        // get current time
        long start = System.currentTimeMillis();

        strThread[] threads = new strThread[numThreads];
        int size = matchLength /  numThreads;

        for (int i = 0; i < numThreads; ++i) {
            int startIndex = i * size;
            int endIndex = (i+1) * size;
            if(i == numThreads - 1) endIndex = matchLength;

            threads[i] = new strThread(startIndex, endIndex, text, pattern, match);
            threads[i].start();
        }

        /* wait for threads to finish */
        for (int i = 0; i < numThreads; ++i) {
            try {
                threads[i].join();
                matchCount += threads[i].getMatchCount();
            } catch (InterruptedException e) {
                System.out.println("Thread interrupted: " + e.getMessage());
            }
        }

        // get current time and calculate elapsed time
        long elapsedTimeMillis = System.currentTimeMillis()-start;
        System.out.println("time in ms = "+ elapsedTimeMillis);

		// print results
        for (int i = 0; i < matchLength; i++) {
            if (match[i] == '1') System.out.print(i+" ");
        }
        System.out.println();
        System.out.println("Total matches " + matchCount);

    }
}

class strThread extends Thread {
    private int startIndex, endIndex;
    private char[] text;
    private char[] pattern;
    private char[] match;
    private int matchCount = 0;

    public strThread(int startIndex, int endIndex, char[] text, char[] pattern, char[] match) {
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        this.text = text;
        this.pattern = pattern;
        this.match = match;
    }

    public void run() {
        int m = pattern.length;

        for(int i = startIndex; i < endIndex; i++) {
            int j;
            for (j = 0; j < m && pattern[j] == text[i + j]; j++);
            if (j >= m) {
                match[i] = '1';
                matchCount++;
            }
        }
    }
    public int getMatchCount() {
        return matchCount;
    }
}

