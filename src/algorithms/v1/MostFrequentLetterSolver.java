package algorithms.v1;

import java.util.Comparator;
import java.util.List;

/**
 * 2 Lowest average guesses:	[prost={count=2315, sum=10010, min=2, average=4.323974, max=11}, perst={count=2315, sum=10037, min=2, average=4.335637, max=11}]
 * 2 Lowest max guesses:		[tulpa={count=2315, sum=10246, min=2, average=4.425918, max=10}, milds={count=2315, sum=10258, min=2, average=4.431102, max=10}]
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
