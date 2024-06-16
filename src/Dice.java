public class Dice {
    private Face[] successfulFaces;

    // Default constructor
    public Dice() {
        successfulFaces = new Face[]{Face.HIT, Face.WILD, Face.CRIT};
    }

    // Used to specify custom successful faces
    public Dice(Face[] successfulFaces) {
        this.successfulFaces = successfulFaces;
    }

    public double getFaceProbability(Face face) {
        return face.probability;
    }

    // Return probability of getting a success
    public double getTotalSuccessProbability() {
        double totalProbability = 0;
        for (Face face: successfulFaces) {
            totalProbability += face.probability;
        }
        return totalProbability;
    }

    public enum Face {
        BLANK(0.25),
        HIT(0.25),
        BLOCK(0.125),
        WILD(0.125),
        CRIT(0.125),
        FAIL(0.125);

        public final double probability;

        Face(double probability) {
            this.probability = probability;
        }
    }
}
