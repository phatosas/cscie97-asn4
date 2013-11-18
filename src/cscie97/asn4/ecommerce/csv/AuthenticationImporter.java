package cscie97.asn4.ecommerce.csv;

import cscie97.asn4.ecommerce.authentication.*;
import cscie97.asn4.ecommerce.collection.*;
import cscie97.asn4.ecommerce.exception.*;
import cscie97.asn4.ecommerce.product.Content;
import cscie97.asn4.ecommerce.product.ContentSearch;
import cscie97.asn4.ecommerce.product.ProductAPI;
import org.apache.commons.lang3.StringUtils;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Set;

/**
 * Provides a single public method for handling the creation of new {@link cscie97.asn4.ecommerce.collection.Collection}s,
 * adding {@link cscie97.asn4.ecommerce.product.Content} to {@link cscie97.asn4.ecommerce.collection.StaticCollection}s,
 * defining the {@link cscie97.asn4.ecommerce.product.ContentSearch} criteria for
 * {@link cscie97.asn4.ecommerce.collection.DynamicCollection}s, and lastly, for searching over these newly created
 * {@link cscie97.asn4.ecommerce.collection.Collection}s. Each sub-task under loading the primary CSV is handled by
 * private methods.
 *
 * @author David Killeffer &lt;rayden7@gmail.com&gt;
 * @version 1.0
 * @see cscie97.asn4.ecommerce.collection.ICollectionServiceAPI
 * @see cscie97.asn4.ecommerce.collection.CollectionServiceAPI
 * @see cscie97.asn4.ecommerce.collection.Collectible
 * @see cscie97.asn4.ecommerce.collection.Collection
 * @see cscie97.asn4.ecommerce.collection.ContentProxy
 * @see cscie97.asn4.ecommerce.collection.StaticCollection
 * @see cscie97.asn4.ecommerce.collection.DynamicCollection
 */
