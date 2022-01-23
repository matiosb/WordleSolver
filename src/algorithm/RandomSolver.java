package algorithm;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class RandomSolver extends AbstractSolver {
    public RandomSolver() {
        super("perst");
    }
    public RandomSolver(String startWord) {
        super(startWord);
    }

    @Override
    protected void sortCandidates(List<String> candidates) {
        Collections.shuffle(this.candidates);
    }
}
