package cscie97.asn4.ecommerce.authentication;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

/**
 * AccessTokens are used for authorization purposes.  Each {@link cscie97.asn4.ecommerce.authentication.User} account
 * will have an AccessToken created for them when they login to the site.  Any future calls to restricted interface
 * methods on either the {@link cscie97.asn4.ecommerce.product.IProductAPI} or
 * {@link cscie97.asn4.ecommerce.collection.ICollectionServiceAPI} must pass the
 * {@link cscie97.asn4.ecommerce.authentication.AccessToken#id} of the user's AccessToken in order to proceed.
 *
 * @author David Killeffer &lt;rayden7@gmail.com&gt;
 * @version 1.0
 * @see cscie97.asn4.ecommerce.authentication.User
 */
public class AccessToken {

    /**
     * Unique identifier for the AccessToken; form is a GUID
     */
    private String id;

    /**
     * ID of the user object that owns this AccessToken
     */
    private String userID;

    /**
     * When the AccessToken expires and is no longer valid for authentication purposes
     */
    private Date expirationTime;

    /**
     * Each time a restricted interface method is called and the ID of this AccessToken is passed, this value is
     * updated to the current time and the expirationTime is also updated to be 1 hour into the future
     */
    private Date lastUpdated;

    /**
     * Class constructor.  Takes a userID to associate the token with a user account.
     *
     * @param userID  id of the user account to associate the token with
     */
    public AccessToken(String userID) {
        this.id = UUID.randomUUID().toString();
        this.userID = userID;

        // default the expiration of the token to be 1 hour in the future
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.HOUR_OF_DAY, 1);
        this.expirationTime = cal.getTime();  // expire in 1 hour

        this.lastUpdated = new Date();
    }

    /**
     * Returns the unique identification value for the token
     *
     * @return  unique ID (guid) of the token
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the unique identification value of the token
     *
     * @param id  unique GUID value for the token
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Returns the userID that is associated with the token
     *
     * @return  userID that is associated with the token
     */
    public String getUserID() {
        return userID;
    }

    /**
     * Sets the userID of the account that is associated with the token
     *
     * @param userID  the ID of the user that is associated with the token
     */
    public void setUserID(String userID) {
        this.userID = userID;
    }

    /**
     * When the token is no longer valid for authorization purposes
     *
     * @return  the time after which the token is no longer valid
     */
    public Date getExpirationTime() {
        return expirationTime;
    }

    /**
     * Sets when the token is no longer valid for authorization purposes
     *
     * @param expirationTime  the time after which the token is no longer valid
     */
    public void setExpirationTime(Date expirationTime) {
        this.expirationTime = expirationTime;
    }

    /**
     * Returns the last time that the token was accessed and updated (e.g., the last time the token was used
     * for authorization purposes)
     *
     * @return  the time the token was last updated
     */
    public Date getLastUpdated() {
        return lastUpdated;
    }

    /**
     * Sets the last time that the token was accessed and updated (e.g., the last time the token was used
     * for authorization purposes)
     *
     * @param lastUpdated  the time the token was last updated
     */
    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}