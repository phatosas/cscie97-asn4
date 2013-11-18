package cscie97.asn4.test;

import cscie97.asn4.ecommerce.authentication.AccessToken;
import cscie97.asn4.ecommerce.authentication.AuthenticationServiceAPI;
import cscie97.asn4.ecommerce.authentication.IAuthenticationServiceAPI;
import cscie97.asn4.ecommerce.csv.*;
import cscie97.asn4.ecommerce.exception.*;
import cscie97.asn4.ecommerce.product.IProductAPI;
import cscie97.asn4.ecommerce.product.Content;
import cscie97.asn4.ecommerce.product.ContentSearch;

/**
 * Test harness for the CSCI-E 97 Assignment 2.  Reads in several supplied input files to load
 * {@link cscie97.asn4.ecommerce.product.Country} objects, {@link cscie97.asn4.ecommerce.product.Device} objects, then
 * {@link cscie97.asn4.ecommerce.product.Content} items, and finally runs several search queries against the
 * {@link cscie97.asn4.ecommerce.product.IProductAPI} to find the content that was loaded.
 *
 * @author David Killeffer &lt;rayden7@gmail.com&gt;
 * @version 1.0
 * @see Importer
 * @see IProductAPI
 * @see Content
 * @see ContentSearch
 */
public class TestDriver {

    /**
     * Executes the primary test logic.  Accepts four command line arguments that should be CSV files.  Command-line
     * arguments should be:
     * <ol>
     *     <li>filename of Countries CSV datafile</li>
     *     <li>filename of Devices CSV datafile</li>
     *     <li>filename of Products CSV datafile</li>
     *     <li>filename of Content Search Queries CSV datafile</li>
     *     <li>filename of Collections CSV datafile</li>
     * </ol>
     *
     * Calls several methods on the {@link cscie97.asn4.ecommerce.csv.Importer} class to load the CSV datafile
     * arguments, including {@link ContentImporter#importCountryFile(String guid, String filename)},
     * {@link ContentImporter#importDeviceFile(String guid, String filename)},
     * {@link ContentImporter#importContentFile(String guid, String filename)} and
     * {@link CollectionImporter#importCollectionsFile(String guid, String filename)}.  Once all Counties, Devices,
     * and Content are loaded, calls {@link cscie97.asn4.ecommerce.csv.SearchEngine#executeQueryFilename(String filename)} to
     * import a CSV of queries to run against the ProductAPI for content items.
     * {@link CollectionImporter#importCollectionsFile(String guid, String filename)} will create new collections,
     * add content items to collections, define the content serch criteria for
     * {@link cscie97.asn4.ecommerce.collection.DynamicCollection}s, and execute searches for text on actual
     * collections themselves.
     *
     * All content item search queries and results, and collection search queries and results, are printed to
     * standard out.
     *
     * @param args  first argument should a CSV datafile of Countries, second argument should be a CSV datafile of
     *              Devices, third argument should be a CSV datafile of Content items, fourth argument should be a
     *              CSV datafile of content search query parameters, and fifth (and last) argument should be a CSV
     *              datafile of collection definitions, adding content to collections, defining dynamic collection
     *              search criteria, and collection searches
     */
    public static void main(String[] args) {
        if (args.length == 6) {
            try {
                IAuthenticationServiceAPI authenticationAPI = AuthenticationServiceAPI.getInstance();

                ////////////////////////////////////////////////////////////////////////////////////////////////////////

                // login as the "super user" to process the Authentication CSV file
                AccessToken superToken = authenticationAPI.login("dkilleffer", "secret");

                AuthenticationImporter.importAuthenticationFile(superToken.getId(), args[0]);

                // logout as the "super user" to process the Authentication CSV file
                authenticationAPI.logout(superToken.getId());

                ////////////////////////////////////////////////////////////////////////////////////////////////////////

                // login as one of the Product Admins to process the Content CSV files
                AccessToken productAdminToken = authenticationAPI.login("sam", "secret");

                ContentImporter.importCountryFile(productAdminToken.getId(), args[1]);
                ContentImporter.importDeviceFile(productAdminToken.getId(), args[2]);
                ContentImporter.importContentFile(productAdminToken.getId(), args[3]);

                SearchEngine.executeQueryFilename(args[4]);

                // logout as the product admin user
                authenticationAPI.logout(productAdminToken.getId());

                ////////////////////////////////////////////////////////////////////////////////////////////////////////


                // login as one of the Collection Admins to process the Collection CSV file
                AccessToken collectionAdminToken = authenticationAPI.login("lucy", "4567");

                CollectionImporter.importCollectionsFile(collectionAdminToken.getId(), args[5]);

                // logout as the collections admin user
                authenticationAPI.logout(collectionAdminToken.getId());

                ////////////////////////////////////////////////////////////////////////////////////////////////////////

            }
            // if we catch an AccessDeniedException, the login information for the super user was incorrect OR the
            // super user was not actually loaded.  Verify that the username/password combo passed in this TestDriver
            // matches up to the correct super user defined at the AuthenticationServiceAPI initialization.
            catch (AccessDeniedException ade) {
                System.out.println(ade.getMessage());
                System.exit(1);
            }
            // if we catch a ParseException, either the original import of Countries, Devices, Content or the
            // execution of the Search Query caused the problem; in either case, the entire program execution should
            // fail and the errors in the original files fixed before the program can be executed again
            catch (ParseException pe) {
                System.out.println(pe.getMessage());
                System.exit(1);
            }
            // if we catch an ImportException, the original load of Countries, Devices, or Content failed for some
            // reason, so the program should fail and exit
            catch (ImportException ie) {
                System.out.println(ie.getMessage());
                System.exit(1);
            }
            // if we catch a QueryEngineException, there was a problem running one of the queries in the file, so
            // the program should fail and exit
            catch (QueryEngineException qee) {
                System.out.println(qee.getMessage());
                System.exit(1);
            }
            // if we catch a CollectionNotFoundException, adding content to collections, defining dynamic collection
            // search criteria, or searching collections failed because of a bad ID passed, so the program should fail and exit
            catch (CollectionNotFoundException cnfe) {
                System.out.println(cnfe.getMessage());
                System.exit(1);
            }
        }
        else {
            System.out.println("Arguments to TestDriver should be: " +
                                    "1) import Authentication CSV file, " +
                                    "2) import Countries CSV file, " +
                                    "3) import Devices CSV file, " +
                                    "4) import Products CSV file, " +
                                    "5) execute Search Query CSV file, and " +
                                    "6) import Collections definitions and Queries CSV file");
            System.exit(1);
        }
    }
}