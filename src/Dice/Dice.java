package Dice;

import java.util.ArrayList;

public class Dice {
    private ArrayList<Face> successfulFaces;

    // Used to specify custom successful faces
    protected Dice(ArrayList<Face> successfulFaces) {
        this.successfulFaces = successfulFaces;
    }

    public double getCritProbability() {
        return Face.CRIT.probability;
    }

    public void setSuccessfulFaces(ArrayList<Face> successfulFaces) {
        this.successfulFaces = successfulFaces;
    }

    public void addSuccessfulFace(Face face) {
        if (successfulFaces.contains(face)) {
            return;
        }

        this.successfulFaces.add(face);
    }

    // Return probability of getting a success
    public double getSuccessProbability() {
        double totalProbability = 0;
        for (Face face: successfulFaces) {
            totalProbability += face.probability;
        }
        return totalProbability;
    }

    protected enum Face {
        BLANK(0.25),
        HIT(0.25),
        BLOCK(0.125),
        WILD(0.125),
        CRIT(0.125),
        FAIL(0.125);

        private final double probability;

        Face(double probability) {
            this.probability = probability;
        }
    }
}
