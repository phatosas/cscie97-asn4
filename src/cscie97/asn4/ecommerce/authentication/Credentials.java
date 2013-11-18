package cscie97.asn4.ecommerce.authentication;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

/**
 * Created with IntelliJ IDEA.
 * User: dkilleffer
 * Date: 11/13/13
 * Time: 10:56 AM
 * To change this template use File | Settings | File Templates.
 */
public class Credentials {

    private String username;

    private String passwordHash;

    private String password;

    private String passwordSalt;

    public Credentials(String username, String password) {
        this.username = username;
        this.password = password;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordSalt() {
        return passwordSalt;
    }

    public void setPasswordSalt(String passwordSalt) {
        this.passwordSalt = passwordSalt;
    }

}