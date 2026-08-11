import java.util.Comparator;

import components.map.Map;
import components.map.Map1L;
import components.queue.Queue;
import components.queue.Queue1L;
import components.set.Set;
import components.set.Set1L;
import components.simplereader.SimpleReader;
import components.simplereader.SimpleReader1L;
import components.simplewriter.SimpleWriter;
import components.simplewriter.SimpleWriter1L;

/**
 * Glossary Project.
 *
 * @author Zacharia Agourrame
 */
public final class Glossary {

    /**
     * Private constructor so this utility class can't be instantiated.
     */
    private Glossary() {
    }

    /**
     * Sorts Alphabetically
     */
    public static class AlphaOrder implements Comparator<String> {
        @Override
        public int compare(String a, String b) {
            // ignore case so "Apple" and "apple" sort next to each other
            return a.compareToIgnoreCase(b);
        }
    }

    /**
     * Fills {@code chars} with the unique characters found in {@code text}.
     *
     * @param text
     *            the given {@code String}
     * @param chars
     *            the {@code Set} to be replaced
     * @replaces charSet
     * @ensures charSet = entries(text)
     */
    public static void fillCharSet(String text, Set<Character> chars) {
        assert text != null : "Violation of: text is not null";
        assert chars != null : "Violation of: chars is not null";

        // start with an empty set
        chars.clear();

        // loop through every character in the string
        for (int i = 0; i < text.length(); i++) {
            char ch = text.charAt(i);
            // only add if it isn't already in the set
            if (!chars.contains(ch)) {
                chars.add(ch);
            }
        }
    }

    /**
     * Returns the next element (word or block of separators) starting at
     * {@code start} in {@code source}, using {@code breaks} to distinguish
     * separator characters from word characters.
     *
     * @param source
     *            the string being scanned
     * @param start
     *            index at which to begin
     * @param breaks
     *            set of characters that are separators
     * @return the next element
     * @requires 0 <= start < |source|
     * @ensures extractElement is the longest prefix of source starting at start
     *          whose characters are either all in breaks or all not in breaks
     */
    public static String extractElement(String source, int start, Set<Character> breaks) {
        assert source != null : "Violation of: source is not null";
        assert breaks != null : "Violation of: breaks is not null";
        assert 0 <= start : "Violation of: 0 <= start";
        assert start < source.length() : "Violation of: start < |source|";

        // holds the element we build up character by character
        String element = "";
        // how far past
        int offset = 0;

        // check if we're starting on a separator or a word character
        if (breaks.contains(source.charAt(start))) {
            // grab characters as long as they're separators
            while (offset + start < source.length()
                    && breaks.contains(source.charAt(start + offset))) {
                element += source.charAt(start + offset);
                offset++;
            }
        } else {
            // grab characters as long if they're not separators
            while (offset + start < source.length()
                    && !breaks.contains(source.charAt(start + offset))) {
                element += source.charAt(start + offset);
                offset++;
            }
        }

        return element;
    }

    /**
     * Writes the opening HTML tags with the given page {@code title}.
     *
     * @param file
     *            output stream to write to
     * @param title
     *            page title
     * @requires file.is_open
     * @ensures the html opening tags with title are written to file
     */
    public static void writeHTMLStart(SimpleWriter file, String title) {
        assert file != null : "Violation of: file is not null";
        assert title != null : "Violation of: title is not null";

        // print the HTML header stuff to the file
        file.println("<!DOCTYPE html>");
        file.println("<html>");
        file.println("<head>");
        file.println("<title>" + title + "</title>");
        file.println("</head>");
        file.println("<body>");
    }

    /**
     * Writes the closing HTML tags.
     *
     * @param file
     *            output stream to write to
     * @requires file.is_open
     * @ensures the html closing tags are written to file
     */
    public static void writeHTMLEnd(SimpleWriter file) {
        assert file != null : "Violation of: file is not null";

        // close the body and html taags
        file.println("</body>");
        file.println("</html>");
    }

    /**
     * Writes the HTML page for a single glossary term.
     *
     *
     * @param file
     *            output stream to write to
     * @param term
     *            the glossary term
     * @param meaning
     *            the term's definition
     * @param allTerms
     *            map of every term in the glossary (used to detect links)
     * @requires file.is_open
     * @ensures file contains an html page for term with its meaning, where any
     *          word in meaning that is a key is a hyperlink
     */
    public static void buildTermPage(SimpleWriter file, String term, String meaning,
            Map<String, String> allTerms) {
        assert file != null : "Violation of: file is not null";
        assert term != null : "Violation of: term is not null";
        assert meaning != null : "Violation of: meaning is not null";
        assert allTerms != null : "Violation of: allTerms is not null";

        // characters that count as word separators
        final String separators = " \t\n\r,.;:!?()-\"'";
        Set<Character> separatorSet = new Set1L<>();
        // build a set version
        fillCharSet(separators, separatorSet);

        // write the top of the html page with the term
        writeHTMLStart(file, term);

        // print the term at the top, red, bold, and italic
        file.println("<h2>");
        file.println("<b>");
        file.println("<i>");
        file.print("<font color=\"red\">");
        file.print(term);
        file.println("</font>");
        file.println("</i>");
        file.println("</b>");
        file.println("</h2>");
        // indent the definition
        file.println("<blockquote>");

        // walk through the whole definition, one element at a time
        int pos = 0;
        while (pos < meaning.length()) {
            String part = extractElement(meaning, pos, separatorSet);
            // move forward by the length of the element
            pos += part.length();

            // if the element is another glossary term make it a link
            if (allTerms.hasKey(part)) {
                file.print("<a href=\"" + part + ".html\">" + part + "</a>");
            } else {
                // otherwise just print the element
                file.print(part);
            }
        }

        // end the current line and close the definition block
        file.println();
        file.println("</blockquote>");
        file.println("<hr>");
        // add a link, linking to the index page
        file.println("<p>");
        file.println("return to");
        file.println("<a href = \"index.html\">index</a>");
        file.println("</p>");

        // close out the html tags
        writeHTMLEnd(file);
    }

