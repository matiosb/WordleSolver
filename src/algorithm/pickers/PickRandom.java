package algorithm.pickers;

import java.util.Random;

public class PickRandom extends AbstractPicker {
    private final Random random = new Random();

    @Override
    public String getStartWord() {
        return "clapt";
    }

    @Override
    public String pickNext() {
        return candidates.get(random.nextInt(candidates.size()));
    }
}
