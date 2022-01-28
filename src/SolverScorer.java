import algorithms.v1.AbstractSolver;
import algorithms.v2.*;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.stream.Collectors;

public class SolverScorer {
    public static void main(String[] args) {
        AbstractCandidatePicker hangOn = new MostFrequentLetterPicker();

        List<String> startWords =
                //List.of("slant", "spade", "sepad", "perst", "plots", "clapt", "prost", "pelts", "polts", "milds", "tulpa", "arose", "arise", "named", "names");
                //List.of("clasp", "claps");
                //List.of("slant");
                //List.of("perst","plots", "pelts", "polts");
                List.of("lares");
                //Solver.ALLOWED_GUESSES;
        List<String> wordsToSolve = AbstractSolver.WORDS;

        long now = System.currentTimeMillis();
        Map<String, ConcurrentLinkedQueue<Integer>> startWordTries = new ConcurrentSkipListMap<>();
        startWords.forEach(startWord -> {
            startWordTries.put(startWord, new ConcurrentLinkedQueue<>());

            wordsToSolve
                    .stream()
                    .parallel()
                    .forEach(w -> {
                        Solver solver =
                                //new Solver(new NaturalOrderPicker(startWord));
                                //new Solver(new MostFrequentLetterPicker(startWord));
                                new Solver(new MostEliminatingPicker(startWord));
                        startWordTries.get(startWord).add(solver.solve(w));
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
