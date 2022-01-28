package algorithms.v2;

import algorithms.Constraint;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Solver {
    public static final List<String> WORDS = getWords();
    public static final List<String> ALLOWED_GUESSES = WORDS;   //getAllowedGuesses();  // uncomment if all 12k words should be allowed in guesses

    private final AbstractCandidatePicker picker;
    protected List<String> candidates;


    public Solver(AbstractCandidatePicker picker) {
        this.picker = picker;
        this.candidates = new ArrayList<>(ALLOWED_GUESSES);
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

    public String nextSuggestion() {
        return nextSuggestion(null);
    }

    public String nextSuggestion(Constraint constraint) {
        // First suggestion
        if (constraint == null) {
            return picker.startWord();
        }

        // Eliminate words
        candidates.removeIf(constraint::fails);

        // Get next
        return picker.pick(candidates);
    }
}
