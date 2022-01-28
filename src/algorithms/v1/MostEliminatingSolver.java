package algorithms.v1;

import algorithms.Constraint;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * 2 Lowest average guesses:	[clasp={count=2315, sum=10156, min=1, average=4.387041, max=11}, claps={count=2315, sum=10211, min=2, average=4.410799, max=11}]
 * 2 Lowest max guesses:		[plong={count=2315, sum=10388, min=2, average=4.487257, max=9}, gland={count=2315, sum=10394, min=1, average=4.489849, max=9}]
 */
public class MostEliminatingSolver extends AbstractSolver {
    private static final Map<String, IntSummaryStatistics> ELIMINATION_STATS = new HashMap<>();
    private static boolean isInited = false;

    public static void init() {
        try {
            Files.readAllLines(Paths.get("src/algorithms/eliminations.txt"))
                    .forEach(l -> {
                        String[] parts = l.split(",");
                        ELIMINATION_STATS.put(
                                parts[0],
                                Arrays.stream(parts).skip(1).mapToInt(Integer::parseInt).summaryStatistics()
                        );
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }

        new MostEliminatingSolver();
        isInited = true;
    }

    public MostEliminatingSolver() {
        super("prost");
    }

    public MostEliminatingSolver(String startWord) {
        super(startWord);
    }

    @Override
    protected void sortCandidates(List<String> candidates) {
        candidates.sort(Comparator.comparingDouble(this::getWordScore).reversed());
    }

    private double getWordScore(String word) {
        if (!isInited) {
            return ELIMINATION_STATS.get(word).getAverage();
        } else {
            return WORDS.stream()
                    .mapToDouble(w -> this.candidates.stream()
                            .filter(c -> Constraint.fromTwoWords(word, w).fails(c))
                            .count()
                    )
                    .average()
                    .orElseThrow();
        }
    }
}
