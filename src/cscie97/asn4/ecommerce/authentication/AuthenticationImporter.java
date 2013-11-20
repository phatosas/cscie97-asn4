package cscie97.asn4.ecommerce.authentication;

import cscie97.asn4.ecommerce.collection.CollectionImporter;
import cscie97.asn4.ecommerce.csv.Importer;
import cscie97.asn4.ecommerce.exception.*;
import org.apache.commons.lang3.StringUtils;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Provides a single public method for handling the creation of new Authentication Service items, which include:
 * <ul>
 *     <li>{@link cscie97.asn4.ecommerce.authentication.User}</li>
 *     <li>{@link cscie97.asn4.ecommerce.authentication.Role}</li>
 *     <li>{@link cscie97.asn4.ecommerce.authentication.Permission}</li>
 *     <li>{@link cscie97.asn4.ecommerce.authentication.Service}</li>
 * </ul>
 * Requires a CSV file be passed that contains the definitions of all items.  Also allows for defining the Permissions
 * that comprise a Role, adding child Roles to Roles, adding Permissions to Services, and adding Credentials to Users
 * and granting Roles to Users.
 *
 * @author David Killeffer &lt;rayden7@gmail.com&gt;
 * @version 1.0
 * @see cscie97.asn4.ecommerce.authentication.IAuthenticationServiceAPI
 * @see cscie97.asn4.ecommerce.authentication.User
 * @see cscie97.asn4.ecommerce.authentication.Role
 * @see cscie97.asn4.ecommerce.authentication.Permission
 * @see cscie97.asn4.ecommerce.authentication.Service
 */
public class AuthenticationImporter extends Importer {

    /**
     * Public method for importing Authentication items into the Authentication Service catalog, including Services,
     * Roles, Permissions, and Users, and setting all appropriate attributes on those objects.
     *
     * @param tokenID                    access token for carrying out restricted interface actions such as this
     * @param filename                file with authentication items to load into the authentication catalog
     * @throws ImportException        thrown when encountering non-parse related exceptions in the import process
     * @throws ParseException         thrown when encountering any issues parsing the input file related to the format of the file contents
     * @throws AccessDeniedException  thrown when encountering any permission-related issues calling the restricted methods of the IAuthenticationServiceAPI
     */
    public static void importAuthenticationFile(String tokenID, String filename)
            throws ImportException, ParseException, AccessDeniedException {

        IAuthenticationServiceAPI authenticationAPI = AuthenticationServiceAPI.getInstance();

        // in order to run the import of the authentication.csv file, the User who owns the passed GUID AccessToken
        // must have ALL of the permissions on the Authentication Service API, which include:
        if (
                authenticationAPI.mayAccess(tokenID, PermissionType.DEFINE_SERVICE) &&
                        authenticationAPI.mayAccess(tokenID, PermissionType.DEFINE_PERMISSION) &&
                        authenticationAPI.mayAccess(tokenID, PermissionType.DEFINE_ROLE) &&
                        authenticationAPI.mayAccess(tokenID, PermissionType.CREATE_USER) &&
                        authenticationAPI.mayAccess(tokenID, PermissionType.ADD_ENTITLEMENT_TO_ROLE) &&
                        authenticationAPI.mayAccess(tokenID, PermissionType.ADD_CREDENTIAL_TO_USER) &&
                        authenticationAPI.mayAccess(tokenID, PermissionType.ADD_ENTITLEMENT_TO_USER)
                ) {
            int lineNumber = 0;  // keep track of what lineNumber we're reading in from the input file for exception handling
            String line;  // store the text on each line as it's processed

            try {
                BufferedReader reader = new BufferedReader(new FileReader(filename));

                while ((line = reader.readLine()) != null) {
                    lineNumber++;

                    // FIRST check if we encountered an empty line, and just skip to the next one if so
                    if (line.length() == 0) { continue; }

                    // SECOND check if the line contains column headers, since some lines may contain comments
                    // (preceeded by hash character); if first character is a hash, skip to next line
                    if (line.substring(0,1).matches("#")) { continue; }

                    String[] cleanedColumns = CollectionImporter.parseCSVLine(line, ",");

                    // depending on both the size of cleanedColumns as well as the first item in the array,
                    // call the appropriate method to handle the command
                    if (cleanedColumns != null ) {
                        try {
                            // define service
                            if (cleanedColumns.length == 4 && cleanedColumns[0].equalsIgnoreCase("define_service")) {
                                AuthenticationImporter.defineService(tokenID, cleanedColumns);
                            }
                            // define permission
                            if (cleanedColumns.length == 5 && cleanedColumns[0].equalsIgnoreCase("define_permission")) {
                                AuthenticationImporter.definePermission(tokenID, cleanedColumns);
                            }
                            // define role
                            if (cleanedColumns.length == 4 && cleanedColumns[0].equalsIgnoreCase("define_role")) {
                                AuthenticationImporter.defineRole(tokenID, cleanedColumns);
                            }
                            // add entitlement to role
                            if (cleanedColumns.length == 3 && cleanedColumns[0].equalsIgnoreCase("add_entitlement_to_role")) {
                                AuthenticationImporter.addEntitlementToRole(tokenID, cleanedColumns);
                            }
                            // create user
                            if (cleanedColumns.length == 3 && cleanedColumns[0].equalsIgnoreCase("create_user")) {
                                AuthenticationImporter.createUser(tokenID, cleanedColumns);
                            }
                            // add credential
                            if (cleanedColumns.length == 4 && cleanedColumns[0].equalsIgnoreCase("add_credential")) {
                                AuthenticationImporter.addCredentialToUser(tokenID, cleanedColumns);
                            }
                            // add entitlement to user
                            if (cleanedColumns.length == 3 && cleanedColumns[0].equalsIgnoreCase("add_entitlement_to_user")) {
                                AuthenticationImporter.addEntitlementToUser(tokenID, cleanedColumns);
                            }
                        }
                        catch (ParseException pe) {
                            throw new ParseException(pe.getMessage(), line, lineNumber, filename, pe);
                        }
                    }
                    else {
                        throw new ParseException("Import Authentication line contains invalid data for the authentication data row.",
                                line,
                                lineNumber,
                                filename,
                                null);
                    }
                }

                // lastly, print out an inventory of all the items in the Authentication Service API catalog
                System.out.println(String.format("\n******************************\n"));
                System.out.println(authenticationAPI.getInventory());
                System.out.println(String.format("\n******************************\n"));
            }
            catch (FileNotFoundException fnfe) {
                throw new ImportException("Could not find file ["+filename+"] to open for reading", lineNumber, filename, fnfe);
            }
            catch (IOException ioe) {
                throw new ImportException("Encountered an IOException when trying to open ["+filename+"] for reading", lineNumber, filename, ioe);
            }
            catch (Exception e) {
                throw new ImportException("Caught a generic Exception when attempting to read file ["+filename+"]", lineNumber, filename, e);
            }

        }
        // NOT ALLOWED!
        else throw new AccessDeniedException(tokenID, "", 0, "", null);
    }

