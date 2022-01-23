package algorithm;

import java.util.Comparator;
import java.util.List;

/**
 * Most Frequent Letter picker
 *     * By avg guesses: [prost={count=2315, sum=10306, min=2, average=4.451836, max=11}, clapt={count=2315, sum=10317, min=2, average=4.456587, max=10}]
 *     * By max guesses: [milds={count=2315, sum=10601, min=2, average=4.579266, max=9}, tulpa={count=2315, sum=10626, min=2, average=4.590065, max=9}]
 */
public class MostFrequentLetterSolver extends AbstractSolver {
    private int[] letterFrequencies;

    public static void init() {
        new MostFrequentLetterSolver();
    }

    public MostFrequentLetterSolver() {
        super("prost");
    }

    public MostFrequentLetterSolver(String startWord) {
        super(startWord);
    }

    private void calculateLetterFrequencies(List<String> candidates) {
        letterFrequencies = new int[256];
        for (String w : candidates) {
            for (char c : w.toCharArray()) {
                letterFrequencies[c]++;
            }
        }
    }

    @Override
    protected void sortCandidates(List<String> candidates) {
        calculateLetterFrequencies(candidates);
        candidates.sort(Comparator.comparingInt(this::getWordScore).reversed());
    }

    private int getWordScore(String word) {
        return word.chars().distinct().map(c -> letterFrequencies[c]).sum();
    }
}
