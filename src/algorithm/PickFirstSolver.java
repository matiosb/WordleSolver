package algorithm;

import java.util.List;

/**
 * Simple picker from alphabetically sorted list
 *     * By avg guesses: [perst={count=2315, sum=11008, min=2, average=4.755076, max=13}, plots={count=2315, sum=11017, min=2, average=4.758963, max=11}]
 *     * By max guesses: [pelts={count=2315, sum=11029, min=2, average=4.764147, max=10}, polts={count=2315, sum=11045, min=2, average=4.771058, max=10}]
 */
public class PickFirstSolver extends AbstractSolver {
    public PickFirstSolver() {
        super("perst");
    }

    public PickFirstSolver(String startWord) {
        super(startWord);
    }

    @Override
    protected void sortCandidates(List<String> candidates) {
        // no-op (original alphabetical order)
    }
}
