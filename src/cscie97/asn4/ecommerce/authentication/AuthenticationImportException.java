package cscie97.asn4.ecommerce.authentication;

import cscie97.asn4.ecommerce.exception.MobileAppException;

/**
 * Exception for problems that the {@link cscie97.asn4.ecommerce.authentication.AuthenticationImporter} may run into
 * during typical operation.  This class will wrap lower-level exceptions (such as FileNotFoundException, IOException,
 * and generic Exception).
 *
 * @author David Killeffer &lt;rayden7@gmail.com&gt;
 * @version 1.0
 * @see cscie97.asn4.ecommerce.authentication.AuthenticationImporter
 * @see cscie97.asn4.ecommerce.authentication.IAuthenticationServiceAPI
 */
public class AuthenticationImportException extends MobileAppException {

    /**
     * Wraps a more generic exception that may have been thrown in the
     * {@link cscie97.asn4.ecommerce.authentication.AuthenticationImporter} class.  Arguments contain more specific
     * details about the exception to simplify debugging.
     *
     * @param line      the string value of the line that caused the exception
     * @param lineNum   the line number in the file that caused the exception
     * @param filename  the filename that was the cause of the original exception
     * @param cause     the wrapped lower-level exception that triggered this exception's creation
     */
    public AuthenticationImportException (String line, int lineNum, String filename, Throwable cause) {
        super( "AuthenticationImportException", line, lineNum, filename, cause);
    }

}