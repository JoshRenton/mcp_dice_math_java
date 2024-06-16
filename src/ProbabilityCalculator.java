public class ProbabilityCalculator {
    // Should never create an instance of this class
    private ProbabilityCalculator() {};
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
}
