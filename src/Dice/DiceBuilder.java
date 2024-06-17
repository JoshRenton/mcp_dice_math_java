package Dice;

import java.util.ArrayList;
import Dice.Dice.Face;

public class DiceBuilder {
    private ArrayList<Face> successfulFaces;

    public DiceBuilder() {
        this.successfulFaces = new ArrayList<Face>();
    }

    private DiceBuilder addSuccessfulFace(Face face) {
        if (!this.successfulFaces.add(face)) {
            this.successfulFaces.add(face);
        }
        return this;
    }

    public DiceBuilder successfulHit() {
        return addSuccessfulFace(Face.HIT);
    }

    public DiceBuilder successfulBlock() {
        return addSuccessfulFace(Face.BLOCK);
    }

    public DiceBuilder successfulWild() {
        return addSuccessfulFace(Face.WILD);
    }

    public DiceBuilder successfulBlank() {
        return addSuccessfulFace(Face.BLANK);
    }

    public DiceBuilder successfulFail() {
        return addSuccessfulFace(Face.FAIL);
    }

    public DiceBuilder successfulCrit() {
        return addSuccessfulFace(Face.CRIT);
    }

    public DiceBuilder defaultAttackDice() {
        return this.successfulHit()
                .successfulWild()
                .successfulCrit();
    }

    public DiceBuilder defaultDefenseDice() {
        return this.successfulBlock()
                .successfulWild()
                .successfulCrit();
    }

    public Dice build() {
        return new Dice(successfulFaces);
    }
}
