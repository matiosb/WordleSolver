package algorithms.v2;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * When guessing with all allowed words:
 * 2 Lowest average guesses:	[slant={count=2315, sum=9994, min=1, average=4.317063, max=11}, prost={count=2315, sum=10005, min=2, average=4.321814, max=11}]
 * 2 Lowest max guesses:		[spade={count=2315, sum=10070, min=1, average=4.349892, max=9}, sepad={count=2315, sum=10153, min=2, average=4.385745, max=9}]
 *
 * When only guessing with answer words:
 * 2 Lowest average guesses:	[slate={count=2315, sum=8415, min=1, average=3.634989, max=9}, trace={count=2315, sum=8422, min=1, average=3.638013, max=8}]
 * 2 Lowest max guesses:		[palsy={count=2315, sum=8613, min=1, average=3.720518, max=7}, craft={count=2315, sum=8634, min=1, average=3.729590, max=7}]
 */
public class MostFrequentLetterPicker extends AbstractCandidatePicker {
    private static final ConcurrentHashMap<Integer, int[]> CACHE = new ConcurrentHashMap<>();

    private int[] letterFrequencies;

    public MostFrequentLetterPicker() {
        super("slant");
    }

    public MostFrequentLetterPicker(String startWord) {
        super(startWord);
    }

    private static int[] calculateLetterFrequencies(List<String> candidates) {
        int hashcode = candidates.hashCode();
        if (CACHE.containsKey(hashcode)) {
            return CACHE.get(hashcode);
        }

        int[] frequencies = new int[256];
        for (String w : candidates) {
            for (char c : w.toCharArray()) {
                frequencies[c]++;
            }
        }

        CACHE.put(hashcode, frequencies);

        return frequencies;
    }

    @Override
    public String pick(List<String> candidates) {
        letterFrequencies = calculateLetterFrequencies(candidates);

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
