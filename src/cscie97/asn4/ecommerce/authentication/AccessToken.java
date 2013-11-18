package cscie97.asn4.ecommerce.authentication;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

/**
 * Created with IntelliJ IDEA.
 * User: dkilleffer
 * Date: 11/13/13
 * Time: 10:56 AM
 * To change this template use File | Settings | File Templates.
 */
public class AccessToken {

    private String id;

    private String userID;

    private Date expirationTime;

    private Date lastUpdated;

    public AccessToken(String userID) {
        this.id = UUID.randomUUID().toString();
        this.userID = userID;

        Calendar cal = Calendar.getInstance(); // creates calendar
        cal.setTime(new Date()); // sets calendar time/date
        cal.add(Calendar.HOUR_OF_DAY, 1); // adds one hour

        this.expirationTime = cal.getTime();  // expire in 1 hour
        this.lastUpdated = new Date();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public Date getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(Date expirationTime) {
        this.expirationTime = expirationTime;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}
