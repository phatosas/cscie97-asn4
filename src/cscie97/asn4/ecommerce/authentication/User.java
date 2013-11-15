package cscie97.asn4.ecommerce.authentication;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: dkilleffer
 * Date: 11/13/13
 * Time: 10:57 AM
 * To change this template use File | Settings | File Templates.
 */
public class User extends Item {

    private Set<Credentials> credentials = new HashSet<Credentials>();

    private Set<Entitlement> entitlements = new HashSet<Entitlement>();

    private Set<AccessToken> accessTokens = new HashSet<AccessToken>();


    /**
     * Class constructor.
     *
     * @param id                  the unique authentication item ID
     * @param name                the authentication item name
     * @param description         authentication item description
     */
    public User(String id, String name, String description)
    {
        //super(id,name,description);
        this.setID(id);
        this.setName(name);
        this.setDescription(description);
    }

    public User() { }







    public Set<Credentials> getCredentials() {
        return credentials;
    }

    public void setCredentials(Set<Credentials> credentials) {
        this.credentials = credentials;
    }

    public Set<Entitlement> getEntitlements() {
        return entitlements;
    }

    public void setEntitlements(Set<Entitlement> entitlements) {
        this.entitlements = entitlements;
    }

    public Set<AccessToken> getAccessTokens() {
        return accessTokens;
    }

    public void setAccessTokens(Set<AccessToken> accessTokens) {
        this.accessTokens = accessTokens;
    }








    public void addCredential(Credentials credential) {
        this.credentials.add(credential);
    }

    public void addCredential(String username, String password) {
        Credentials credential = new Credentials(username, password);
        addCredential(credential);
    }


    public void addEntitlement(Entitlement entitlement) {
        this.entitlements.add(entitlement);
    }

    public void addAccessToken(AccessToken token) {
        this.accessTokens.add(token);
    }


    public boolean hasPermission(String permissionID) {

        // TODO: iterate over every item in "entitlements", return true as soon as encountered one with same ID

        return false;
    }

    public boolean validatePassword(String password) {
        for (Credentials c : credentials) {
            try {
                //String actualHash = PasswordHash.createHash( c.getPasswordSalt() + password);
                //String actualHash = PasswordHash.createHash( c.getPasswordSalt() + ":"+ password);
                //String actualHash = PasswordHash.createHash(password);
                //String expectedHash = c.getPasswordHash();

                //System.out.println("CHECKING PASSWORD FOR USER ID ["+this.getID()+"] AND USERNAME: ["+c.getUsername()+"] - password: ["+password+"]:\n" +
                //        "expected hash: ["+expectedHash+"]\n" +
                //        "actual hash: ["+actualHash+"]\n");

                if (PasswordHash.validatePassword(password, c.getPasswordHash()) )
                    return true;
            }
            catch (NoSuchAlgorithmException nsae) { }
            catch (InvalidKeySpecException ikse) { }
        }
        return false;
    }

}
