package com.primenumbers;
import com.perftestanalysis.PerfTestAnalysis;
import java.util.Arrays;

public class PrimeNumbers {

    public static void main(String args[]) {
        long startTime, executionTime;
        int  primeCount;
        int  n = (int)1e8;
        double control[] = new double[20];
        double variant[] = new double[20];

        betterCountPrimes(n); betterCountPrimes(n);  // experience showed a startup cost
        for (int i=0; i<variant.length; i++){
            startTime = System.currentTimeMillis();
            betterCountPrimes(n);
            variant[i] = (System.currentTimeMillis() - startTime)/1000.0;
        }

        goodCountPrimes(n); goodCountPrimes(n);  // experience showed a startup cost
        for (int i=0; i<control.length; i++){
            startTime = System.currentTimeMillis();
            goodCountPrimes(n);
            control[i] = (System.currentTimeMillis() - startTime)/1000.0;
        }

        PerfTestAnalysis pta = new PerfTestAnalysis(variant, control);
        double confidence = 0.950;
        System.out.println("\nVARIANT < CONTROL; confidence=" + confidence + ": " + pta.isVariantLTControl(confidence));
        pta.printConfidenceInterval(1 - ((1-confidence) * 2.0));
    }


    public static int betterCountPrimes(int n) {
        // initially assume all integers are prime (i.e. NOT composite)
        boolean[] isComposite = new boolean[n+1];
        int       nbrOfPrimes = 0;

        // find non-primes <= n using Sieve of Eratosthenes
        int stoppingPoint = (int)Math.sqrt(n);
        for (int factor = 2; factor <= stoppingPoint; factor++) {
            if (!isComposite[factor]) {
                for (int j = factor; factor*j <= n; j++) {
                    isComposite[factor*j] = true;
                }
            }
        }

        // count primes
        int primes = 0;
        for (int i = 2; i <= n; i++) {
            if (!isComposite[i]) { primes++; };
        }
        return primes;
    }

    public static int goodCountPrimes(int n){
        // initially assume all integers are prime
        boolean[] isPrime = new boolean[n+1];
        for (int i = 2; i <= n; i++) {
            isPrime[i] = true;
        }

        // mark non-primes <= n using Sieve of Eratosthenes
        for (int factor = 2; factor*factor <= n; factor++) {
            if (isPrime[factor]) {
                for (int j = factor; factor*j <= n; j++) {
                    isPrime[factor*j] = false;
                }
            }
        }
        // count primes
        int primes = 0;
        for (int i = 2; i <= n; i++) {
            if (isPrime[i]) primes++;
        }
        return primes;
    }
}