public class AuthenticationImporter extends Importer {

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
     * @param guid                   access token for carrying out restricted interface actions such as this
     * @param authenticationData     string array of lines (each line is part of a CSV file) to be parsed and loaded as a new Authentication Service
     * @throws cscie97.asn4.ecommerce.exception.ParseException    if an error occurred parsing the authenticationData
     */
    private static void defineService(String guid, String[] authenticationData) throws ParseException {
        // ensure that we have at least 4 elements passed and that the first element is "define_collection"
        if (authenticationData == null ||
            authenticationData.length != 4 ||
            !authenticationData[0].trim().equalsIgnoreCase("define_service")
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
        authenticationAPI.addService(guid, service);

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
     * @param guid                   access token for carrying out restricted interface actions such as this
     * @param authenticationData     string array of lines (each line is part of a CSV file) to be parsed and loaded as a new Authentication Permission
     * @throws cscie97.asn4.ecommerce.exception.ParseException    if an error occurred parsing the authenticationData
     */
    private static void definePermission(String guid, String[] authenticationData) throws ParseException {
        // ensure that we have at least 5 elements passed and that the first element is "define_permission"
        if (authenticationData == null ||
                authenticationData.length != 5 ||
                !authenticationData[0].trim().equalsIgnoreCase("define_permission")
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
        authenticationAPI.addPermissionToService(guid, serviceID, permission);

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
     * @param guid                   access token for carrying out restricted interface actions such as this
     * @param authenticationData     string array of lines (each line is part of a CSV file) to be parsed and loaded as a new Authentication Permission
     * @throws cscie97.asn4.ecommerce.exception.ParseException    if an error occurred parsing the authenticationData
     */
    private static void defineRole(String guid, String[] authenticationData) throws ParseException {
        // ensure that we have exactly 4 elements passed and that the first element is "define_role"
        if (authenticationData == null ||
                authenticationData.length != 4 ||
                !authenticationData[0].trim().equalsIgnoreCase("define_role")
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
        authenticationAPI.addRole(guid, role);

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
     * @param guid                   access token for carrying out restricted interface actions such as this
     * @param authenticationData     string array of lines (each line is part of a CSV file) to be parsed and loaded as a new Authentication Service registered User
     * @throws cscie97.asn4.ecommerce.exception.ParseException    if an error occurred parsing the authenticationData
     */
    private static void createUser(String guid, String[] authenticationData) throws ParseException {
        // ensure that we have exactly 3 elements passed and that the first element is "create_user"
        if (authenticationData == null ||
                authenticationData.length != 3 ||
                !authenticationData[0].trim().equalsIgnoreCase("create_user")
        ) {
            throw new ParseException("Import Authentication line contains invalid data when calling createUser(): "+ StringUtils.join(authenticationData, ","),
                    null,
                    0,
                    null,
                    null);
        }

        User user = new User(authenticationData[1].trim(),authenticationData[2].trim());

        IAuthenticationServiceAPI authenticationAPI = AuthenticationServiceAPI.getInstance();
        authenticationAPI.addUser(guid, user);

        System.out.println(String.format("Adding User to AuthenticationService catalog: [%s]\n", user));
    }



    private static void addEntitlementToRole(String guid, String[] authenticationData) throws ParseException {
     /*
     3
     # add_entitlement_to_role, <role_id>, <entitlement_id>
     add_entitlement_to_role, authentication_admin_role, define_service
     */
    }

    private static void addCredential(String guid, String[] authenticationData) throws ParseException {
     /*
     4
     # add_credential, <user_id>, <login_name>, <password>
     add_credential authentication_admin, jill, 1234567
     */
    }

    private static void addEntitlementToUser(String guid, String[] authenticationData) throws ParseException {
     /*
     3
     # add_entitlement_to_user, <userid>, <entitlementid>
     add_entitlement_to_user, authentication_admin, authentication_admin_role
     */
    }



















    /**
     * Public method for importing {@link cscie97.asn4.ecommerce.collection.Collection} items into the authentication
     *
     *
     * catalog.
     *
     * Note that any {@link cscie97.asn4.ecommerce.product.Content} items referenced in the import file must
     * already exist in the Product catalog first, or the import of that Collection items will not work.
     * Throws ImportException on error accessing or processing the input Collections File.
     *
     * @param guid                    access token for carrying out restricted interface actions such as this
     * @param filename                file with collection items to load into the product catalog
     * @throws cscie97.asn4.ecommerce.exception.ImportException        thrown when encountering non-parse related exceptions in the import process
     * @throws ParseException         thrown when encountering any issues parsing the input file related to the format of the file contents
     */
    public static void importAuthenticationFile(String guid, String filename)
            throws ImportException, ParseException, AccessDeniedException {

        IAuthenticationServiceAPI authenticationAPI = AuthenticationServiceAPI.getInstance();

        // in order to run the import of the authentication.csv file, the User who owns the GUID AccessToken must have ALL of the permissions on the Authentication Service API, which include:
        //
        // define_service
        // define_permission
        // define_role
        // add_entitlement
        // create_user
        // add_credential_to_user
        // add_entitlement_to_user
        //

        boolean mayDefineService = authenticationAPI.mayAccess(guid, "define_service");
        boolean mayDefinePermission = authenticationAPI.mayAccess(guid, "define_permission");
        boolean mayDefineRole = authenticationAPI.mayAccess(guid, "define_role");
        boolean mayAddEntitlement = authenticationAPI.mayAccess(guid, "add_entitlement");
        boolean mayCreateUser = authenticationAPI.mayAccess(guid, "create_user");
        boolean mayAddUserCredentials = authenticationAPI.mayAccess(guid, "add_credential_to_user");
        boolean mayAddUserEntitlement = authenticationAPI.mayAccess(guid, "add_entitlement_to_user");

        if (
                authenticationAPI.mayAccess(guid, "define_service") &&
                authenticationAPI.mayAccess(guid, "define_permission") &&
                authenticationAPI.mayAccess(guid, "define_role") &&
                authenticationAPI.mayAccess(guid, "add_entitlement") &&
                authenticationAPI.mayAccess(guid, "create_user") &&
                authenticationAPI.mayAccess(guid, "add_credential_to_user") &&
                authenticationAPI.mayAccess(guid, "add_entitlement_to_user")
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
                                AuthenticationImporter.defineService(guid, cleanedColumns);
                            }
                            // define permission
                            if (cleanedColumns.length == 5 && cleanedColumns[0].equalsIgnoreCase("define_permission")) {
                                AuthenticationImporter.definePermission(guid, cleanedColumns);
                            }
                            // define role
                            if (cleanedColumns.length == 4 && cleanedColumns[0].equalsIgnoreCase("define_role")) {
                                AuthenticationImporter.defineRole(guid, cleanedColumns);
                            }
                            // add entitlement to role
                            if (cleanedColumns.length == 3 && cleanedColumns[0].equalsIgnoreCase("add_entitlement_to_role")) {
                                AuthenticationImporter.addEntitlementToRole(guid, cleanedColumns);
                            }
                            // create user
                            if (cleanedColumns.length == 3 && cleanedColumns[0].equalsIgnoreCase("create_user")) {
                                AuthenticationImporter.createUser(guid, cleanedColumns);
                            }
                            // add credential
                            if (cleanedColumns.length == 4 && cleanedColumns[0].equalsIgnoreCase("add_credential")) {
                                AuthenticationImporter.addCredential(guid, cleanedColumns);
                            }
                            // add entitlement to user
                            if (cleanedColumns.length == 3 && cleanedColumns[0].equalsIgnoreCase("add_entitlement_to_user")) {
                                AuthenticationImporter.addEntitlementToUser(guid, cleanedColumns);
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

                /**
                // lastly, exercise iterating over ALL the collections
                System.out.println(String.format("\n******************************\n"));
                System.out.println("ITERATING OVER ALL COLLECTIONS...\n\n");
                // per requirements, iterate over all collections and print out their contents
                CollectionIterator allCollectionsIterator = CollectionServiceAPI.getInstance().getCollectionIterator("staticA");
                while (allCollectionsIterator.hasNext()) {
                    Collectible collectible = allCollectionsIterator.next();
                    System.out.println(collectible);

                }
                System.out.println(String.format("\n******************************\n"));
                */


                // TODO: lastly, generate and display an inventory of all Authentication items, grouped by type
                int j = 9;


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
        else throw new AccessDeniedException(guid, "", 0, "", null);
    }

}
