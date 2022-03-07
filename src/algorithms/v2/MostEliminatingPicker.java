package algorithms.v2;

import algorithms.Constraint;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 2 Lowest average guesses:	[clasp={count=2315, sum=10156, min=1, average=4.387041, max=11}, claps={count=2315, sum=10211, min=2, average=4.410799, max=11}]
 * 2 Lowest max guesses:		[plong={count=2315, sum=10388, min=2, average=4.487257, max=9}, gland={count=2315, sum=10394, min=1, average=4.489849, max=9}]
 *
 * Internle Words only:
 * 2 Lowest average guesses:	[laser={count=152, sum=402, min=1, average=2.644737, max=4}, alite={count=152, sum=409, min=1, average=2.690789, max=4}]
 * 2 Lowest max guesses:		[laser={count=152, sum=402, min=1, average=2.644737, max=4}, alite={count=152, sum=409, min=1, average=2.690789, max=4}]
 */
public class MostEliminatingPicker extends AbstractCandidatePicker {
    private static Map<String, Double> INITIAL_STATS;
    private static List<String> ANSWER_WORDS;
    private Map<String, Double> currentEliminationStats;

//    static {
//        try {
//            Map<String, Double> firstGuessStats = new HashMap<>();
//            Files.readAllLines(Paths.get("src/algorithms/v2/eliminations.txt"))
//                    .forEach(l -> {
//                        String[] parts = l.split(",");
//                        firstGuessStats.put(parts[0], Double.parseDouble(parts[1]));
//                    });
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    public MostEliminatingPicker(String startWord, List<String> guessWords, List<String> answerWords) {
        super(startWord);
        this.currentEliminationStats = new HashMap<>(INITIAL_STATS);
    }

    public static void init(List<String> guessWords, List<String> answerWords) {
        INITIAL_STATS = getUpdatedStats(guessWords, answerWords);
        ANSWER_WORDS = answerWords;
    }

    public static void outputFirstGuessEliminations(List<String> guessWords, List<String> targets, String outputFile) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
            for (Map.Entry<String, Double> e : getUpdatedStats(guessWords, targets).entrySet()) {
                writer.write(e.getKey() + ", " + e.getValue());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Map<String, Double> getUpdatedStats(List<String> guessWords, List<String> answerWords) {
        return guessWords
                .stream()
                .parallel()
                .collect(Collectors.toMap(
                        w -> w,
                        w -> answerWords.stream()
                                .mapToLong(answer -> {
                                    Constraint constraint = Constraint.fromTwoWords(w, answer);
                                    return guessWords.stream().filter(constraint::fails).count();
                                })
                                .average()
                                .orElseThrow())
                );
    }

    @Override
    public String pick(List<String> candidates) {
        if (candidates.size() < currentEliminationStats.size()) {
            currentEliminationStats = getUpdatedStats(candidates, ANSWER_WORDS);
        }

        return candidates
                .stream()
                .max(Comparator.comparingDouble(this::getWordScore))
                .orElseThrow();
    }

    private double getWordScore(String word) {
        return currentEliminationStats.get(word);
    }
}
