import static org.junit.Assert.assertEquals;

import java.util.Set;

import org.junit.Test;

import components.set.Set1L;
import components.simplereader.SimpleReader;
import components.simplereader.SimpleReader1L;
import components.simplewriter.SimpleWriter;
import components.simplewriter.SimpleWriter1L;

public class StringReassemblyTest {

    // Tests combination with a partial overlap
    @Test
    public void combinationTest1() {
        String str = StringReassembly.combination("abcde", "defgh", 2);
        assertEquals("abcdefgh", str);
    }

    // Tests combination where str1 is fully inside the overlap
    @Test
    public void combinationTest2() {
        String str = StringReassembly.combination("123456", "1234567890", 6);
        assertEquals("1234567890", str);
    }

    // Tests adding strings where shorter ones get replaced by a longer one
    @Test
    public void addToSetAvoidingSubstringsTest1() {
        String str1 = "Go bucks!";
        String str2 = "bucks!";
        String str3 = "Go bucks! Beat Michigan!";

        Set<String> ans = new Set1L<>();
        Set<String> test = new Set1L<>();

        ans.add(str3);
        ans.add("5");

        StringReassembly.addToSetAvoidingSubstrings(test, str1);
        StringReassembly.addToSetAvoidingSubstrings(test, str2);
        StringReassembly.addToSetAvoidingSubstrings(test, str3);
        StringReassembly.addToSetAvoidingSubstrings(test, "5");
        StringReassembly.addToSetAvoidingSubstrings(test, "b");

        assertEquals(ans, test);
    }

    // Tests adding a substring of an existing element, set should not change
    @Test
    public void addToSetAvoidingSubstringsTest2() {
        Set<String> ans = new Set1L<>();
        Set<String> test = new Set1L<>();

        ans.add("Beat Michigan");
        test.add("Beat Michigan");

        StringReassembly.addToSetAvoidingSubstrings(test, "Michigan");

        assertEquals(ans, test);
    }

    // Tests linesFromInput on cheer-8-2.txt
    @Test
    public void linesFromInputTest1() {
        SimpleReader in = new SimpleReader1L("cheer-8-2.txt");

        Set<String> testSet = StringReassembly.linesFromInput(in);
        Set<String> ansSet = new Set1L<>();

        ansSet.add("Bucks -- Beat");
        ansSet.add("Go Bucks");
        ansSet.add("o Bucks -- B");
        ansSet.add("Beat Mich");
        ansSet.add("Michigan~");

        assertEquals(ansSet, testSet);

        in.close();
    }

    // Tests linesFromInput with a substring line and a duplicate line
    @Test
    public void linesFromInputTest2() {
        // create a small input file for this test
        SimpleWriter fileOut = new SimpleWriter1L("linesFromInputTest2.txt");
        fileOut.println("hello world");
        fileOut.println("hello");
        fileOut.println("goodbye");
        fileOut.println("goodbye");
        fileOut.close();

        SimpleReader in = new SimpleReader1L("linesFromInputTest2.txt");

        Set<String> testSet = StringReassembly.linesFromInput(in);
        Set<String> ansSet = new Set1L<>();

        ansSet.add("hello world");
        ansSet.add("goodbye");

        assertEquals(ansSet, testSet);

        in.close();
    }

    // Tests printWithLineSeparators with multiple separators
    @Test
    public void printWithLineSeparatorsTest1() {
        String text = "abc~edf~fhkjsahfkjahfl~";

        SimpleWriter out = new SimpleWriter1L("printWithLineSeparatorsTest.txt");
        StringReassembly.printWithLineSeparators(text, out);
        out.close();

        SimpleReader in = new SimpleReader1L("printWithLineSeparatorsTest.txt");

        assertEquals("abc", in.nextLine());
        assertEquals("edf", in.nextLine());
        assertEquals("fhkjsahfkjahfl", in.nextLine());

        in.close();
    }

    // Tests printWithLineSeparators with no in the text
    @Test
    public void printWithLineSeparatorsTest2() {
        String text = "Go Bucks";

        SimpleWriter out = new SimpleWriter1L("printWithLineSeparatorsTest2.txt");
        StringReassembly.printWithLineSeparators(text, out);
        out.close();

        SimpleReader in = new SimpleReader1L("printWithLineSeparatorsTest2.txt");

        assertEquals("Go Bucks", in.nextLine());

        in.close();
    }
}
