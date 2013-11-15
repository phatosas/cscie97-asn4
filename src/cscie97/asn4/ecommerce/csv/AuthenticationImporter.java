package cscie97.asn4.ecommerce.csv;

import cscie97.asn4.ecommerce.collection.*;
import cscie97.asn4.ecommerce.exception.CollectionNotFoundException;
import cscie97.asn4.ecommerce.exception.ContentNotFoundException;
import cscie97.asn4.ecommerce.exception.ImportException;
import cscie97.asn4.ecommerce.exception.ParseException;
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
     * Creates collections and adds them to the {@link cscie97.asn4.ecommerce.collection.CollectionServiceAPI}.
     * The format of each element in collectionData should be:
     * <ol>
     *     <li><b>define_collection</b></li>
     *     <li><b>static|dynamic</b></li>
     *     <li>collection ID</li>
     *     <li>collection name</li>
     *     <li>collection description</li>
     * </ol>
     *
     * @param guid                    access token for carrying out restricted interface actions such as this
     * @param collectionData          string array of lines (each line is part of a CSV file) to be parsed and loaded as new collections
     * @throws cscie97.asn4.ecommerce.exception.ParseException         if an error occurred parsing the collectionData
     */
    private static void defineCollection(String guid, String[] collectionData) throws ParseException {

        // ensure that we have at least 5 elements passed and that the first element is "define_collection"
        if (collectionData == null || collectionData.length != 5 || !collectionData[0].trim().equalsIgnoreCase("define_collection")) {
            throw new ParseException("Import Collections line contains invalid data when calling definedCollections(): "+ StringUtils.join(collectionData, ","),
                    null,
                    0,
                    null,
                    null);
        }

        Collection collection = Collection.createCollection(collectionData[1].trim());
        collection.setId(collectionData[2].trim());
        collection.setName(collectionData[3].trim());
        collection.setDescription(collectionData[4].trim());

        ICollectionServiceAPI collectionAPI = CollectionServiceAPI.getInstance();
        collectionAPI.addCollection(guid, collection);
        System.out.println(String.format("Adding Collection to CollectionService catalog: [%s]\n", collection));
    }

    /**
     * Adds {@link cscie97.asn4.ecommerce.product.Content} and {@link cscie97.asn4.ecommerce.collection.Collection}
     * items to Collections.
     * The format of each element in collectionData should be:
     * <ol>
     *     <li><b>add_collection_content</b></li>
     *     <li>collection ID</li>
     *     <li><b>product|collection</b></li>
     *     <li>content ID</li>
     * </ol>
     *
     * @param guid                    access token for carrying out restricted interface actions such as this
     * @param collectionData          string array of lines (each line is part of a CSV file) to be parsed and add Content to Collection
     * @throws ParseException         if an error occurred parsing the collectionData
     */
    private static void addContentToCollection(String guid, String[] collectionData)
            throws ParseException, CollectionNotFoundException, ContentNotFoundException
    {
        // ensure that we have at least 4 elements passed and that the first element is "add_collection_content"
        if (collectionData == null || collectionData.length != 4 || !collectionData[0].trim().equalsIgnoreCase("add_collection_content")) {
            throw new ParseException("Import Collections line contains invalid data when calling addContentToCollection(): "+StringUtils.join(collectionData,","),
                    null,
                    0,
                    null,
                    null);
        }

        // first, we need to find the current collection based on the collectionID in the collection catalog
        ICollectionServiceAPI collectionAPI = CollectionServiceAPI.getInstance();
        Collection foundCollection = collectionAPI.getCollectionByID(collectionData[1].trim());
        if (foundCollection == null) {
            throw new CollectionNotFoundException("Add Content to Collection could not find the collection to add content to with ID:"+collectionData[1].trim()+" - arguments passed to addContentToCollection(): "+StringUtils.join(collectionData,","), 0, null, null);
        }

        // ensure that the type we're adding to the collection is either "collection" or "product"
        if (collectionData[2] == null ||
                collectionData[2].trim().length() == 0 ||
                (!collectionData[2].trim().equalsIgnoreCase("collection") && !collectionData[2].trim().equalsIgnoreCase("product"))
                ) {
            throw new ContentNotFoundException("Add Content to Collection encountered an illegal value for type to add:"+collectionData[2].trim()+" - "+StringUtils.join(collectionData,","), 0, null, null);
        }
        // and also ensure that the content ID is not null and at least 1 character in length
        if ( collectionData[3] == null || !(collectionData[3].trim().length() >= 1) ) {
            throw new ContentNotFoundException("Add Content to Collection could not find the content item to add with ID: ["+collectionData[3].trim()+"] - "+StringUtils.join(collectionData,","), 0, null, null);
        }


        Collectible thingToAdd = null;

        // add a child collection to a parent collection
        if (collectionData[2].trim().equalsIgnoreCase("collection")) {
            // find the collection to add
            thingToAdd = collectionAPI.getCollectionByID(collectionData[3].trim());
            if (thingToAdd == null) {
                throw new CollectionNotFoundException("Add Content to Collection could not find the collection attempting to be added [ID: "+collectionData[3].trim()+"] to existing collection [ID:"+collectionData[1].trim()+"] - arguments passed to addContentToCollection(): "+StringUtils.join(collectionData,","), 0, null, null);
            }
        }
        // add a child Content item to a parent collection
        else if (collectionData[2].trim().equalsIgnoreCase("product")) {
            // find the Content item to add, and if found, create a new ContentProxy object for the actual adding to the Collection
            Content foundContent = ProductAPI.getInstance().getContentByID(collectionData[3].trim());
            if (foundContent == null) {
                throw new ContentNotFoundException("Add Content to Collection could not find the content item attempting to be added [ID: "+collectionData[3].trim()+"] to existing collection [ID:"+collectionData[1].trim()+"] - arguments passed to addContentToCollection(): "+StringUtils.join(collectionData,","), 0, null, null);
            }
            thingToAdd = new ContentProxy(foundContent.getID());
        }

        collectionAPI.addContentToCollection(guid, foundCollection.getId(), thingToAdd);
    }

    /**
     * Sets dynamic search criteria attribute on {@link cscie97.asn4.ecommerce.collection.DynamicCollection}s.
     * DynamicCollections use the {@link cscie97.asn4.ecommerce.product.ContentSearch} attribute to search the
     * {@link cscie97.asn4.ecommerce.product.IProductAPI} for matching {@link cscie97.asn4.ecommerce.product.Content}
     * items.
     *
     * The format of each element in collectionData should be:
     * <ol>
     *     <li><b>set_dynamic_criteria</b></li>
     *     <li>collection ID</li>
     *     <li>list of content categories (pipe-separated)</li>
     *     <li>search text (will scan {@link cscie97.asn4.ecommerce.product.Content} name, description, author name)</li>
     *     <li><b>minimum</b> content rating (from 0 to 5, where 5 is best)</li>
     *     <li><b>maximum</b> price (as a float, in BitCoins)</li>
     *     <li>list of supported language codes (pipe-separated)</li>
     *     <li>list of country codes (pipe-separated)</li>
     *     <li>device ID</li>
     *     <li>list of content types (can be any or all of "application", "ringtone", or "wallpaper" currently)</li>
     * </ol>
     *
     * @param guid                    access token for carrying out restricted interface actions such as this
     * @param collectionData          string array of lines (each line is part of a CSV file) to be parsed to set DynamicCollection's search criteria
     * @throws ParseException         if an error occurred parsing the collectionData
     */
    private static void setDynamicCriteria(String guid, String[] collectionData) throws ParseException, CollectionNotFoundException {

        // ensure that we have at least 10 elements passed and that the first element is "set_dynamic_criteria"
        if (collectionData == null || collectionData.length != 10 || !collectionData[0].trim().equalsIgnoreCase("set_dynamic_criteria")) {
            throw new ParseException("Import Collections line contains invalid data when calling setDynamicCriteria(): "+StringUtils.join(collectionData,","),
                    null,
                    0,
                    null,
                    null);
        }

        // first, we need to find the current collection based on the collectionID in the collection catalog
        ICollectionServiceAPI collectionAPI = CollectionServiceAPI.getInstance();
        Collection foundCollection = collectionAPI.getCollectionByID(collectionData[1].trim());
        if (foundCollection == null) {
            throw new CollectionNotFoundException("Set Dynamic Criteria for Collection could not find the collection to set the dynamic content search criteria for with ID:"+collectionData[1].trim()+" - arguments passed to addContentToCollection(): "+StringUtils.join(collectionData,","), 0, null, null);
        }

        // ensure that we can construct our ContentSearch object based on the remaining parameters
        ContentSearch searchCriteria;

        // construct a string that contains ONLY the search criteria portion of the passed collectionData, and make
        // sure to separate each element with a comma and a space character for correct parsing by SearchEngine
        String csvSearchCriteria = StringUtils.join(Arrays.copyOfRange(collectionData, 2, collectionData.length), ", ");

        searchCriteria = SearchEngine.getContentSearchForCSV(csvSearchCriteria);

        CollectionServiceAPI.getInstance().setDynamicCollectionSearchCriteria(guid, foundCollection.getId(), searchCriteria);
    }

    /**
     * Search all {@link cscie97.asn4.ecommerce.collection.Collection}s in the
     * {@link cscie97.asn4.ecommerce.collection.ICollectionServiceAPI} to find those that have full or partial matches
     * to the searchText on either the collection's name or description.
     *
     * @param searchText        text to use for searching on all Collections name and description for partial matches
     * @throws ParseException   if an error occurred while executing the search
     */
    private static void searchCollections(String searchText) throws ParseException {
        // show the original collections query
        System.out.println(String.format("COLLECTIONS SEARCH QUERY: %s\n", searchText));

        Set<Collection> matchingCollections = CollectionServiceAPI.getInstance().searchCollections(searchText);

        if (matchingCollections.size() > 0) {
            System.out.println(String.format("\t[%d] COLLECTIONS MATCH YOUR SEARCH CRITERIA:\n", matchingCollections.size()));
        } else {
            System.out.println("\tNO COLLECTIONS MATCH YOUR SEARCH CRITERIA.\n");
        }

        int resultsCounter = 1;
        for (Collection collection : matchingCollections) {
            System.out.println(String.format("\tMATCHING COLLECTION NAME #%d: %s", resultsCounter, collection.getName()));
            resultsCounter++;
        }
        System.out.println(String.format("\n******************************\n"));
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
    public static void importAuthenticationFile(String guid, String filename) throws ImportException, ParseException, CollectionNotFoundException {





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
                        // define collections
                        if (cleanedColumns.length == 5 && cleanedColumns[0].equalsIgnoreCase("define_collection")) {
                            AuthenticationImporter.defineCollection(guid, cleanedColumns);
                        }

                        // add content to collections
                        if (cleanedColumns.length == 4 && cleanedColumns[0].equalsIgnoreCase("add_collection_content")) {
                            AuthenticationImporter.addContentToCollection(guid, cleanedColumns);
                        }

                        // set dynamic collection content search criteria
                        if (cleanedColumns.length == 10 && cleanedColumns[0].equalsIgnoreCase("set_dynamic_criteria")) {
                            AuthenticationImporter.setDynamicCriteria(guid, cleanedColumns);
                        }

                        // search collections
                        if (cleanedColumns.length == 2 && cleanedColumns[0].equalsIgnoreCase("search_collection")) {
                            AuthenticationImporter.searchCollections(cleanedColumns[1].toLowerCase());
                        }
                    }
                    catch (ParseException pe) {
                        throw new ParseException(pe.getMessage(), line, lineNumber, filename, pe);
                    }
                }
                else {
                    throw new ParseException("Import Collections line contains invalid data for the collection data row.",
                            line,
                            lineNumber,
                            filename,
                            null);
                }
            }

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

}
