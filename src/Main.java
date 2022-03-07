import algorithms.Constraint;
import algorithms.v2.MostEliminatingPicker;
import algorithms.v2.MostFrequentLetterPicker;
import algorithms.v2.Solver;

import java.util.*;

public class Main {
    public static void main(String[] args) throws ReflectiveOperationException {
        List<String> words = Solver.WORDS;

//        MostEliminatingPicker.init(words, words);
        MostFrequentLetterPicker.init(words);

        Solver solver =  new Solver("train", words, words, MostFrequentLetterPicker.class);
        String word = solver.nextSuggestion();
        System.out.println(word);

        Scanner s = new Scanner(System.in);
        System.out.print("\tEnter constraints (e.g. XNYXX): ");

        String input = s.nextLine();
        while (!input.equals("")) {
            try {
                word = solver.nextSuggestion(Constraint.fromConstraintString(word, input));
                System.out.println(word);
            } catch (NoSuchElementException e) {
                System.out.println("No more suggestions available!");
                break;
            }
            System.out.print("\tEnter constraints (e.g. XNYXX): ");
            input = s.nextLine();
        }
    }
}
