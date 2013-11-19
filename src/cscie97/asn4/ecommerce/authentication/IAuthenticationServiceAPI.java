package cscie97.asn4.ecommerce.authentication;

/**
 * Created with IntelliJ IDEA.
 * User: dkilleffer
 * Date: 11/13/13
 * Time: 10:58 AM
 * To change this template use File | Settings | File Templates.
 */
public interface IAuthenticationServiceAPI {

    public void addService(String tokenID, Service service);

    public void addUser(String tokenID, User user);

    public void addRole(String tokenID, Role role);

    //public void addPermission(String tokenID, Permission permission);

    public void addPermissionToService(String tokenID, String serviceID, Permission permission);

    public void addPermissionToRole(String tokenID, String roleID, String permissionID);

    public void addCredentialToUser(String tokenID, String userID, String username, String password);

    public void addEntitlementToUser(String tokenID, String userID, String entitlementID);

    public AccessToken login(String username, String password) throws AccessDeniedException;

    public void logout(String tokenID);

    public boolean mayAccess(String tokenID, String permissionID);

    public boolean mayAccess(String tokenID, PermissionType permissionType);

    public String getInventory();

}