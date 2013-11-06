package cscie97.asn4.ecommerce.exception;

/**
 * Exception for problems that the {@link cscie97.asn4.ecommerce.csv.Importer} or
 * {@link cscie97.asn4.ecommerce.csv.SearchEngine} may run into during typical operation such as importing
 * Countries, Devices, Content, or executing queries.
 *
 * @author David Killeffer &lt;rayden7@gmail.com&gt;
 * @version 1.0
 * @see cscie97.asn4.ecommerce.csv.Importer
 * @see cscie97.asn4.ecommerce.csv.SearchEngine
 */
public class ParseException extends Exception {

    /**
     * The string line where the original exception originated in the import file
     */
    private String lineWhereFailed;

    /**
     * The line number of the import file that triggered the original exception
     */
    private int lineIndexWhereFailed = 0;

    /**
     * Name of the imported file that triggered the original exception
     */
    private String filename;

    /**
     * The original exception that this class wraps with more specific information
     */
    private Throwable originalCause = null;


    /**
     * Wraps a more generic exception that may have been thrown in the {@link cscie97.asn4.ecommerce.csv.Importer}
     * or {@link cscie97.asn4.ecommerce.csv.SearchEngine} classes.  Arguments contain more specific details about
     * the exception to simplify debugging.
     *
     * @param line      the string value of the line that caused the exception
     * @param cause     the wrapped lower-level exception that triggered this exception's creation; may be null
     */
    public ParseException (String msg, String line, Throwable cause) {
        super("ParseException occurred on line [" + line + "]", cause);
        this.lineWhereFailed = line;
        this.originalCause = cause;
    }

    /**
     * Wraps a more generic exception that may have been thrown in the {@link cscie97.asn4.ecommerce.csv.Importer}
     * or {@link cscie97.asn4.ecommerce.csv.SearchEngine} classes.  Arguments contain more specific details about
     * the exception to simplify debugging.
     *
     * @param msg       the exception message from the throwing cause
     * @param line      the string value of the line that caused the exception
     * @param cause     the wrapped lower-level exception that triggered this exception's creation; may be null
     */
    public ParseException (String msg, String line, int lineNum, String filename, Throwable cause) {
        super("ParseException occurred on line [" + line + "] of file [" + filename + "] at line number [" + lineNum + "]", cause);
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