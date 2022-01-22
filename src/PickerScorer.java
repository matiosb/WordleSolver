import algorithm.Solver;
import algorithm.pickers.PickMostFrequentLetters;
import algorithm.pickers.PickRandom;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.stream.Collectors;

/**
 * Simple picker from alphabetically sorted list
 *     * By avg guesses: [perst={count=2315, sum=11008, min=2, average=4.755076, max=13}, plots={count=2315, sum=11017, min=2, average=4.758963, max=11}]
 *     * By max guesses: [pelts={count=2315, sum=11029, min=2, average=4.764147, max=10}, polts={count=2315, sum=11045, min=2, average=4.771058, max=10}]
 *
 * Most Frequent Letter picker
 *     * By avg guesses: [prost={count=2315, sum=10306, min=2, average=4.451836, max=11}, clapt={count=2315, sum=10317, min=2, average=4.456587, max=10}]
 *     * By max guesses: [milds={count=2315, sum=10601, min=2, average=4.579266, max=9}, tulpa={count=2315, sum=10626, min=2, average=4.590065, max=9}]
 */
public class PickerScorer {
    public static void main(String[] args) {
        PickMostFrequentLetters.init();
        List<String> startWords =
                List.of("perst", "plots", "clapt", "prost", "pelts", "polts", "milds", "tulpa", "arose", "arise", "named", "names");
                //Solver.ALLOWED_GUESSES;
        List<String> wordsToSolve = Solver.WORDS;

        long now = System.currentTimeMillis();
        Map<String, ConcurrentLinkedQueue<Integer>> startWordTries = new ConcurrentSkipListMap<>();
        startWords.forEach(startWord -> {
            startWordTries.put(startWord, new ConcurrentLinkedQueue<>());

            wordsToSolve.stream().parallel().forEach(w -> {
                        Solver solver = new Solver(
                                //new PickFirst()
                                new PickRandom()
                                //new PickMostFrequentLetters()
                        );
                        startWordTries.get(startWord).add(solver.solve(w));
                        solver.reset();
                    }
            );
        });

        System.out.println("Elapsed: " + (System.currentTimeMillis() - now) + " ms");

        Map<String, IntSummaryStatistics> startWordSummaries = toIntSummaryStatisticsMap(startWordTries);
        System.out.println("2 Lowest average guesses:\t" +
                startWordSummaries.entrySet().stream()
                        .sorted(Comparator.comparingDouble(e -> e.getValue().getAverage()))
                        .limit(2)
                        .collect(Collectors.toList()));

        System.out.println("2 Lowest max guesses:\t\t" +
                startWordSummaries.entrySet().stream()
                        .sorted(Comparator
                                .comparingDouble(PickerScorer::getMax)
                                .thenComparingDouble(PickerScorer::getAvg))
                        .limit(2)
                        .collect(Collectors.toList()));
    }

    private static Map<String, IntSummaryStatistics> toIntSummaryStatisticsMap(Map<String, ConcurrentLinkedQueue<Integer>> startWordTries) {
        Map<String, IntSummaryStatistics> map = new HashMap<>();
        startWordTries.forEach((k, v) -> map.put(k, v.stream().mapToInt(Integer::intValue).summaryStatistics()));
        return map;
    }

    private static int getMax(Map.Entry<String, IntSummaryStatistics> e) {
        return e.getValue().getMax();
    }

    private static double getAvg(Map.Entry<String, IntSummaryStatistics> e) {
        return e.getValue().getAverage();
    }
}
