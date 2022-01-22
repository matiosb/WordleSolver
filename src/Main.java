import algorithm.Constraint;
import algorithm.Solver;
import algorithm.pickers.PickMostFrequentLetters;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        Solver solver = new Solver(new PickMostFrequentLetters());
        String word = solver.nextSuggestion();
        System.out.println(word);

        Scanner s = new Scanner(System.in);
        System.out.print("\tEnter constraints (e.g. XNYXX): ");

        String input = s.nextLine();
        while (!input.equals("")) {
            word = solver.nextSuggestion(Constraint.fromConstraintString(word, input));
            System.out.println(word);

            System.out.print("\tEnter constraints (e.g. XNYXX): ");
            input = s.nextLine();
        }

    }
}
