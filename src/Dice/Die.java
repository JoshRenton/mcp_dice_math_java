package Dice;

import java.util.ArrayList;

public class Die {
    private ArrayList<Face> successfulFaces;

    // Used to specify custom successful faces
    protected Die(ArrayList<Face> successfulFaces) {
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
        double totalProbability = 0.0;
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

    public static class DieBuilder {
        private final ArrayList<Face> successfulFaces;

        public DieBuilder() {
            successfulFaces = new ArrayList<Face>();
        }

        private DieBuilder addSuccessfulFace(Face face) {
            if (!successfulFaces.contains(face)) {
                successfulFaces.add(face);
            }
            return this;
        }

        public DieBuilder successfulHit() {
            return addSuccessfulFace(Face.HIT);
        }

        public DieBuilder successfulBlock() {
            return addSuccessfulFace(Face.BLOCK);
        }

        public DieBuilder successfulWild() {
            return addSuccessfulFace(Face.WILD);
        }

        public DieBuilder successfulBlank() {
            return addSuccessfulFace(Face.BLANK);
        }

        public DieBuilder successfulFail() {
            return addSuccessfulFace(Face.FAIL);
        }

        public DieBuilder successfulCrit() {
            return addSuccessfulFace(Face.CRIT);
        }

        public DieBuilder defaultAttackDie() {
            return this.successfulHit()
                    .successfulWild()
                    .successfulCrit();
        }

        public DieBuilder defaultDefenseDie() {
            return this.successfulBlock()
                    .successfulWild()
                    .successfulCrit();
        }

        public Die build() {
            return new Die(successfulFaces);
        }
    }
}
