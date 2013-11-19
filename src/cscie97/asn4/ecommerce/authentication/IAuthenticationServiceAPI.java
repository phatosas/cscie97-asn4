package cscie97.asn4.ecommerce.authentication;

/**
 * Public interface for the AuthenticaitonServiceAPI.  Administrators may use the methods here to create and
 * administer new Roles, Permissions, Services, and Users, and also define the properties on each.  Also contains
 * methods for logging in and logging out, which enable registered users to call restricted interface methods on the
 * {@link cscie97.asn4.ecommerce.authentication.IAuthenticationServiceAPI},
 * {@link cscie97.asn4.ecommerce.product.IProductAPI}, and
 * {@link cscie97.asn4.ecommerce.collection.ICollectionServiceAPI} classes.
 *
 * @author David Killeffer &lt;rayden7@gmail.com&gt;
 * @version 1.0
 * @see cscie97.asn4.ecommerce.collection.Collection
 * @see cscie97.asn4.ecommerce.collection.Collectible
 * @see cscie97.asn4.ecommerce.collection.DynamicCollection
 * @see cscie97.asn4.ecommerce.collection.StaticCollection
 * @see cscie97.asn4.ecommerce.collection.ContentProxy
 * @see cscie97.asn4.ecommerce.product.Content
 * @see cscie97.asn4.ecommerce.product.ContentSearch
 */
public interface IAuthenticationServiceAPI {

    /**
     * Adds a new Service definition to the catalog.  Services can contain child permissions, but primarily serve as a
     * marker class to logically group permissions into sensible sets.
     *
     * @param tokenID  id of the AccessToken to use for authentication to execute this method
     * @param service  the Service object to add to the Authentication catalog
     */
    public void addService(String tokenID, Service service);

    /**
     * Adds a new registered User to the catalog.
     *
     * @param tokenID  id of the AccessToken to use for authentication to execute this method
     * @param user     the User object to add to the Authentication catalog
     */
    public void addUser(String tokenID, User user);

    /**
     * Adds a new Role to the catalog.  Roles may contain other Roles or Permissions to define logical groupings that
     * correspond to types of Users that may use the AuthenticationServiceAPI.
     *
     * @param tokenID  id of the AccessToken to use for authentication to execute this method
     * @param role     the Role object to add to the Authentication catalog
     */
    public void addRole(String tokenID, Role role);

    /**
     * Adds a new Permission to the catalog as a child of the Service
     *
     * @param tokenID     id of the AccessToken to use for authentication to execute this method
     * @param serviceID   the pre-existing Service ID to add the Permission to
     * @param permission  the Permission to add as a child of the Service
     */
    public void addPermissionToService(String tokenID, String serviceID, Permission permission);

    /**
     * Adds a new Permission to the catalog as a child of the Role.
     *
     * @param tokenID       id of the AccessToken to use for authentication to execute this method
     * @param roleID        the pre-existing Role ID to add the Permission to
     * @param permissionID  the pre-existing Permission ID to add as a child of the Role
     */
    public void addPermissionToRole(String tokenID, String roleID, String permissionID);

    /**
     * Adds a new Credential to the User.
     *
     * @param tokenID   id of the AccessToken to use for authentication to execute this method
     * @param userID    the pre-existing User ID to add the Credential to
     * @param username  the username to use to create the Credential
     * @param password  the password to use to create the Credential
     */
    public void addCredentialToUser(String tokenID, String userID, String username, String password);

    /**
     * Adds a new Entitlement (can be a Role or Permission) to the User.
     *
     * @param tokenID        id of the AccessToken to use for authentication to execute this method
     * @param userID         the pre-existing User ID to add the Entitlement to
     * @param entitlementID  the pre-existing Entitlement ID (can be a Role or Permission) to add to the User
     */
    public void addEntitlementToUser(String tokenID, String userID, String entitlementID);

    /**
     * Logs a user into the AuthenticationService.  Modifies the AccessToken of the User to expire in 1 hour.
     *
     * @param username   the username to authenticate
     * @param password   the user's password to authenticate
     * @throws AccessDeniedException  thrown if the credentials passed are invalid
     * @return the AccessToken of the user
     */
    public AccessToken login(String username, String password) throws AccessDeniedException;

    /**
     * Logs the User out that owns the AccessToken with the supplied ID.  Modifies the AccessToken to set the
     * expiration time to be now.
     *
     * @param tokenID  the id of the AccessToken for the owning user
     */
    public void logout(String tokenID);

    /**
     * Checks if the User that owns the AccessToken corresponding to the passed tokenID has permission to use
     * the Permission.  Looks up the AccessToken's owning User, and inspects their Entitlements to confirm whether
     * or not the user has the corresponding Permission.
     *
     * @param tokenID       the id of the AccessToken to check
     * @param permissionID  the id of the Permission to check the user for
     * @return true if the user has the Permission, false otherwise
     */
    public boolean mayAccess(String tokenID, String permissionID);

    /**
     * Checks if the User that owns the AccessToken corresponding to the passed tokenID has permission to use
     * the Permission.  Looks up the AccessToken's owning User, and inspects their Entitlements to confirm whether
     * or not the user has the corresponding Permission.
     *
     * @param tokenID         the id of the AccessToken to check
     * @param permissionType  the PermissionType to check the user for
     * @return true if the user has the Permission, false otherwise
     */
    public boolean mayAccess(String tokenID, PermissionType permissionType);

    /**
     * Returns a string representation of the entire Authentication catalog, including Services,
     * Users, Roles, and Permissions.  Uses the Visitor pattern to visit each Service, User, Role, and Permission to
     * inquire about the object's salient properties.
     *
     * @return  string representation of the entire Authentication catalog
     */
    public String getInventory();

}