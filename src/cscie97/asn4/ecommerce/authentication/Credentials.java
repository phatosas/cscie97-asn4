package cscie97.asn4.ecommerce.authentication;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

/**
 * {@link cscie97.asn4.ecommerce.authentication.User} objects may have multiple sets of credentials; a Credential
 * essentially consists of the username and password combination that a User will use to login and logout of the
 * {@link cscie97.asn4.ecommerce.authentication.IAuthenticationServiceAPI}, which will create an
 * {@link cscie97.asn4.ecommerce.authentication.AccessToken} that they may use to carry out restricted interface
 * methods.  Users may then have multiple sets of usernames/passwords that they can use to log into the system.
 *
 * Passwords are hashed using the helper class {@link cscie97.asn4.ecommerce.authentication.PasswordHash}.  At no time
 * are the actual plain-text passwords stored on the object; only the password hash is saved on the object.
 *
 * @author David Killeffer &lt;rayden7@gmail.com&gt;
 * @version 1.0
 * @see cscie97.asn4.ecommerce.authentication.User
 * @see cscie97.asn4.ecommerce.authentication.AccessToken
 * @see cscie97.asn4.ecommerce.authentication.PasswordHash
 */
public class Credentials {

    /**
     * The username that is used when logging into the
     * {@link cscie97.asn4.ecommerce.authentication.IAuthenticationServiceAPI}
     */
    private String username;

    /**
     * The hashed password; used for authentication
     */
    private String passwordHash;

    /**
     * The unique salt that is applied when hashing a password; must be saved so that subsequent attempts to verify
     * the password will generate the same hash
     */
    private String passwordSalt;

    /**
     * Class constructor.  Takes a username and password, hashes the password, and saves the hashed version and
     * the salt used to generate the hash.
     *
     * @param username  the username that is to be used
     * @param password  the plain-text password; will be hashed and saved (the plain-text version is never saved)
     */
    public Credentials(String username, String password) {
        this.username = username;
        try {
            String hashedAndSaltedPassword = PasswordHash.createHash(password);
            this.passwordHash = hashedAndSaltedPassword;
            String[] parts = hashedAndSaltedPassword.split(":");
            if (parts != null && parts.length == 3) {
                // first part is number of pbkdf2 iterations used to compute the hash
                this.passwordSalt = parts[1];  // second part is the salt
            }
        }
        catch (NoSuchAlgorithmException nsae) { }
        catch (InvalidKeySpecException ikse) { }
    }

    /**
     * Returns the username
     *
     * @return  the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username
     *
     * @param username  the username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * The hashed version of the password
     *
     * @return  the hashed version of the password
     */
    public String getPasswordHash() {
        return passwordHash;
    }

    /**
     * Sets the hashed version of the password; note that this does not perform the actual hashing, that is done
     * in the class constructor.
     *
     * @param passwordHash  the hashed version of the password
     */
    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    /**
     * The salt used when hashing the password; required for later verifying the user password so that a new hashed
     * password can be generated and compared to the current hashed password
     *
     * @return  the salt used to create the original hashed password
     */
    public String getPasswordSalt() {
        return passwordSalt;
    }

    /**
     * Sets the salt value used to hash the password.
     *
     * @param passwordSalt  sets the salt value to hash the password
     */
    public void setPasswordSalt(String passwordSalt) {
        this.passwordSalt = passwordSalt;
    }

}