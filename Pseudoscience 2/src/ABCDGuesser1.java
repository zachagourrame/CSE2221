import components.simplereader.SimpleReader;
import components.simplereader.SimpleReader1L;
import components.simplewriter.SimpleWriter;
import components.simplewriter.SimpleWriter1L;
import components.utilities.FormatChecker;

/**
 * Approximates a constant using the de Jager formula and while-loops.
 *
 * @author Zacharia Agourrame
 *
 */
public final class ABCDGuesser1 {

    /**
     * No argument constructor--private to prevent instantiation.
     */
    private ABCDGuesser1() {
    }

    /**
     * Repeatedly asks the user for a positive real number until the user enters
     * one. Returns the positive real number.
     *
     * @param in
     *            the input stream
     * @param out
     *            the output stream
     * @return a positive real number entered by the user
     */
    private static double getPositiveDouble(SimpleReader in, SimpleWriter out) {
        out.print("Enter the constant μ to approximate: ");
        String input = in.nextLine();
        out.println("");
        double doublein = 0.0;
        if (FormatChecker.canParseDouble(input) && Double.parseDouble(input) > 0.0) {
            doublein = Double.parseDouble(input);
        } else {
            while (!FormatChecker.canParseDouble(input)
                    || Double.parseDouble(input) <= 0.0) {
                out.println("Error: Input is invalid!");
                out.print("Enter the constant μ to approximate:");
                input = in.nextLine();
                out.println("");
            }
            doublein = Double.parseDouble(input);
        }
        return doublein;
    }

    /**
     * Repeatedly asks the user for a positive real number not equal to 1.0
     * until the user enters one. Returns the positive real number.
     *
     * @param in
     *            the input stream
     * @param out
     *            the output stream
     * @return a positive real number not equal to 1.0 entered by the user
     */
    private static double getPositiveDoubleNotOne(SimpleReader in, SimpleWriter out) {
        out.print("Please enter a positive number that is not one: ");
        String input = in.nextLine();
        out.println("");
        double doublein = 0.0;
        if (FormatChecker.canParseDouble(input) && Double.parseDouble(input) != 1.0
                && Double.parseDouble(input) > 0.0) {
            doublein = Double.parseDouble(input);
        } else {
            while (!FormatChecker.canParseDouble(input)
                    || (Double.parseDouble(input) == 1.0)
                    || Double.parseDouble(input) <= 0.0) {
                out.println("Error: Input is invalid!");
                out.print("Please enter a positive number that is not one: ");
                input = in.nextLine();
                out.println("");
            }
            doublein = Double.parseDouble(input);
        }
        return doublein;
    }

    /**
     * Main method.
     *
     * @param args
     *            the command line arguments
     */
    public static void main(String[] args) {
        SimpleReader in = new SimpleReader1L();
        SimpleWriter out = new SimpleWriter1L();
        double u = getPositiveDouble(in, out);
        double w = getPositiveDoubleNotOne(in, out);
        double x = getPositiveDoubleNotOne(in, out);
        double y = getPositiveDoubleNotOne(in, out);
        double z = getPositiveDoubleNotOne(in, out);
        double minError = Double.MAX_VALUE;
        double bestApprox = 0;
        double bestA = 0, bestB = 0, bestC = 0, bestD = 0;
        final double[] exponents = { -5, -4, -3, -2, -1, -1.0 / 2, -1.0 / 3, -1.0 / 4, 0,
                1.0 / 4, 1.0 / 3, 1.0 / 2, 1, 2, 3, 4, 5 };
        int ai = 0;
        while (ai < exponents.length) {
            int bi = 0;
            while (bi < exponents.length) {
                int ci = 0;
                while (ci < exponents.length) {
                    int di = 0;
                    while (di < exponents.length) {
                        double approx = Math.pow(w, exponents[ai])
                                * Math.pow(x, exponents[bi]) * Math.pow(y, exponents[ci])
                                * Math.pow(z, exponents[di]);
                        double error = Math.abs((u - approx) / u);
                        if (error < minError) {
                            minError = error;
                            bestApprox = approx;
                            bestA = exponents[ai];
                            bestB = exponents[bi];
                            bestC = exponents[ci];
                            bestD = exponents[di];
                        }
                        di++;
                    }
                    ci++;
                }
                bi++;
            }
            ai++;
        }
        final int hundred = 100;

        out.println("a = " + bestA);
        out.println("b = " + bestB);
        out.println("c = " + bestC);
        out.println("d = " + bestD);
        out.println("Approximation: " + bestApprox);
        out.print("Relative error: ");
        out.print(minError * hundred, 2, false);
        out.println("%");

        /*
         * Close input and output streams
         */
        in.close();
        out.close();
    }

}
