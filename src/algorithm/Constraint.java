package algorithm;

import java.util.Map;

public class Constraint {
    private static final Map<Character, ConstraintType> CONSTRAINT_STRING_TO_TYPE_MAP = Map.of(
            'X', ConstraintType.DOES_NOT_EXIST,
            'Y', ConstraintType.CORRECT_POSITION,
            'N', ConstraintType.INCORRECT_POSITION);

    private final ConstraintType[] constraintTypes;
    private final String source;

    private Constraint(String source, String val2, boolean asConstraintString) {
        this.source = source;
        this.constraintTypes = asConstraintString ? toConstraintTypes(val2) : toConstraintTypes(source, val2);
    }

    public static Constraint fromConstraintString(String source, String constraintString) {
        return new Constraint(source, constraintString, true);
    }

    public static Constraint fromTwoWords(String source, String target) {
        return new Constraint(source, target, false);
    }

    private static ConstraintType[] toConstraintTypes(String constraintString) {
        ConstraintType[] constraintTypes = new ConstraintType[constraintString.length()];

        for (int i = 0; i < constraintString.length(); i++) {
            constraintTypes[i] = CONSTRAINT_STRING_TO_TYPE_MAP.get(constraintString.charAt(i));
        }

        return constraintTypes;
    }

    private static ConstraintType[] toConstraintTypes(String source, String target) {
        ConstraintType[] constraintTypes = new ConstraintType[source.length()];

        for (int i = 0; i < source.length(); i++) {
            char c = source.charAt(i);
            int target_pos = target.indexOf(c);

            if (target.charAt(i) == c) {
                constraintTypes[i] = ConstraintType.CORRECT_POSITION;
            } else if (target_pos == -1) {
                constraintTypes[i] = ConstraintType.DOES_NOT_EXIST;
            } else {
                constraintTypes[i] = ConstraintType.INCORRECT_POSITION;
            }
        }

        return constraintTypes;
    }

    public boolean fails(String word) {
        for (int i = 0; i < constraintTypes.length; i++) {
            char c1 = source.charAt(i);
            char c2 = word.charAt(i);

            switch (this.constraintTypes[i]) {
                case INCORRECT_POSITION:
                    if (c2 == c1 || !word.contains(c1 + "")) return true;
                    break;
                case CORRECT_POSITION:
                    if (c2 != c1) return true;
                    break;
                case DOES_NOT_EXIST:
                    if (word.contains(c1 + "")) return true;
                    break;
                default:
                    throw new RuntimeException("Unexpected constraint type");
            }
        }

        return false;
    }

    private enum ConstraintType {
        DOES_NOT_EXIST,
        CORRECT_POSITION,
        INCORRECT_POSITION,
    }
}
