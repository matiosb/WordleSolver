package algorithms.v2;

import algorithms.Constraint;

import java.awt.event.TextEvent;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Solver {
    public static final List<String> INTERNLE_WORDS = getInternleWords();
    public static final List<String> WORDS = getWords();
    public static final List<String> ALLOWED_GUESSES = getAllowedGuesses(WORDS);
    private final List<String> answerWords;
    private final List<String> guessWords;
    private final List<String> candidates;
    private final AbstractCandidatePicker picker;

    public Solver(String startWord,
                  List<String> guessWords,
                  List<String> answerWords,
                  Class<? extends AbstractCandidatePicker> pickerClass)
            throws ReflectiveOperationException {

        Constructor<? extends AbstractCandidatePicker> ctor = pickerClass.getConstructor(
                String.class,
                List.class,
                List.class
        );
        this.picker = ctor.newInstance(startWord, guessWords, answerWords);

        this.answerWords = answerWords;
        this.guessWords = guessWords;
        this.candidates = new ArrayList<>(guessWords);
    }

    private static List<String> getInternleWords() {
        return readFile("src/internle_words.txt");
    }

    private static List<String> getWords() {
        return readFile("src/words.txt");
    }

    private static List<String> getAllowedGuesses(List<String> answerWords) {
        List<String> allowedGuesses = readFile("src/guesses.txt");
        allowedGuesses.addAll(answerWords);

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
