package algorithm.pickers;

import algorithm.Solver;

import java.util.*;

public class PickMostFrequentLetters extends AbstractPicker {
    public static List<String> startingCandidates = new ArrayList<>(Solver.ALLOWED_GUESSES);
    private static int[] letterFrequencies;

    static {
        calculateLetterFrequencies(startingCandidates);
        startingCandidates.sort(Comparator.comparingInt(PickMostFrequentLetters::getWordScore).reversed());
    }

    public static void init() {
        new PickMostFrequentLetters();
    }

    private static void calculateLetterFrequencies(List<String> candidates) {
        letterFrequencies = new int[256];
        for (String w : candidates) {
            for (char c : w.toCharArray()) {
                letterFrequencies[c]++;
            }
        }
    }

    private static int getWordScore(String word) {
        return word.chars().distinct().map(c -> letterFrequencies[c]).sum();
    }

    @Override
    public void setCandidates(List<String> candidates) {
        // ignore given candidates and use presorted list
        this.candidates = new ArrayList<>(startingCandidates);

    }

    @Override
    public String getStartWord() {
        return "prost";
    }

    @Override
    public String pickNext() {
        return this.candidates.get(0);
    }
}
