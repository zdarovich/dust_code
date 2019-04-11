package algorithms.dancers;

/**
 * API specification for the objects
 * representing various dancers.
 */
public interface Dancer {
    public enum Gender {
        MALE, FEMALE
    }

    public int getID();
    public Gender getGender();
    public int getHeight();
}
