package cscie97.asn4.ecommerce.authentication;

import java.util.Set;
import java.util.HashSet;
import java.util.UUID;
import java.util.Arrays;
import java.util.Date;

/**
 * Concrete implementation class of the AuthenticaitonServiceAPI.  Administrators may use the methods here to create and
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
     * Class constructor.  Declares the top-level entitlements to initially be an empty HashSet.  Also creates the
     * initial "super user" that is used to load the Authentication catalog.
     */
    private AuthenticationServiceAPI() {
        this.entitlements = new HashSet<Entitlement>() { };

        // create an initial "Super User" that may be used to initially load the authentication.csv file
        createSuperUser();
    }

    /**
     * Bootstraps the AuthenticationServiceAPI with a "super user" that can be used to initially load the
     * authentication catalog via the {@link cscie97.asn4.ecommerce.authentication.AuthenticationImporter}. This user
     * will have all the Permissions of the AuthenticationServiceAPI, and so will be able to call all the methods
     * defined in {@link cscie97.asn4.ecommerce.authentication.IAuthenticationServiceAPI}.
     */
    private void createSuperUser() {
        if (this.superUser == null) {
            // create a hard-coded special "Super User" so that we can use that super user to import the Authentication data
            this.superUser = new User(UUID.randomUUID().toString(), SUPER_ADMINISTRATOR_USERNAME, "Administrative Super User");
            superUser.addCredential( new Credentials(SUPER_ADMINISTRATOR_USERNAME, SUPER_ADMINISTRATOR_PASSWORD) );
            superUser.setAccessToken( new AccessToken(superUser.getID()) );

            // define primary Authentication Service
            String authServiceID = "authentication_service";
            String authServiceName = "Authentication Service";
            String authServiceDescription = "Manage Authentication Configuration and Control Access to Restricted Service Interfaces";
            Service authenticationService = new Service(authServiceID, authServiceName, authServiceDescription);

            Permission p1  = new Permission(PermissionType.DEFINE_SERVICE,"Define Service Permission","Permission to create a new service");
            Permission p2  = new Permission(PermissionType.DEFINE_PERMISSION,"Define Permission Permission","Permission to create a new permission");
            Permission p3  = new Permission(PermissionType.DEFINE_ROLE,"Define Role Permission","Permission to create a new role");
            Permission p4  = new Permission(PermissionType.CREATE_USER,"Create User Permission","Permission to create create a user");
            Permission p5  = new Permission(PermissionType.ADD_ENTITLEMENT_TO_ROLE,"Add entitlement to role permission","Permission to add an entitlement to a role");
            Permission p6  = new Permission(PermissionType.ADD_CREDENTIAL_TO_USER,"Add Credential to User Permission","Permission to add credentials to a user");
            Permission p7  = new Permission(PermissionType.ADD_ENTITLEMENT_TO_USER,"Add Entitlement to User Permission","Permission to add entitlements to a user");
            Permission p8  = new Permission(PermissionType.CREATE_COLLECTION,"Create Collection Permission","Permission to create a new collection");
            Permission p9  = new Permission(PermissionType.ADD_CONTENT,"Add Collection Content Permission","Permission to add content to an existing collection");
            Permission p10 = new Permission(PermissionType.DEFINE_COLLECTION_SEARCH_CRITERIA,"Define Dynamic Collection Search Criteria Permission","Define the search criteria used by DynamicCollections");
            Permission p11 = new Permission(PermissionType.CREATE_PRODUCT,"Create Product Permission","Permission to create a new product");
            Permission p12 = new Permission(PermissionType.CREATE_COUNTRY,"Create Country Permission","Permission to create a new country");
            Permission p13 = new Permission(PermissionType.CREATE_DEVICE,"Create Device Permission","Permission to create a new device");

            // define all Authentication Service Permissions
            Set<Entitlement> allAuthPermissionsSet = new HashSet<Entitlement>(Arrays.asList(p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12, p13));
            Set<Permission> allAuthPermissionsList = new HashSet<Permission>(Arrays.asList(p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12, p13));

            // define primary Authentication SUPER USER Role, with all appropriate Permissions
            String authServiceSuperRoleID = "authentication_admin_role";
            String authServiceSuperRoleName = "Authentication Admin";
            String authServiceSuperRoleDescription = "All permissions required by Authentication Administrator";
            Role authenticationRole = new Role(authServiceSuperRoleID, authServiceSuperRoleName, authServiceSuperRoleDescription);
            authenticationRole.addChildren( allAuthPermissionsSet );

            authenticationService.addPermissions( allAuthPermissionsList );

            // because the services are defined as a Set, we can add this without fear of it being a duplicate
            this.services.add(authenticationService);

            this.entitlements.addAll( allAuthPermissionsSet );
            this.entitlements.add( authenticationRole );

            superUser.addEntitlement(authenticationRole);

            this.users.add(superUser);
        }
    }

    /**
     * Returns a reference to the single static instance of the AuthenticationServiceAPI.  Will also create a
     * "super user" account that can be used for loading all the Authentication catalog items.
     *
     * @return  singleton instance of AuthenticationServiceAPI
     */
    public static synchronized IAuthenticationServiceAPI getInstance() {
        if (instance == null) {
            instance = new AuthenticationServiceAPI();
        }
        return instance;
    }

    /**
     * Adds a new Service definition to the catalog.  Services can contain child permissions, but primarily serve as a
     * marker class to logically group permissions into sensible sets.
     *
     * @param tokenID  id of the AccessToken to use for authentication to execute this method
     * @param service  the Service object to add to the Authentication catalog
     */
    @Override
    public void addService(String tokenID, Service service) {
        if (this.mayAccess(tokenID, "define_service")) {
            this.services.add(service);
        }
    }

    /**
     * Adds a new registered User to the catalog.
     *
     * @param tokenID  id of the AccessToken to use for authentication to execute this method
     * @param user     the User object to add to the Authentication catalog
     */
    @Override
    public void addUser(String tokenID, User user) {
        if (this.mayAccess(tokenID, PermissionType.CREATE_USER)) {
            this.users.add(user);
        }
    }

    /**
     * Adds a new Role to the catalog.  Roles may contain other Roles or Permissions to define logical groupings that
     * correspond to types of Users that may use the AuthenticationServiceAPI.
     *
     * @param tokenID  id of the AccessToken to use for authentication to execute this method
     * @param role     the Role object to add to the Authentication catalog
     */
    @Override
    public void addRole(String tokenID, Role role) {
        if (mayAccess(tokenID, PermissionType.DEFINE_ROLE)) {
            this.entitlements.add(role);
        }
    }

    /**
     * Adds a new Permission to the catalog as a child of the Service
     *
     * @param tokenID     id of the AccessToken to use for authentication to execute this method
     * @param serviceID   the pre-existing Service ID to add the Permission to
     * @param permission  the Permission to add as a child of the Service
     */
    @Override
    public void addPermissionToService(String tokenID, String serviceID, Permission permission) {
        if (mayAccess(tokenID, PermissionType.DEFINE_PERMISSION)) {
            Service service = this.getServiceById(serviceID);
            if (service != null) {
                service.addPermission(permission);
                this.entitlements.add(permission);
            }
        }
    }

    /**
     * Adds a new Permission to the catalog as a child of the Role.
     *
     * @param tokenID       id of the AccessToken to use for authentication to execute this method
     * @param roleID        the pre-existing Role ID to add the Permission to
     * @param permissionID  the pre-existing Permission ID to add as a child of the Role
     */
    @Override
    public void addPermissionToRole(String tokenID, String roleID, String permissionID) {
        if (mayAccess(tokenID, PermissionType.ADD_ENTITLEMENT_TO_ROLE)) {
            Entitlement foundRole = this.getEntitlementById(roleID);
            Entitlement foundPermission = this.getEntitlementById(permissionID);
            if (foundRole != null &&
                foundRole instanceof Role &&
                foundPermission != null &&
                foundPermission instanceof Permission
            ) {
                ((Role) foundRole).addChild(foundPermission);
            }
        }
    }

    /**
     * Adds a new Credential to the User.
     *
     * @param tokenID   id of the AccessToken to use for authentication to execute this method
     * @param userID    the pre-existing User ID to add the Credential to
     * @param username  the username to use to create the Credential
     * @param password  the password to use to create the Credential
     */
    @Override
    public void addCredentialToUser(String tokenID, String userID, String username, String password) {
        if (mayAccess(tokenID, PermissionType.ADD_CREDENTIAL_TO_USER)) {
            User foundUser = this.getUserByUserID(userID);
            if (foundUser != null) {
                foundUser.addCredential( new Credentials(username, password) );
            }
        }
    }

    /**
     * Adds a new Entitlement (can be a Role or Permission) to the User.
     *
     * @param tokenID        id of the AccessToken to use for authentication to execute this method
     * @param userID         the pre-existing User ID to add the Entitlement to
     * @param entitlementID  the pre-existing Entitlement ID (can be a Role or Permission) to add to the User
     */
    @Override
    public void addEntitlementToUser(String tokenID, String userID, String entitlementID) {
        if (mayAccess(tokenID, PermissionType.ADD_ENTITLEMENT_TO_USER)) {
            User foundUser = this.getUserByUserID(userID);
            if (foundUser != null) {
                Entitlement entitlement = this.getEntitlementById(entitlementID);
                foundUser.addEntitlement(entitlement);
            }
        }
    }

    /**
     * Logs a user into the AuthenticationService.  Modifies the AccessToken of the User to expire in 1 hour.
     *
     * @param username   the username to authenticate
     * @param password   the user's password to authenticate
     * @throws AccessDeniedException  thrown if the credentials passed are invalid
     * @return the AccessToken of the user
     */
    @Override
    public AccessToken login(String username, String password) throws AccessDeniedException {
        User foundUser = getUserByUsername(username);
        if (foundUser != null) {
            if ( foundUser.validatePassword(password) ) {
                // whether the user has an existing access token or not, just generate a new on that will
                // expire in an hour, assign that to the user, and return it
                AccessToken token = new AccessToken( foundUser.getID() );
                foundUser.setAccessToken(token);
                return foundUser.getAccessToken();
            }
        }
        throw new AccessDeniedException(username, "", 0, "", null);
    }

    /**
     * Logs the User out that owns the AccessToken with the supplied ID.  Modifies the AccessToken to set the
     * expiration time to be now.
     *
     * @param tokenID  the id of the AccessToken for the owning user
     */
    @Override
    public void logout(String tokenID) {
        // need to find the user that owns the token, then destroy the token on that user
        User foundUser = getUserByAccessTokenID(tokenID);
        if (foundUser != null) {
            AccessToken foundToken = foundUser.getAccessToken();
            if (foundToken != null && foundToken.getId().equals(tokenID)) {
                foundToken.setLastUpdated(new Date());
                foundToken.setExpirationTime(new Date());
            }
        }
    }

    /**
     * Checks if the User that owns the AccessToken corresponding to the passed tokenID has permission to use
     * the Permission.  Looks up the AccessToken's owning User, and inspects their Entitlements to confirm whether
     * or not the user has the corresponding Permission.
     *
     * @param tokenID       the id of the AccessToken to check
     * @param permissionID  the id of the Permission to check the user for
     * @return true if the user has the Permission, false otherwise
     */
    @Override
    public boolean mayAccess(String tokenID, String permissionID) {
        User foundUser = getUserByAccessTokenID(tokenID);
        if (foundUser != null) {
            // if the user HAS an access token currently but it's expired, throw an exception and have them login again
            AccessToken foundToken = foundUser.getAccessToken();
            if (foundToken != null && !foundToken.getExpirationTime().after(new Date())) {
                return false;
            } else {
                return foundUser.hasPermission(permissionID);
            }
        }
        return false;
    }

    /**
     * Checks if the User that owns the AccessToken corresponding to the passed tokenID has permission to use
     * the Permission.  Looks up the AccessToken's owning User, and inspects their Entitlements to confirm whether
     * or not the user has the corresponding Permission.
     *
     * @param tokenID         the id of the AccessToken to check
     * @param permissionType  the PermissionType to check the user for
     * @return true if the user has the Permission, false otherwise
     */
    public boolean mayAccess(String tokenID, PermissionType permissionType) {
        return mayAccess(tokenID, permissionType.getPermissionName());
    }

    /**
     * Returns a string representation of the entire Authentication catalog, including Services,
     * Users, Roles, and Permissions.  Uses the Visitor pattern to visit each Service, User, Role, and Permission to
     * inquire about the object's salient properties.
     *
     * @return  string representation of the entire Authentication catalog
     */
    @Override
    public String getInventory() {
        AuthenticationVisitor av = new AuthenticationVisitor();

        // get counts of how many services and users, and how many distinct Permissions/Roles
        int numServices = this.services.size();
        int numUsers = this.users.size();
        int numEntitlements = this.entitlements.size();

        StringBuilder inventory = new StringBuilder();
        inventory.append("Authentication Service API Inventory\n------------------------------------\n\n");
        inventory.append(String.format("There are [%d] registered Users.  They are:\n",numUsers));
        for (User u : users) {
            inventory.append( u.acceptVisitor(av) );
        }

        inventory.append("\n");
        inventory.append(String.format("There are [%d] defined Services.  They are:\n",numServices));
        for (Service s : services) {
            inventory.append( s.acceptVisitor(av) );
        }

        inventory.append("\n");
        inventory.append(String.format("There are [%d] defined Entitlements (Roles or Permissions).  They are:\n",numEntitlements));
        for (Entitlement e : entitlements) {
            inventory.append( e.acceptVisitor(av) );
        }

        return inventory.toString();
    }


    /* begin region: Private helper methods */

    /**
     * Helper method to retrieve a User by their ID.
     *
     * @param userID  the id of the user to find
     * @return  the found user
     */
    private User getUserByUserID(String userID) {
        for (User user : users) {
            if (user.getID().equals(userID) ) {
                return user;
            }
        }
        return null;
    }

    /**
     * Helper method to retrieve a User by any of their associated usernames.
     *
     * @param username  the username of the user to find
     * @return  the found user
     */
    private User getUserByUsername(String username) {
        for (User user : users) {
            for (Credentials credentials : user.getCredentials()) {
                if ( credentials.getUsername().equals(username)) {
                    return user;
                }
            }
        }
        return null;
    }

    /**
     * Helper method to retrieve a User by the id of their AccessToken
     *
     * @param tokenID  the id of the AccessToken that the belongs to the user
     * @return  the found user
     */
    private User getUserByAccessTokenID(String tokenID) {
        for (User user : users) {
            AccessToken token = user.getAccessToken();
            if (token != null && token.getId().equals(tokenID)) {
                return user;
            }
        }
        return null;
    }

    /**
     * Helper method to retrieve an Entitlement by its ID.
     *
     * @param entitlementId  the id of the Entitlement to find
     * @return  the found Entitlement
     */
    private Entitlement getEntitlementById(String entitlementId) {
        for (Entitlement e : this.entitlements) {
            if (e.getID().equals(entitlementId))
                return e;
        }
        return null;
    }

    /**
     * Helper method to retrieve a Service by its ID.
     *
     * @param serviceId  the id of the Service to find
     * @return  the found Service
     */
    private Service getServiceById(String serviceId) {
        for (Service s : this.services) {
            if (s.getID().equals(serviceId))
                return s;
        }
        return null;
    }

    /* end region: Private helper methods */

}