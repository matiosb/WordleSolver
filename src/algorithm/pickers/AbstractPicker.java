package algorithm.pickers;

import java.util.List;

public abstract class AbstractPicker {
    protected List<String> candidates;

    public List<String> getCandidates() {
        return this.candidates;
    }

    public void setCandidates(List<String> candidates) {
        this.candidates = candidates;
    }

    public abstract String getStartWord();
    public abstract String pickNext();
}
