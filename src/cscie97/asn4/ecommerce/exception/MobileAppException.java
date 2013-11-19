package cscie97.asn4.ecommerce.exception;

/**
 * Exception for problems that might occur in any of the {@link cscie97.asn4.ecommerce.collection},
 * {@link cscie97.asn4.ecommerce.product}, or {@link cscie97.asn4.ecommerce.csv} classes. This class will wrap
 * lower-level exceptions (such as FileNotFoundException, IOException, ParseException, and generic Exception).
 *
 * @author David Killeffer &lt;rayden7@gmail.com&gt;
 * @version 1.0
 * @see cscie97.asn4.ecommerce.product.ContentImporter
 * @see cscie97.asn4.ecommerce.collection.CollectionImporter
 * @see cscie97.asn4.ecommerce.product.SearchEngine
 */
public class MobileAppException extends Exception {

    /**
     * The string line where the original exception originated in the import file
     */
    private String lineWhereFailed;

    /**
     * The line number of the import file that triggered the original exception
     */
    private int lineIndexWhereFailed;

    /**
     * Name of the imported file that triggered the original exception
     */
    private String filename;

    /**
     * The original exception that this class wraps with more specific information
     */
    private Throwable originalCause;

    /**
     * Wraps a more generic exception that may have been thrown in one of the CSV importer classes or elsewhere
     * in the Mobile Application Store API.  Arguments contain more specific details about the exception to simplify
     * debugging.
     *
     * @param exceptionType  the string value of the custom exception type that caused the exception
     * @param line           the string value of the line that caused the exception
     * @param lineNum        the line number in the file that caused the exception
     * @param filename       the filename that was the cause of the original exception
     * @param cause          the wrapped lower-level exception that triggered this exception's creation
     */
    //public MobileAppException (String exceptionType, String line, int lineNum, String filename, Throwable cause) {
    public MobileAppException (String exceptionType, String line, int lineNum, String filename, Throwable cause) {
        super(exceptionType + " occurred at line #" + lineNum + " of file " + filename + " in line number [" + lineNum + "]", cause);
        this.lineWhereFailed = line;
        this.lineIndexWhereFailed = lineNum;
        this.filename = filename;
        this.originalCause = cause;
    }

    /**
     * Returns the string value of the entire line in the original file that caused the wrapped exception
     *
     * @return  the string value of the line where the original exception occurred
     */
    public String getLineWhereFailed() {
        return lineWhereFailed;
    }

    /**
     * Sets the string value of the entire line in the original file that caused the wrapped exception
     *
     * @param lineWhereFailed  the string value of the line that caused the exception
     */
    public void setLineWhereFailed(String lineWhereFailed) {
        this.lineWhereFailed = lineWhereFailed;
    }

    /**
     * Returns the line number where the original exception occurred
     *
     * @return  the line number where the original exception occurred
     */
    public int getLineIndexWhereFailed() {
        return lineIndexWhereFailed;
    }

    /**
     * Sets the line number where the original exception occurred
     *
     * @param lineIndexWhereFailed  the line number where the original exception occurred
     */
    public void setLineIndexWhereFailed(int lineIndexWhereFailed) {
        this.lineIndexWhereFailed = lineIndexWhereFailed;
    }

    /**
     * Returns the original filename that triggered the exception
     *
     * @return  the original filename that triggered the exception
     */
    public String getFilename() {
        return filename;
    }

    /**
     * Sets the original filename that triggered the exception
     *
     * @param  filename   the original filename that triggered the exception
     */
    public void setFilename(String filename) {
        this.filename = filename;
    }

    /**
     * Returns the original exception that the ImportException is wrapping with more specific details
     *
     * @return  the original wrapped exception
     */
    public Throwable getOriginalCause() {
        return originalCause;
    }

    /**
     * Sets the original exception that the ImportException is wrapping with more specific details
     *
     * @param originalCause   the original wrapped exception
     */
    public void setOriginalCause(Throwable originalCause) {
        this.originalCause = originalCause;
    }

}