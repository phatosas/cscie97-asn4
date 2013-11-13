package cscie97.asn4.ecommerce.authentication;

import cscie97.asn4.ecommerce.collection.Collection;
import cscie97.asn4.ecommerce.collection.ICollectionServiceAPI;

import java.util.HashSet;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: dkilleffer
 * Date: 11/13/13
 * Time: 11:02 AM
 * To change this template use File | Settings | File Templates.
 */
public class AuthenticationServiceAPI implements IAuthenticationServiceAPI {

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
