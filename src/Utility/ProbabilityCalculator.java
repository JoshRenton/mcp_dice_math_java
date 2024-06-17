package Utility;

import Dice.Die;
public final class ProbabilityCalculator {
    private ProbabilityCalculator() {};

    public static double[] cumulativeDmgProbabilities(Die atkDie, Die defDie, int numAtkDice, int numDefDice) {
        double[] atkProba = successProbabilities(atkDie, numAtkDice);
        double[] defProba = successProbabilities(defDie, numDefDice);
        double[] dmgProba = dmgProbabilities(atkProba, defProba);
        return greaterThanProbabilities(dmgProba);
    }

    private static int binomialCoefficient(int n, int k) {
        int nCk = 1;

        // C(n,k) = C(n,n-k), so use lowest of 2 values
        if (k > n - k) {
            k = n - k;
        }

        // Calculate [n * ... * n-k] / k!
        for (int i = 1; i <= k; i++) {
            nCk *= (n - i + 1);
            nCk /= i;
        }

        return nCk;
    }

    private static double probabilityOfKSuccessesRollingNDice(Die die, int n, int k) {
        double successProbability = die.getSuccessProbability();
        double failProbability = 1 - successProbability;

        return binomialCoefficient(n, k) * Math.pow(successProbability, k)
                * Math.pow(failProbability, n - k);
    }

    private static double probabilityOfRCritsGivenKSuccesses(Die die, int n, int k) {
        double critProbability = die.getCritProbability();
        double noCritProbability = 1 - critProbability;

        return binomialCoefficient(n, k) * Math.pow(critProbability, k)
                * Math.pow(noCritProbability, n - k);
    }

    private static double[] greaterThanProbabilities(double[] probabilities) {
        return cumulativeProbabilities(probabilities, true);
    }

    private static double[] lessThanProbabilities(double[] probabilities) {
        return cumulativeProbabilities(probabilities, false);
    }

    private static double[] successProbabilities(Die die, int n) {
        double[] probabilities = new double[2 * n + 1];

        for (int k = 0; k <= n; k++) {
            double initial = probabilityOfKSuccessesRollingNDice(die, n, k);
            for (int r = 0; r <= k; r++) {
                double crit = probabilityOfRCritsGivenKSuccesses(die, k, r);
                for (int q = 0; q <= r; q++) {
                    double critSuccess = probabilityOfKSuccessesRollingNDice(die, r, q);
                    probabilities[k + q] += initial * crit * critSuccess;
                }
            }
        }

        return probabilities;
    }

    // Takes normal probabilities and returns cumulative probabilities
    private static double[] cumulativeProbabilities(double[] probabilities, boolean gt) {
        double[] cumulativeProbabilities = probabilities.clone();
        // If gt is true, return greater than probabilities
        if (gt) {
            for (int i = cumulativeProbabilities.length - 2; i >= 0; i--) {
                cumulativeProbabilities[i] += cumulativeProbabilities[i + 1];
            }
        } else {
            // Otherwise return less than
            for (int i = 1; i < cumulativeProbabilities.length; i++) {
                cumulativeProbabilities[i] += cumulativeProbabilities[i - 1];
            }
        }

        return cumulativeProbabilities;
    }

    // Calculates the probabilities of doing x damage
    private static double[] dmgProbabilities(double[] atkProbabilities, double[] defProbabilities) {
        double[] probabilities = new double[atkProbabilities.length];

        for (int i = 0; i < atkProbabilities.length; i++) {
            for (int j = 0; j < defProbabilities.length; j++) {
                if (i - j > 0) {
                    probabilities[i - j] += atkProbabilities[i] * defProbabilities[j];
                } else {
                    probabilities[0] += atkProbabilities[i] * defProbabilities[j];
                }
            }
        }
        return probabilities;
    }
}
