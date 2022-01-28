package algorithms.v2;

import java.util.List;

public abstract class AbstractCandidatePicker {
    private final String startWord;

    protected AbstractCandidatePicker(String startWord) {
        this.startWord = startWord;
    }

    public String startWord() {
        return this.startWord;
    }

    public abstract String pick(List<String> candidates);
}
