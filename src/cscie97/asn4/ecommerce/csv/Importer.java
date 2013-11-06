package cscie97.asn4.ecommerce.csv;

/**
 * Abstract parent class of all other CSV loading and query implementing child classes.  Provides utility methods
 * that are useful to all implementing classes and centralizes shared methods (such as CSV line parsing).
 *
 * @author David Killeffer &lt;rayden7@gmail.com&gt;
 * @version 1.0
 * @see cscie97.asn4.ecommerce.csv.CollectionImporter
 * @see cscie97.asn4.ecommerce.csv.ContentImporter
 * @see cscie97.asn4.ecommerce.csv.Importer
 * @see cscie97.asn4.ecommerce.csv.SearchEngine
 */
public abstract class Importer {

    /**
     * Splits up the line string based on the supplied separator into an array of strings and ignores any
     * backslash-escaped separator.
     *
     * @param line       the string to parse out and split into an array based on separator
     * @param separator  the character to use for splitting out the line
     * @return           an array of strings that were split by the separator
     */
    public static String[] parseCSVLine(String line, String separator) {
        // need to do a negative lookbehind to properly escape the backslash-preceeding characters that come
        // immediately prior to the passed separator in input strings (help from:
        // http://stackoverflow.com/questions/820172/how-to-split-a-comma-separated-string-while-ignoring-escaped-commas)
        String[] parts = line.split("(?<!\\\\)"+separator);

        // remove any remaining backslash characters from each of the parts if that backslash is immediately
        // followed by a comma (which is the way our CSVs are formatted to escape inline commas per column
        for(int i=0; i<parts.length; i++) {
            //parts[i] = parts[i].replaceAll("\\\\,+", ",");
            parts[i] = parts[i].replaceAll("\\\\,+", ",").trim();
        }
        return parts;
    }

}