package algorithms.v1;

import algorithms.v1.AbstractSolver;

import java.util.Collections;
import java.util.List;

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
