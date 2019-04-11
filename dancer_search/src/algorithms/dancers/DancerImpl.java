package algorithms.dancers;

public class DancerImpl implements Dancer {

    private int id;
    private Gender gender;
    private int height;

    public DancerImpl(int id, Gender gender, int height) {
        this.id = id;
        this.gender = gender;
        this.height = height;
    }

    @Override
    public int getID() {
        return this.id;
    }

    @Override
    public Gender getGender() {
        return this.gender;
    }

    @Override
    public int getHeight() {
        return this.height;
    }

    @Override
    public String toString() {
        return "Dancer id " + id + " with gender " + gender + " and height " + height;
    }
}
