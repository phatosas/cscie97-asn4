package cscie97.asn4.ecommerce.authentication;

import cscie97.asn4.ecommerce.exception.MobileAppException;

/**
 * Exception for problems that the {@link cscie97.asn4.ecommerce.authentication.IAuthenticationServiceAPI} encounters
 * when users attempt to carry out a restricted interface method without the appropriate permissions.
 *
 * @author David Killeffer &lt;rayden7@gmail.com&gt;
 * @version 1.0
 * @see cscie97.asn4.ecommerce.authentication.IAuthenticationServiceAPI
 */
public class AccessDeniedException extends MobileAppException {

    public String username;

    /**
     * Wraps a more generic exception that may have been thrown in either the
     * {@link cscie97.asn4.ecommerce.authentication.AuthenticationImporter} or
     * {@link cscie97.asn4.ecommerce.authentication.IAuthenticationServiceAPI} classes.  Arguments contain more
     * specific details about the exception to simplify debugging.
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