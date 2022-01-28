package algorithms.v1;

import algorithms.Constraint;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractSolver {
    public static final List<String> WORDS = getWords();
    public static final List<String> ALLOWED_GUESSES = getAllowedGuesses();
    public static List<String> initialCandidates = null;
    protected List<String> candidates;
    protected String suggestedStartWord;

    protected AbstractSolver(String startWord) {
        this.suggestedStartWord = startWord;
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
        if (initialCandidates == null) {
            initialCandidates = new ArrayList<>(ALLOWED_GUESSES);
            sortCandidates(initialCandidates);
        }
        this.candidates = new ArrayList<>(initialCandidates);
    }

    public String nextSuggestion() {
        return nextSuggestion(null);
    }

    public String nextSuggestion(Constraint constraint) {
        // First suggestion
        if (constraint == null) {
            return this.suggestedStartWord;
        }

        // Eliminate words
        this.candidates.removeIf(constraint::fails);

        // Sort again
        sortCandidates(this.candidates);

        // Get next
        return candidates.get(0);
    }

    protected abstract void sortCandidates(List<String> candidates);
}
