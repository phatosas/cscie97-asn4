package cscie97.asn4.ecommerce.authentication;

import cscie97.asn4.ecommerce.exception.AccessDeniedException;

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

        // create an initial "Super User" that may be used to initially load the authentication.csv file
        createSuperUser();
    }

    private void createSuperUser() {
        if (this.superUser == null) {
            // create a hard-coded special "Super User" so that we can use that super user to import the Authentication data
            String superUserGUID = UUID.randomUUID().toString();

            this.superUser = new User(superUserGUID, SUPER_ADMINISTRATOR_USERNAME, "Administrative Super User");

            Credentials credential = new Credentials(SUPER_ADMINISTRATOR_USERNAME, SUPER_ADMINISTRATOR_PASSWORD);
            superUser.addCredential(credential);

            /// create new AccessToken for super user
            superUser.setAccessToken( new AccessToken(superUser.getID()) );


            ////////////////////////////////////////////////////////////////////////////////////////////////////////////

            // define primary Authentication Service
            String authServiceID = "authentication_service";
            String authServiceName = "Authentication Service";
            String authServiceDescription = "Manage Authentication Configuration and Control Access to Restricted Service Interfaces";
            Service authenticationService = new Service(authServiceID, authServiceName, authServiceDescription);

            Permission p1  = new Permission(PermissionType.DEFINE_SERVICE,"Define Service Permission","Permission to create a new service");
            Permission p2  = new Permission(PermissionType.DEFINE_PERMISSION,"Define Permission Permission","Permission to create a new permission");
            Permission p3  = new Permission(PermissionType.DEFINE_ROLE,"Define Role Permission","Permission to create a new role");
            Permission p4  = new Permission(PermissionType.ADD_ENTITLEMENT_TO_ROLE,"Add entitlement to role permission","Permission to add an entitlement to a role");
            Permission p5  = new Permission(PermissionType.CREATE_USER,"Create User Permission","Permission to create create a user");
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
    public void addService(String tokenID, Service service) {
        if (this.mayAccess(tokenID, "define_service")) {
            this.services.add(service);
        }
    }

    @Override
    public void addUser(String tokenID, User user) {
        if (this.mayAccess(tokenID, PermissionType.CREATE_USER)) {
            this.users.add(user);
        }
    }

    @Override
    public void addRole(String tokenID, Role role) {
        if (mayAccess(tokenID, PermissionType.DEFINE_ROLE)) {
            this.entitlements.add(role);
        }
    }

    @Override
    public void addPermission(String tokenID, Permission permission) {
        if (mayAccess(tokenID, PermissionType.DEFINE_PERMISSION)) {
            this.entitlements.add(permission);
        }
    }

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

    @Override
    public void addPermissionToService(String tokenID, String serviceID, String permissionID) {
        Permission permission = (Permission) this.getEntitlementById(permissionID);
        this.addPermissionToService(tokenID, serviceID, permission);
    }

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

    @Override
    public void addCredentialToUser(String tokenID, String userID, String username, String password) {
        if (mayAccess(tokenID, PermissionType.ADD_CREDENTIAL_TO_USER)) {
            User foundUser = this.getUserByUserID(userID);
            if (foundUser != null) {
                foundUser.addCredential( new Credentials(username, password) );
            }
        }
    }

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

    @Override
    public void createService(String tokenID, String id, String name, String description) {
        this.addService(tokenID, new Service(id,name,description) );
    }

    @Override
    public void createPermission(String tokenID, String id, String name, String description) {
        this.addPermission(tokenID, new Permission(id,name,description) );
    }

    @Override
    public void createRole(String tokenID, String id, String name, String description) {
        this.addRole(tokenID, new Role(id, name, description));
    }

    @Override
    public void createUser(String tokenID, String id, String name) {
        this.addUser(tokenID, new User(id, name, "User account for"+name));
    }

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

    @Override
    public boolean mayAccess(String tokenID, String permissionID) {
        User foundUser = getUserByAccessTokenID(tokenID);
        if (foundUser != null) {
            // if the user HAS an access token currently but it's expired, throw an exception and have them login again
            AccessToken foundToken = foundUser.getAccessToken();
            if (foundToken != null && !foundToken.getExpirationTime().after(new Date())) {
                return false;
            }
            else {
                return foundUser.hasPermission(permissionID);
            }
        }
        return false;
    }

    public boolean mayAccess(String tokenID, PermissionType permissionType) {
        return mayAccess(tokenID, permissionType.getPermissionName());
    }

    @Override
    public String getInventory() {

        // TODO: use the visitor pattern to display an inventory of all the classes

        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }


    /* begin region: Private helper methods */

    private User getUserByUserID(String userID) {
        for (User user : users) {
            if (user.getID().equals(userID) ) {
                return user;
            }
        }
        return null;
    }

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

    private User getUserByAccessTokenID(String tokenID) {
        for (User user : users) {
            AccessToken token = user.getAccessToken();
            if (token != null && token.getId().equals(tokenID)) {
                return user;
            }
        }
        return null;
    }

    private Entitlement getEntitlementById(String entitlementId) {
        for (Entitlement e : this.entitlements) {
            if (e.getID().equals(entitlementId))
                return e;
        }
        return null;
    }

    private Service getServiceById(String serviceId) {
        for (Service s : this.services) {
            if (s.getID().equals(serviceId))
                return s;
        }
        return null;
    }

    /* end region: Private helper methods */

}