    /**
     * Creates services and adds them to the {@link cscie97.asn4.ecommerce.authentication.AuthenticationServiceAPI}.
     * The format of each element in authenticationData should be:
     * <ol>
     *     <li><b>define_service</b></li>
     *     <li>service ID</li>
     *     <li>service name</li>
     *     <li>service description</li>
     * </ol>
     *
     * @param tokenID                   access token for carrying out restricted interface actions such as this
     * @param authenticationData     string array of lines (each line is part of a CSV file) to be parsed and loaded as a new Authentication Service
     * @throws cscie97.asn4.ecommerce.exception.ParseException    if an error occurred parsing the authenticationData
     */
    private static void defineService(String tokenID, String[] authenticationData) throws ParseException {
        // ensure that we have at least 4 elements passed and that the first element is "define_collection"
        if (authenticationData == null ||
            authenticationData.length != 4 ||
            !authenticationData[0].trim().equalsIgnoreCase(PermissionType.DEFINE_SERVICE.getPermissionName())
        ) {
            throw new ParseException("Import Authentication line contains invalid data when calling defineService(): "+ StringUtils.join(authenticationData, ","),
                    null,
                    0,
                    null,
                    null);
        }

        Service service = new Service();
        service.setID(authenticationData[1].trim());
        service.setName(authenticationData[2].trim());
        service.setDescription(authenticationData[3].trim());

        IAuthenticationServiceAPI authenticationAPI = AuthenticationServiceAPI.getInstance();
        authenticationAPI.addService(tokenID, service);

        System.out.println(String.format("Adding Service to AuthenticationService catalog: [%s]\n", service));
    }

