package cscie97.asn4.ecommerce.exception;

/**
 * Exception for problems that the {@link cscie97.asn4.ecommerce.csv.CollectionImporter},
 * {@link cscie97.asn4.ecommerce.csv.ContentImporter}, or {@link cscie97.asn4.ecommerce.csv.SearchEngine} may run into
 * during typical operation.  This class will wrap lower-level exceptions (such as FileNotFoundException, IOException,
 * and generic Exception).
 *
 * @author David Killeffer &lt;rayden7@gmail.com&gt;
 * @version 1.0
 * @see cscie97.asn4.ecommerce.csv.CollectionImporter
 * @see cscie97.asn4.ecommerce.csv.ContentImporter
 * @see cscie97.asn4.ecommerce.csv.SearchEngine
 */
public class AccessDeniedException extends MobileAppException {

    public String username;

    /**
     * Wraps a more generic exception that may have been thrown in either the
     * {@link cscie97.asn4.ecommerce.csv.CollectionImporter}, {@link cscie97.asn4.ecommerce.csv.ContentImporter}, or
     * {@link cscie97.asn4.ecommerce.csv.SearchEngine} classes.  Arguments contain more specific details about the
     * exception to simplify debugging.
     *
     * @param line      the string value of the line that caused the exception
     * @param lineNum   the line number in the file that caused the exception
     * @param filename  the filename that was the cause of the original exception
     * @param cause     the wrapped lower-level exception that triggered this exception's creation
     */
    public AccessDeniedException (String username, String line, int lineNum, String filename, Throwable cause) {
        super( "AccessDeniedException: invalid username and/or password for user: ["+username+"]", line, lineNum, filename, cause);
        this.username = username;
    }

}