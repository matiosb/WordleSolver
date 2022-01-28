package algorithms.v2;

import algorithms.Constraint;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 2 Lowest average guesses:	[clasp={count=2315, sum=10156, min=1, average=4.387041, max=11}, claps={count=2315, sum=10211, min=2, average=4.410799, max=11}]
 * 2 Lowest max guesses:		[plong={count=2315, sum=10388, min=2, average=4.487257, max=9}, gland={count=2315, sum=10394, min=1, average=4.489849, max=9}]
 */
public class MostEliminatingPicker extends AbstractCandidatePicker {
    private static final Map<Integer, Map<String, Double>> CACHE = new ConcurrentHashMap<>();

    private Map<String, Double> currentEliminationStats;

    static {
        try {
            Map<String, Double> firstGuessStats = new HashMap<>();
            Files.readAllLines(Paths.get("src/algorithms/eliminations.txt"))
                    .forEach(l -> {
                        String[] parts = l.split(",");
                        firstGuessStats.put(parts[0], Double.parseDouble(parts[1]));
                    });

            CACHE.put(new ArrayList<>(Solver.ALLOWED_GUESSES).hashCode(), firstGuessStats);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public MostEliminatingPicker(String startWord) {
        super(startWord);
    }

    @Override
    public String pick(List<String> candidates) {
        currentEliminationStats = getUpdatedStats(candidates);

        return candidates
                .stream()
                .max(Comparator.comparingDouble(this::getWordScore))
                .orElseThrow();
    }

    // TODO: There's an NPE thrown here. Need to debug.
    private Map<String, Double> getUpdatedStats(List<String> candidates) {
        int hashcode = candidates.hashCode();
        if (CACHE.containsKey(hashcode)) {
            return CACHE.get(hashcode);
        }
        Map<String, Double> stats = candidates
                .stream()
                .parallel()
                .collect(Collectors.toMap(
                        w -> w,
                        w -> Solver.WORDS.stream()
                                .mapToLong(answer -> {
                                    Constraint constraint = Constraint.fromTwoWords(w, answer);
                                    return candidates.stream().filter(constraint::fails).count();
                                })
                                .average()
                                .orElseThrow())
                );

        CACHE.put(candidates.hashCode(), stats);

        return stats;
    }

    private double getWordScore(String word) {
        return currentEliminationStats.get(word);
    }
}