    /**
     * Creates permissions and adds them to the {@link cscie97.asn4.ecommerce.authentication.AuthenticationServiceAPI}.
     * The format of each element in authenticationData should be:
     * <ol>
     *     <li><b>define_permission</b></li>
     *     <li>service ID</li>
     *     <li>permission id</li>
     *     <li>permission name</li>
     *     <li>service description</li>
     * </ol>
     *
     * @param tokenID                   access token for carrying out restricted interface actions such as this
     * @param authenticationData     string array of lines (each line is part of a CSV file) to be parsed and loaded as a new Authentication Permission
     * @throws cscie97.asn4.ecommerce.exception.ParseException    if an error occurred parsing the authenticationData
     */
    private static void definePermission(String tokenID, String[] authenticationData) throws ParseException {
        // ensure that we have at least 5 elements passed and that the first element is "define_permission"
        if (authenticationData == null ||
                authenticationData.length != 5 ||
                !authenticationData[0].trim().equalsIgnoreCase(PermissionType.DEFINE_PERMISSION.getPermissionName())
        ) {
            throw new ParseException("Import Authentication line contains invalid data when calling definePermission(): "+ StringUtils.join(authenticationData, ","),
                    null,
                    0,
                    null,
                    null);
        }

        Permission permission = new Permission();
        permission.setID(authenticationData[2].trim());
        permission.setName(authenticationData[3].trim());
        permission.setDescription(authenticationData[4].trim());

        String serviceID = authenticationData[1].trim();

        IAuthenticationServiceAPI authenticationAPI = AuthenticationServiceAPI.getInstance();
        authenticationAPI.addPermissionToService(tokenID, serviceID, permission);

        System.out.println(String.format("Adding Permission on ServiceID [%s] to AuthenticationService catalog: [%s]\n", serviceID, permission));
    }

    /**
     * Creates roles and adds them to the {@link cscie97.asn4.ecommerce.authentication.AuthenticationServiceAPI}.
     * The format of each element in authenticationData should be:
     * <ol>
     *     <li><b>define_role</b></li>
     *     <li>role id</li>
     *     <li>role name</li>
     *     <li>role description</li>
     * </ol>
     *
     * @param tokenID                   access token for carrying out restricted interface actions such as this
     * @param authenticationData     string array of lines (each line is part of a CSV file) to be parsed and loaded as a new Authentication Permission
     * @throws cscie97.asn4.ecommerce.exception.ParseException    if an error occurred parsing the authenticationData
     */
    private static void defineRole(String tokenID, String[] authenticationData) throws ParseException {
        // ensure that we have exactly 4 elements passed and that the first element is "define_role"
        if (authenticationData == null ||
                authenticationData.length != 4 ||
                !authenticationData[0].trim().equalsIgnoreCase(PermissionType.DEFINE_ROLE.getPermissionName())
        ) {
            throw new ParseException("Import Authentication line contains invalid data when calling defineRole(): "+ StringUtils.join(authenticationData, ","),
                    null,
                    0,
                    null,
                    null);
        }

        Role role = new Role();
        role.setID(authenticationData[1].trim());
        role.setName(authenticationData[2].trim());
        role.setDescription(authenticationData[3].trim());

        IAuthenticationServiceAPI authenticationAPI = AuthenticationServiceAPI.getInstance();
        authenticationAPI.addRole(tokenID, role);

        System.out.println(String.format("Adding Role to AuthenticationService catalog: [%s]\n", role));
    }

    /**
     * Creates registered users and adds them to the {@link cscie97.asn4.ecommerce.authentication.AuthenticationServiceAPI}.
     * The format of each element in authenticationData should be:
     * <ol>
     *     <li><b>create_user</b></li>
     *     <li>user id</li>
     *     <li>user name</li>
     * </ol>
     *
     * @param tokenID                   access token for carrying out restricted interface actions such as this
     * @param authenticationData     string array of lines (each line is part of a CSV file) to be parsed and loaded as a new Authentication Service registered User
     * @throws cscie97.asn4.ecommerce.exception.ParseException    if an error occurred parsing the authenticationData
     */
    private static void createUser(String tokenID, String[] authenticationData) throws ParseException {
        // ensure that we have exactly 3 elements passed and that the first element is "create_user"
        if (authenticationData == null ||
                authenticationData.length != 3 ||
                !authenticationData[0].trim().equalsIgnoreCase(PermissionType.CREATE_USER.getPermissionName())
        ) {
            throw new ParseException("Import Authentication line contains invalid data when calling createUser(): "+ StringUtils.join(authenticationData, ","),
                    null,
                    0,
                    null,
                    null);
        }

        User user = new User(authenticationData[1].trim(),authenticationData[2].trim());

        IAuthenticationServiceAPI authenticationAPI = AuthenticationServiceAPI.getInstance();
        authenticationAPI.addUser(tokenID, user);

        System.out.println(String.format("Adding User to AuthenticationService catalog: [%s]\n", user));
    }

