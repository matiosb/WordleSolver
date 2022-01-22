package algorithm.pickers;

public class PickFirst extends AbstractPicker {
    @Override
    public String getStartWord() {
        return "perst";
    }

    @Override
    public String pickNext() {
        if (candidates.isEmpty()) {
            throw new IllegalStateException("No more candidates!");
        }
        return candidates.get(0);
    }
}
