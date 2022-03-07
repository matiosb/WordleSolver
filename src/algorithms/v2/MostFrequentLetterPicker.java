package algorithms.v2;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * When guessing with all allowed words:
 * 2 Lowest average guesses:	[slant={count=2315, sum=9994, min=1, average=4.317063, max=11}, prost={count=2315, sum=10005, min=2, average=4.321814, max=11}]
 * 2 Lowest max guesses:		[spade={count=2315, sum=10070, min=1, average=4.349892, max=9}, sepad={count=2315, sum=10153, min=2, average=4.385745, max=9}]
 *
 * When only guessing with answer words:
 * 2 Lowest average guesses:	[slate={count=2315, sum=8415, min=1, average=3.634989, max=9}, trace={count=2315, sum=8422, min=1, average=3.638013, max=8}]
 * 2 Lowest max guesses:		[palsy={count=2315, sum=8613, min=1, average=3.720518, max=7}, craft={count=2315, sum=8634, min=1, average=3.729590, max=7}]
 *
 * One-time letter frequency calculation (answer words only):
 * 2 Lowest average guesses:	[train={count=2315, sum=8605, min=1, average=3.717063, max=9}, crane={count=2315, sum=8629, min=1, average=3.727430, max=9}]
 * 2 Lowest max guesses:		[perch={count=2315, sum=8917, min=1, average=3.851836, max=7}, print={count=2315, sum=8642, min=1, average=3.733045, max=8}]
 *
 * Internle words:
 * 2 Lowest average guesses:	[laser={count=152, sum=410, min=1, average=2.697368, max=4}, morse={count=152, sum=414, min=1, average=2.723684, max=4}]
 * 2 Lowest max guesses:		[laser={count=152, sum=410, min=1, average=2.697368, max=4}, morse={count=152, sum=414, min=1, average=2.723684, max=4}]
 */
public class MostFrequentLetterPicker extends AbstractCandidatePicker {
    private static int[] INITIAL_FREQUENCIES;
    private final int[] letterFrequencies;

    public MostFrequentLetterPicker(String startWord, List<String> guessWords, List<String> answerWords) {
        super(startWord);
        letterFrequencies = INITIAL_FREQUENCIES.clone();
    }

    public static void init(List<String> answerWords) {
        INITIAL_FREQUENCIES = calculateLetterFrequencies(answerWords);
    }

    private static int[] calculateLetterFrequencies(List<String> candidates) {
        int[] frequencies = new int[256];
        for (String w : candidates) {
            for (char c : w.toCharArray()) {
                frequencies[c]++;
            }
        }

        return frequencies;
    }

    @Override
    public String pick(List<String> candidates) {
        // To update letter frequency on every pick, uncomment
        //letterFrequencies = calculateLetterFrequencies(candidates);

        return candidates
                .stream()
                .max(Comparator.comparingDouble(this::getWordScore))
                .orElseThrow();
    }

    /**
     * - Assigns a score based on letter frequency in remaining candidates.
     * - Scores distinct letters only
     * - Interesting case: "layer" - there are many words matching la*er in the entire guess set which get equal scores.
     *   This causes "layer" to be picked last, alphabetically.
     * @param word Word to score
     * @return a int score
     */
    private int getWordScore(String word) {
        return word.chars().distinct().map(c -> letterFrequencies[c]).sum();
    }
}
