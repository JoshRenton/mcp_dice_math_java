import Dice.Die;
import Utility.ProbabilityCalculator;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

public class Runner {
    private static int numAtkDice;
    private static int numDefDice;
    private static Die atkDie;
    private static Die defDie;
    private static boolean critsExplodeAtk;
    private static boolean critsExplodeDef;
    private static Gui gui;
    public static void main(String[] args) {
        atkDie = new Die.DieBuilder().defaultAttackDie().build();
        defDie = new Die.DieBuilder().defaultDefenseDie().build();

        numAtkDice = 0;
        numDefDice = 0;

        critsExplodeAtk = true;
        critsExplodeDef = true;

        gui = new Gui();
        gui.showGui();
    }

    public static void updateAtkDie(String faceName) {
        atkDie.toggleSuccessfulFace(faceName);
        updateResults();
    }

    public static void updateDefDie(String faceName) {
        defDie.toggleSuccessfulFace(faceName);
        updateResults();
    }

    public static void toggleCritsExplodeAtk() {
        critsExplodeAtk = !critsExplodeAtk;
        updateResults();
    }

    public static void toggleCritsExplodeDef() {
        critsExplodeDef = !critsExplodeDef;
        updateResults();
    }

    private static int getSelectedNum(ActionEvent e) {
        JComboBox<Integer> input = (JComboBox<Integer>) e.getSource();
        return (int) input.getSelectedItem();
    }

    private static void updateResults() {
        double[] results = ProbabilityCalculator.cumulativeDmgProbabilities(atkDie, defDie,
                numAtkDice, numDefDice, critsExplodeAtk, critsExplodeDef);
        gui.displayResults(results);
    }

    // TODO: Consider moving these to Gui class
    public static class AtkListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            numAtkDice = getSelectedNum(e);
            updateResults();
        }
    }

    public static class DefListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            numDefDice = getSelectedNum(e);
            updateResults();
        }
    }
}
