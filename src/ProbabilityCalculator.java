import java.util.Arrays;

public class ProbabilityCalculator {
    // Should never create an instance of this class
    public static void main(String[] args) {
        Dice dice = new Dice();
        double[] cumulativeProbabilities = greaterThanProbabilities(dice, 10);
        System.out.println(Arrays.toString(cumulativeProbabilities));
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

    // Probabilities of getting >= k successes for each 0 <= k <= n
    static double[] greaterThanProbabilities(Dice dice, int n) {
        double[] cumulativeProbabilities = new double[n + 1];
        double total = 0.0;

        for (int k = 0; k <= n; k++) {
            total += probabilityOfKSuccessesRollingNDice(dice, n, k);
            cumulativeProbabilities[n - k] = total;
        }

        return cumulativeProbabilities;
    }
}