    /**
     * Writes the index page listing every term alphabetically, with a link to
     * each term's page.
     *
     * @param file
     *            output stream to write to
     * @param termQueue
     *            queue of every term in the glossary
     * @updates termQueue
     * @requires file is open
     * @ensures termQueue is sorted alphabetically
     */
    public static void buildIndexPage(SimpleWriter file, Queue<String> termQueue) {
        assert file != null : "Violation of: file is not null";
        assert termQueue != null : "Violation of: termQueue is not null";

        // sort the terms alphabetically, ignoring capitals
        Comparator<String> cmp = new AlphaOrder();
        termQueue.sort(cmp);

        // write the top of the index page
        writeHTMLStart(file, "Glossary");

        // header for the page
        file.println("<h2>Glossary</h2>");
        file.println("<hr>");
        file.println("<h3>Index</h3>");
        // start the terms
        file.println("<ul>");
        // loop through every term and write a list item with a link
        for (String entry : termQueue) {
            file.println("<li>");
            file.println("<a href=\"" + entry + ".html\">" + entry + "</a>");
            file.println("</li>");
        }
        // close the list
        file.println("</ul>");

        // close out the html tags
        writeHTMLEnd(file);
    }

    /**
     * Reads term/definition pairs from {@code input} into {@code glossary}
     *
     *
     * @param input
     *            input stream to read from
     * @param glossary
     *            map to populate with term/definition pairs
     * @param termQueue
     *            queue to populate with terms in insertion order
     * @updates input, glossary, termQueue
     * @requires input is in the format specified by the project
     * @ensures glossary and termQueue contain every term/definition pair from
     *          input
     */
    public static void readGlossary(SimpleReader input, Map<String, String> glossary,
            Queue<String> termQueue) {
        assert input != null : "Violation of: input is not null";
        assert input.isOpen() : "Violation of: input.is_open";
        assert glossary != null : "Violation of: glossary is not null";
        assert termQueue != null : "Violation of: termQueue is not null";

        // holds the term
        String pendingTerm = "";
        // holds the definition
        String pendingDef = "";
        // tells us if we've already read the term line
        boolean haveTerm = false;

        // run until end of the file
        while (!input.atEOS()) {
            String line = input.nextLine();

            // non-empty lines are a term or a definition
            if (!line.isEmpty()) {
                if (!haveTerm) {
                    // first non-empty line of a new entry is the term
                    pendingTerm = line;
                    haveTerm = true;
                } else if (pendingDef.isEmpty()) {
                    // next non-empty line is the definition
                    pendingDef = line;
                } else {
                    // any more get included in the definition
                    pendingDef += " " + line;
                }
            } else {
                // an empty line, this entries finished
                if (haveTerm && !pendingDef.isEmpty()) {
                    // save the pair
                    glossary.add(pendingTerm, pendingDef);
                    termQueue.enqueue(pendingTerm);
                }
                // reset everything for next entry
                pendingTerm = "";
                pendingDef = "";
                haveTerm = false;
            }
        }

    }

    /**
     * Writes one HTML page per term in {@code glossary} to {@code directory}.
     *
     * @param directory
     *            output directory for the term pages
     * @param glossary
     *            map of every term and its definition
     * @requires directory exists
     * @ensures directory contains one html page per term in glossary
     */
    public static void generateTermPages(String directory, Map<String, String> glossary) {
        assert directory != null : "Violation of: directory is not null";
        assert glossary != null : "Violation of: glossary is not null";

        // loop through every pair in the glossary
        for (Map.Pair<String, String> entry : glossary) {
            // open a new file named after the term
            SimpleWriter pageOut = new SimpleWriter1L(
                    directory + "/" + entry.key() + ".html");
            // write the html page for this term
            buildTermPage(pageOut, entry.key(), entry.value(), glossary);
            // close the file
            pageOut.close();
        }
    }

    /**
     * Main method.
     *
     * @param args
     *            command-line arguments; not used
     */
    public static void main(String[] args) {
        // open streams
        SimpleWriter consoleOut = new SimpleWriter1L();
        SimpleReader consoleIn = new SimpleReader1L();

        // ask the user for the input
        consoleOut.print("Enter the name of the input file: ");
        String inputPath = consoleIn.nextLine();
        consoleOut.println();
        // ask the user where to save the html files
        consoleOut.print("Enter the name of the save location: ");
        String directory = consoleIn.nextLine();
        consoleOut.println();

        // open the input file for reading
        SimpleReader sourceFile = new SimpleReader1L(inputPath);

        // map to hold each term with definition
        Map<String, String> glossary = new Map1L<>();
        // queue to remember the order
        Queue<String> termQueue = new Queue1L<>();

        // read the input file into the map and queue
        readGlossary(sourceFile, glossary, termQueue);
        // write out an html page for each term
        generateTermPages(directory, glossary);

        // open the index.html file and write the main page
        SimpleWriter indexOut = new SimpleWriter1L(directory + "/index.html");
        buildIndexPage(indexOut, termQueue);

        // close all the streams
        indexOut.close();
        sourceFile.close();
        consoleOut.close();
        consoleIn.close();
    }
}
