package cscie97.asn4.ecommerce.authentication;

import cscie97.asn4.ecommerce.exception.MobileAppException;

/**
 * Exception for problems that the {@link cscie97.asn4.ecommerce.authentication.IAuthenticationServiceAPI}, may run into
 * during typical operation.
 *
 * @author David Killeffer &lt;rayden7@gmail.com&gt;
 * @version 1.0
 * @see cscie97.asn4.ecommerce.authentication.IAuthenticationServiceAPI
 */
public class InvalidAccessTokenException extends MobileAppException {

    /**
     * Thrown if an invalid AccessToken is attempted to be used to authenticate with.
     *
     * @param line      the string value of the line that caused the exception
     * @param lineNum   the line number in the file that caused the exception
     * @param filename  the filename that was the cause of the original exception
     * @param cause     the wrapped lower-level exception that triggered this exception's creation
     */
    public InvalidAccessTokenException (String line, int lineNum, String filename, Throwable cause) {
        super( "InvalidAccessTokenException", line, lineNum, filename, cause);
    }

}