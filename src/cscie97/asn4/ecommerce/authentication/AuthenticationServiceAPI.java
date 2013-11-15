package cscie97.asn4.ecommerce.authentication;

import cscie97.asn4.ecommerce.collection.Collection;
import cscie97.asn4.ecommerce.collection.ICollectionServiceAPI;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: dkilleffer
 * Date: 11/13/13
 * Time: 11:02 AM
 * To change this template use File | Settings | File Templates.
 */
public class AuthenticationServiceAPI implements IAuthenticationServiceAPI {


    private static final String SUPER_ADMINISTRATOR_USERNAME = "dkilleffer";

    private static final String SUPER_ADMINISTRATOR_PASSWORD = "secret";

    private User superUser;



    /**
     * The unique top-level Entitlements contained in the Authentication catalog; each Entitlement may only be
     * declared at the top-level once, but may be nested arbitrarily deeply in other Entitlements.
     */
    private Set<Entitlement> entitlements = new HashSet<Entitlement>();

    /**
     * The unique Services contained in the Authentication catalog.
     */
    private Set<Service> services = new HashSet<Service>();


    /**
     * The unique registered Users contained in the Authentication catalog.
     */
    private Set<User> users = new HashSet<User>();



    /**
     * Singleton instance of the AuthenticationServiceAPI
     */
    private static IAuthenticationServiceAPI instance = null;

    /**
     * Class constructor.  Declares the top-level entitlements to initially be an empty HashSet.
     */
    private AuthenticationServiceAPI() {
        this.entitlements = new HashSet<Entitlement>() { };

        //private final String SUPER_ADMINISTRATOR_USERNAME = "dkilleffer";
        //private final String SUPER_ADMINISTRATOR_PASSWORD = "secret";
        //private User administrativeSuperUser;

        String superUserGUID = UUID.randomUUID().toString();

        Credentials credential = new Credentials(SUPER_ADMINISTRATOR_USERNAME, SUPER_ADMINISTRATOR_PASSWORD);

        this.superUser = new User(superUserGUID, SUPER_ADMINISTRATOR_USERNAME, "Administrative Super User");

        superUser.addCredential(credential);

        // http://stackoverflow.com/questions/3581258/adding-n-hours-to-a-date-in-java
        Calendar cal = Calendar.getInstance(); // creates calendar
        cal.setTime(new Date()); // sets calendar time/date
        cal.add(Calendar.HOUR_OF_DAY, 1); // adds one hour
        //cal.getTime(); // returns new date object, one hour in the future

        AccessToken token = new AccessToken();
        token.setId( UUID.randomUUID().toString() );
        token.setExpirationTime( cal.getTime() );  // expire in 1 hour
        token.setLastUpdated( new Date() );
        token.setUserID( superUser.getID() );

        superUser.addAccessToken(token);


        //boolean isPasswordValid = superUser.validatePassword(SUPER_ADMINISTRATOR_PASSWORD);
        boolean badPasswordCheck = superUser.validatePassword("fake");
        boolean goodPasswordCheck = superUser.validatePassword("secret");
        boolean bestPasswordCheck = superUser.validatePassword(SUPER_ADMINISTRATOR_PASSWORD);

    }

    /**
     * Returns a reference to the single static instance of the AuthenticationServiceAPI.
     *
     * @return  singleton instance of AuthenticationServiceAPI
     */
    public static synchronized IAuthenticationServiceAPI getInstance() {
        if (instance == null) {
            instance = new AuthenticationServiceAPI();
        }
        return instance;
    }






    //public UUID thingOne = new UUID();
    public UUID thingTwo = UUID.randomUUID();



    @Override
    public void createService(AccessToken token, String id, String name, String description) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void createPermission(AccessToken token, String id, String name, String description) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void createRole(AccessToken token, String id, String name, String description) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void createUser(AccessToken token, String id, String name) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public AccessToken login(String username, String password) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void logout(AccessToken token) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean mayAccess(AccessToken token, String permissionID) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getInventory() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public User getUserByUsername(String username) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
