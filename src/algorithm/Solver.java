package algorithm;

import algorithm.pickers.AbstractPicker;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class Solver {
    public static final List<String> WORDS = getWords();
    public static final List<String> ALLOWED_GUESSES = getAllowedGuesses();
    private final AbstractPicker candidatePicker;

    public Solver(AbstractPicker picker) {
        this.candidatePicker = picker;
        reset();
    }

    private static List<String> getWords() {
        return readFile("src/words.txt");
    }

    private static List<String> getAllowedGuesses() {
        List<String> allowedGuesses = readFile("src/guesses.txt");
        allowedGuesses.addAll(WORDS);
        allowedGuesses.sort(Comparator.naturalOrder());
        return allowedGuesses;
    }

    private static List<String> readFile(String url) {
        try {
            return Files.readAllLines(Paths.get(url))
                    .stream()
                    .map(String::toLowerCase)
                    .map(String::trim)
                    .sorted()
                    .distinct()
                    .collect(Collectors.toList()
                    );
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public int solve(String target) {
        int tries = 1;
        String word = nextSuggestion();
        while (!word.equals(target)) {
            tries++;
            word = nextSuggestion(Constraint.fromTwoWords(word, target));
        }

        return tries;
    }

    public void reset() {
        this.candidatePicker.setCandidates(new ArrayList<>(ALLOWED_GUESSES));
    }

    public String nextSuggestion() {
        return nextSuggestion(null);
    }

    public String nextSuggestion(Constraint constraint) {
        // First suggestion
        if (constraint == null) {
            return this.candidatePicker.getStartWord();
        }

        // Eliminate words
        this.candidatePicker.getCandidates().removeIf(constraint::fails);

        // Get next
        return candidatePicker.pickNext();
    }
}
