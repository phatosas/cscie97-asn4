package cscie97.asn4.ecommerce.collection;

import cscie97.asn4.ecommerce.exception.MobileAppException;

/**
 * Exception for problems that the {@link CollectionImporter} may run into during typical
 * operation.  This class will wrap lower-level exceptions (such as FileNotFoundException, IOException, and generic
 * Exception).
 *
 * @author David Killeffer &lt;rayden7@gmail.com&gt;
 * @version 1.0
 * @see CollectionImporter
 * @see cscie97.asn4.ecommerce.product.SearchEngine
 */
public class CollectionNotFoundException extends MobileAppException {

    /**
     * Wraps a more generic exception that may have been thrown in the
     * {@link CollectionImporter} class.  Arguments contain more specific details about the
     * exception to simplify debugging.
     *
     * @param line      the string value of the line that caused the exception
     * @param lineNum   the line number in the file that caused the exception
     * @param filename  the filename that was the cause of the original exception
     * @param cause     the wrapped lower-level exception that triggered this exception's creation
     */
    public CollectionNotFoundException (String line, int lineNum, String filename, Throwable cause) {
        super( "CollectionNotFoundException", line, lineNum, filename, cause);
    }

}