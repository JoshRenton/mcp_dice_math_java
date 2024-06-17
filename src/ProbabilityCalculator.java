import Dice.*;

public class ProbabilityCalculator {
    public static void main(String[] args) {
        DiceBuilder diceBuilder = new DiceBuilder();
        Dice dice = diceBuilder.defaultAttackDice().build();
    }

    static int binomialCoefficient(int n, int k) {
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

    static double probabilityOfKSuccessesRollingNDice(Dice dice, int n, int k) {
        double successProbability = dice.getSuccessProbability();
        double failProbability = 1 - successProbability;

        return binomialCoefficient(n, k) * Math.pow(successProbability, k)
                * Math.pow(failProbability, n - k);
    }

    static double probabilityOfRCritsGivenKSuccesses(Dice dice, int n, int k) {
        double critProbability = dice.getCritProbability();
        double noCritProbability = 1 - critProbability;

        return binomialCoefficient(n, k) * Math.pow(critProbability, k)
                * Math.pow(noCritProbability, n - k);
    }

    // Probabilities of getting >= k successes for each 0 <= k <= n
    static double[] greaterThanProbabilities(Dice dice, int n) {
        double[] probabilities = new double[2 * n + 1];

        for (int k = 0; k <= n; k++) {
            double initial = probabilityOfKSuccessesRollingNDice(dice, n, k);
            for (int r = 0; r <= k; r++) {
                double crit = probabilityOfRCritsGivenKSuccesses(dice, k, r);
                for (int q = 0; q <= r; q++) {
                    double critSuccess = probabilityOfKSuccessesRollingNDice(dice, r, q);
                    probabilities[k + q] += initial * crit * critSuccess;
                }
            }
        }

        for (int i = 2 * n - 1; i >= 0; i--) {
            probabilities[i] += probabilities[i + 1];
        }

        return probabilities;
    }
}
