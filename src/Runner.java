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
    private static Gui gui;
    public static void main(String[] args) {
        atkDie = new Die.DieBuilder().defaultAttackDie().build();
        defDie = new Die.DieBuilder().defaultDefenseDie().build();

        numAtkDice = 0;
        numDefDice = 0;

        gui = new Gui();
        gui.showGui();
    }

    private static int getSelectedNum(ActionEvent e) {
        JComboBox<Integer> input = (JComboBox<Integer>) e.getSource();
        return (int) input.getSelectedItem();
    }

    public static class AtkListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            numAtkDice = getSelectedNum(e);
            double[] results = ProbabilityCalculator.cumulativeDmgProbabilities(atkDie, defDie,
                    numAtkDice, numDefDice);
            gui.displayResults(results);
        }
    }

    public static class DefListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            numDefDice = getSelectedNum(e);
            double[] results = ProbabilityCalculator.cumulativeDmgProbabilities(atkDie, defDie,
                    numAtkDice, numDefDice);
            gui.displayResults(results);
        }
    }
}
