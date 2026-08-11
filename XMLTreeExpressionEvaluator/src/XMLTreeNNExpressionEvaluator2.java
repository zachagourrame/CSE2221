import components.naturalnumber.NaturalNumber;
import components.naturalnumber.NaturalNumber2;
import components.simplereader.SimpleReader;
import components.simplereader.SimpleReader1L;
import components.simplewriter.SimpleWriter;
import components.simplewriter.SimpleWriter1L;
import components.utilities.Reporter;
import components.xmltree.XMLTree;
import components.xmltree.XMLTree1;

/**
 * Program to evaluate XMLTree expressions of {@code int}.
 *
 * @author Zacharia Agourrame
 *
 */
public final class XMLTreeNNExpressionEvaluator2 {

    /**
     * Private constructor so this utility class cannot be instantiated.
     */
    private XMLTreeNNExpressionEvaluator2() {
    }

    /**
     * Evaluate the given expression.
     *
     * @param exp
     *            the {@code XMLTree} representing the expression
     * @return the value of the expression
     * @requires <pre>
     * [exp is a subtree of a well-formed XML arithmetic expression]  and
     *  [the label of the root of exp is not "expression"]
     * </pre>
     * @ensures evaluate = [the value of the expression]
     */
    private static NaturalNumber evaluate(XMLTree exp) {
        assert exp != null : "Violation of: exp is not null";

        NaturalNumber result;

        if (exp.label().equals("number")) {
            // base case: leaf node holding a number
            result = new NaturalNumber2(exp.attributeValue("value"));
        } else {
            // recursive case: evaluate both children first
            NaturalNumber left = evaluate(exp.child(0));
            NaturalNumber right = evaluate(exp.child(1));

            // apply the operator at the root
            if (exp.label().equals("plus")) {
                left.add(right);
            } else if (exp.label().equals("minus")) {
                // subtract requires this >= n
                if (left.compareTo(right) < 0) {
                    Reporter.fatalErrorToConsole(
                            "Error: subtraction result would be negative");
                }
                left.subtract(right);
            } else if (exp.label().equals("times")) {
                left.multiply(right);
            } else if (exp.label().equals("divide")) {
                // divide requires n > 0
                if (right.isZero()) {
                    Reporter.fatalErrorToConsole("Error: division by zero");
                }
                left.divide(right);
            }

            result = left;
        }

        return result;
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

        out.print("Enter the name of an expression XML file: ");
        String file = in.nextLine();
        while (!file.equals("")) {
            XMLTree exp = new XMLTree1(file);
            out.println(evaluate(exp.child(0)));
            out.print("Enter the name of an expression XML file: ");
            file = in.nextLine();
        }

        in.close();
        out.close();
    }

}
