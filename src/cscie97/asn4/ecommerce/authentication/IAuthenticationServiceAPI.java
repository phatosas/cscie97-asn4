package cscie97.asn4.ecommerce.authentication;

/**
 * Created with IntelliJ IDEA.
 * User: dkilleffer
 * Date: 11/13/13
 * Time: 10:58 AM
 * To change this template use File | Settings | File Templates.
 */
public interface IAuthenticationServiceAPI {

    public void createService(AccessToken token, String id, String name, String description);

    public void createPermission(AccessToken token, String id, String name, String description);

    public void createRole(AccessToken token, String id, String name, String description);

    public void createUser(AccessToken token, String id, String name);

    public AccessToken login(String username, String password);

    public void logout(AccessToken token);

    public boolean mayAccess(AccessToken token, String permissionID);

    public String getInventory();

    public User getUserByUsername(String username);

}