    /**
     * Adds Entitlements (which may be actually either Roles or Permissions) to Roles.  The format of each element
     * in authenticationData should be:
     * <ol>
     *     <li><b>add_entitlement_to_role</b></li>
     *     <li>role id</li>
     *     <li>entitlement id</li>
     * </ol>
     *
     * @param tokenID                   access token for carrying out restricted interface actions such as this
     * @param authenticationData     string array of lines (each line is part of a CSV file) to be parsed and loaded as a new Authentication Service registered User
     * @throws cscie97.asn4.ecommerce.exception.ParseException    if an error occurred parsing the authenticationData
     */
    private static void addEntitlementToRole(String tokenID, String[] authenticationData) throws ParseException {
        // ensure that we have exactly 3 elements passed and that the first element is "add_entitlement_to_role"
        if (authenticationData == null ||
                authenticationData.length != 3 ||
                !authenticationData[0].trim().equalsIgnoreCase(PermissionType.ADD_ENTITLEMENT_TO_ROLE.getPermissionName())
        ) {
            throw new ParseException("Import Authentication line contains invalid data when calling addEntitlementToRole(): "+ StringUtils.join(authenticationData, ","),
                    null,
                    0,
                    null,
                    null);
        }

        String roleID = authenticationData[1].trim();
        String entitlementID = authenticationData[2].trim();

        IAuthenticationServiceAPI authenticationAPI = AuthenticationServiceAPI.getInstance();
        authenticationAPI.addPermissionToRole(tokenID, roleID, entitlementID);

        System.out.println(String.format("Adding Entitlement ID [%s] to Role ID [%s]\n", roleID, entitlementID));
    }

    /**
     * Adds Credentials to users, which are comprised of a username and password (hashed).  Users can have multiple
     * sets of Credentials, which will allow them to login to the AuthenticationServiceAPI with different usernames
     * and password.
     *
     * The format of each element in authenticationData should be:
     * <ol>
     *     <li><b>add_credential</b></li>
     *     <li>user id</li>
     *     <li>username</li>
     *     <li>password</li>
     * </ol>
     *
     * @param tokenID                   access token for carrying out restricted interface actions such as this
     * @param authenticationData     string array of lines (each line is part of a CSV file) to be parsed and loaded as a new Authentication Service registered User
     * @throws cscie97.asn4.ecommerce.exception.ParseException    if an error occurred parsing the authenticationData
     */
    private static void addCredentialToUser(String tokenID, String[] authenticationData) throws ParseException {
        // ensure that we have exactly 4 elements passed and that the first element is "add_credential"
        if (authenticationData == null ||
            authenticationData.length != 4 ||
            !authenticationData[0].trim().equalsIgnoreCase(PermissionType.ADD_CREDENTIAL_TO_USER.getPermissionName())
        ) {
            throw new ParseException("Import Authentication line contains invalid data when calling addCredential(): "+ StringUtils.join(authenticationData, ","),
                    null,
                    0,
                    null,
                    null);
        }

        String userID = authenticationData[1].trim();
        String username = authenticationData[2].trim();
        String password = authenticationData[3].trim();

        IAuthenticationServiceAPI authenticationAPI = AuthenticationServiceAPI.getInstance();
        authenticationAPI.addCredentialToUser(tokenID, userID, username, password);

        System.out.println(String.format("Adding Credentials username [%s] password [%s] to User ID [%s]\n", username, password, userID));
    }

    /**
     * Adds Entitlements to users, which can be either a Permission or Role.
     *
     * The format of each element in authenticationData should be:
     * <ol>
     *     <li><b>add_entitlement_to_user</b></li>
     *     <li>user id</li>
     *     <li>entitlement id</li>
     * </ol>
     *
     * @param tokenID                   access token for carrying out restricted interface actions such as this
     * @param authenticationData     string array of lines (each line is part of a CSV file) to be parsed and loaded as a new Authentication Service registered User
     * @throws cscie97.asn4.ecommerce.exception.ParseException    if an error occurred parsing the authenticationData
     */
    private static void addEntitlementToUser(String tokenID, String[] authenticationData) throws ParseException {
        // ensure that we have exactly 4 elements passed and that the first element is "add_credential"
        if (authenticationData == null ||
            authenticationData.length != 3 ||
            !authenticationData[0].trim().equalsIgnoreCase(PermissionType.ADD_ENTITLEMENT_TO_USER.getPermissionName())
        ) {
            throw new ParseException("Import Authentication line contains invalid data when calling addEntitlementToUser(): "+ StringUtils.join(authenticationData, ","),
                    null,
                    0,
                    null,
                    null);
        }

        String userID = authenticationData[1].trim();
        String entitlementID  = authenticationData[2].trim();

        IAuthenticationServiceAPI authenticationAPI = AuthenticationServiceAPI.getInstance();
        authenticationAPI.addEntitlementToUser(tokenID, userID, entitlementID);

        System.out.println(String.format("Adding Entitlement ID [%s] to User ID [%s]\n", entitlementID, userID));
    }

}