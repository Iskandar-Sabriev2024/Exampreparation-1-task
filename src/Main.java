import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Main {
    public static void main(String[] args) throws Exception {
        int start = 1;
        int end = 1_000_000;


        long startTime = System.currentTimeMillis();
        long simpleSum = calculateSum(start, end);
        long simpleTime = System.currentTimeMillis() - startTime;

        System.out.println("Oddiy usulda yig'indi: " + simpleSum);
        System.out.println("Oddiy usulda hisoblash vaqti: " + simpleTime + " ms");


        startTime = System.currentTimeMillis();
        long parallelSum = calculateSumWithThreads(start, end, 5);
        long parallelTime = System.currentTimeMillis() - startTime;

        System.out.println("Threadlar orqali yig'indi: " + parallelSum);
        System.out.println("Threadlar orqali hisoblash vaqti: " + parallelTime + " ms");


        if (simpleTime > parallelTime) {
            System.out.println("Thread orqali hisoblash oddiy usuldan " + (simpleTime - parallelTime) + " ms tezroq.");
        } else if (simpleTime == parallelTime) {
            System.out.println("Thread orqali hisoblash va oddiy usulda hisoblash o`zaro teng!");
        } else {
            System.out.println("Oddiy usulda hisoblash thread orqali usuldan " + (parallelTime - simpleTime) + " ms tezroq.");
        }
    }


    private static long calculateSum(int start, int end) {
        long sum = 0;
        for (int i = start; i <= end; i++) {
            sum += i;
        }
        return sum;
    }


    private static long calculateSumWithThreads(int start, int end, int threadCount) throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        List<Future<Long>> futures = new ArrayList<>();

        int range = (end - start + 1) / threadCount;

        for (int i = 0; i < threadCount; i++) {
            int rangeStart = start + i * range;
            int rangeEnd = (i == threadCount - 1) ? end : (rangeStart + range - 1);

            Callable<Long> task = () -> calculateSum(rangeStart, rangeEnd);
            futures.add(executor.submit(task));
        }

        long totalSum = 0;
        for (Future<Long> future : futures) {
            totalSum += future.get();
        }

        executor.shutdown();
        return totalSum;
    }
}
