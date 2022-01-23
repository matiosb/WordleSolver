import algorithm.AbstractSolver;
import algorithm.MostFrequentLetterSolver;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.stream.Collectors;

public class SolverScorer {
    public static void main(String[] args) {
        MostFrequentLetterSolver.init();

        List<String> startWords =
                List.of("perst", "plots", "clapt", "prost", "pelts", "polts", "milds", "tulpa", "arose", "arise", "named", "names");
                //AbstractSolver.ALLOWED_GUESSES;
        List<String> wordsToSolve = AbstractSolver.WORDS;

        long now = System.currentTimeMillis();
        Map<String, ConcurrentLinkedQueue<Integer>> startWordTries = new ConcurrentSkipListMap<>();
        startWords.forEach(startWord -> {
            startWordTries.put(startWord, new ConcurrentLinkedQueue<>());

            wordsToSolve.stream().parallel().forEach(w -> {
                        AbstractSolver solver = new MostFrequentLetterSolver(startWord);
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
                                .comparingDouble(SolverScorer::getMax)
                                .thenComparingDouble(SolverScorer::getAvg))
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
