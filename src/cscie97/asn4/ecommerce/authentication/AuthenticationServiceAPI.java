package cscie97.asn4.ecommerce.authentication;

import cscie97.asn4.ecommerce.collection.Collection;
import cscie97.asn4.ecommerce.collection.ICollectionServiceAPI;
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

            Calendar cal = Calendar.getInstance(); // creates calendar
            cal.setTime(new Date()); // sets calendar time/date
            cal.add(Calendar.HOUR_OF_DAY, 1); // adds one hour

            /// create new AccessToken for super user
            AccessToken token = new AccessToken();
            token.setId( UUID.randomUUID().toString() );
            token.setExpirationTime( cal.getTime() );  // expire in 1 hour
            token.setLastUpdated( new Date() );
            token.setUserID( superUser.getID() );

            superUser.setAccessToken(token);

            ////////////////////////////////////////////////////////////////////////////////////////////////////////////

            // define primary Authentication Service
            String authServiceID = "authentication_service";
            String authServiceName = "Authentication Service";
            String authServiceDescription = "Manage Authentication Configuration and Control Access to Restricted Service Interfaces";
            Service authenticationService = new Service(authServiceID, authServiceName, authServiceDescription);

            Permission p1 = new Permission("define_service","Define Service Permission","Permission to create a new service");
            Permission p2 = new Permission("define_permission","Define Permission Permission","Permission to create a new permission");
            Permission p3 = new Permission("define_role","Define Role Permission","Permission to create a new role");
            Permission p4 = new Permission("add_entitlement","Add entitlement to role permission","Permission to add an entitlement to a role");
            Permission p5 = new Permission("create_user","Create User Permission","Permission to create create a user");
            Permission p6 = new Permission("add_credential_to_user","Add Credential to User Permission","Permission to add credentials to a user");
            Permission p7 = new Permission("add_entitlement_to_user","Add Entitlement to User Permission","Permission to add entitlements to a user");

            // define all Authentication Service Permissions
            Set<Entitlement> allAuthPermissionsSet = new HashSet<Entitlement>(Arrays.asList(p1, p2, p3, p4, p5, p6, p7));
            Set<Permission> allAuthPermissionsList = new HashSet<Permission>(Arrays.asList(p1, p2, p3, p4, p5, p6, p7));

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
        if (this.mayAccess(tokenID, "create_user")) {
            this.users.add(user);
        }
    }


    @Override
    public void addRole(String tokenID, Role role) {
        if (mayAccess(tokenID, "define_role")) {
            this.entitlements.add(role);
        }
    }

    @Override
    public void addPermission(String tokenID, Permission permission) {
        if (mayAccess(tokenID, "define_permission")) {
            this.entitlements.add(permission);
        }
    }

    @Override
    public void addPermissionToService(String tokenID, String serviceID, Permission permission) {
        if (mayAccess(tokenID, "define_permission")) {
            Service service = this.getServiceById(serviceID);
            if (service != null) {
                service.addPermission(permission);
                this.entitlements.add(permission);
            }
        }
    }

    @Override
    public void addPermissionToService(String tokenID, String serviceID, String permissionID) {
            /*
            define_service
            define_permission
            define_role
            add_entitlement_to_role
            create_user
            add_credential
            add entitlement
            */
    }

    @Override
    public void addPermissionToRole(String tokenID, String roleID, String permissionID) {
        if (mayAccess(tokenID, "add_entitlement_to_role")) {
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
                return foundUser.getAccessToken();
            }
        }
        throw new AccessDeniedException(username, "", 0, "", null);
    }

    @Override
    public void logout(String tokenID) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean mayAccess(String tokenID, String permissionID) {
        User foundUser = getUserByAccessTokenID(tokenID);
        if (foundUser != null) {
            for (Entitlement e : foundUser.getEntitlements()) {
                if (e.getID().equals(permissionID)) {
                    return true;
                }
                else if (e instanceof Role) {
                    RoleIterator iterator = ((Role) e).getIterator();
                    while (iterator.hasNext()) {
                        Entitlement e2 = iterator.next();
                        if (e2.getID().equals(permissionID)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    @Override
    public String getInventory() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public User getUserByUsername(String username) {
        for (User user : users) {
            for (Credentials credentials : user.getCredentials()) {
                if ( credentials.getUsername().equals(username)) {
                    return user;
                }
            }
        }
        return null;
    }

    @Override
    public User getUserByAccessTokenID(String tokenID) {
        for (User user : users) {
            AccessToken token = user.getAccessToken();
            if (token != null && token.getId().equals(tokenID)) {
                return user;
            }
        }
        return null;
    }



    /* begin region: Private helper methods */

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
