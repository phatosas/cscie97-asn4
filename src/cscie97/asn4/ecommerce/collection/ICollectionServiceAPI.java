package cscie97.asn4.ecommerce.collection;

import cscie97.asn4.ecommerce.product.ContentSearch;
import java.util.Set;

/**
 * Public interface for the CollectionServiceAPI. Consumers and Administrators may search over all collections that
 * match searchText in the Collection name or description.  Administrators may also create collections, add content to
 * collections, and for DynamicCollections, define the search criteria used.
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
public interface ICollectionServiceAPI {

    /**
     * Restricted interface; will validate GUID token before adding content to a collection.  Adds the passed
     * collection to the CollectionService catalog at the top-level.
     *
     * @param guid        the string access token to check for authentication and authorization for carrying out
     *                    restricted actions on the CollectionServiceAPI
     * @param collection  the {@link cscie97.asn4.ecommerce.collection.Collection} to add to the Collection catalog
     */
    public void addCollection(String guid, Collection collection);

    /**
     * Restricted interface; will validate GUID token before adding content to a collection.  Looks up the
     * {@link cscie97.asn4.ecommerce.collection.Collection} with matching collectionId in the catalog and then adds
     * the passed {@link cscie97.asn4.ecommerce.collection.Collectible} to that
     * {@link cscie97.asn4.ecommerce.collection.Collection}.  Note that
     * {@link cscie97.asn4.ecommerce.collection.Collectible}s may be either
     * {@link cscie97.asn4.ecommerce.collection.ContentProxy} items (which wrap
     * {@link cscie97.asn4.ecommerce.product.Content} items that are returned by the
     * {@link cscie97.asn4.ecommerce.product.IProductAPI}, or {@link cscie97.asn4.ecommerce.collection.Collection}
     * objects.
     *
     * @param guid          the string access token to check for authentication and authorization for carrying out
     *                      restricted actions on the CollectionServiceAPI
     * @param collectionId  the collection ID of the {@link cscie97.asn4.ecommerce.collection.Collection} to add the
     *                      collectible to
     * @param collectible   the collectible (which may actually be either a
     *                      {@link cscie97.asn4.ecommerce.collection.ContentProxy} or a
     *                      {@link cscie97.asn4.ecommerce.collection.Collection} item) to add to the found Collection
     */
    public void addContentToCollection(String guid, String collectionId, Collectible collectible);

    /**
     * Given a collection ID, search for any {@link cscie97.asn4.ecommerce.collection.Collection} that matches that
     * code in the collection catalog.  Constructs a private virtual "root" collection and sets all current top-level
     * collections as it's children so that it can iterate over every collection in the catalog to find the one that matches.
     *
     * @param collectionID  the unique collection ID to find the actual {@link cscie97.asn4.ecommerce.collection.Collection}
     * @return              the found {@link cscie97.asn4.ecommerce.collection.Collection} with the matching ID;
     *                      returns null if not found
     */
    public Collection getCollectionByID(String collectionID);

    /**
     * If collectionID passed is not null and corresponds to a valid collection, simply returns the iterator for that
     * collection (note that the {@link cscie97.asn4.ecommerce.collection.CollectionIterator} can also simply be
     * retrieved if a reference to the actual {@link cscie97.asn4.ecommerce.collection.Collection} instance).  If the
     * passed collectionId is null or empty string, constructs a virtual "root" level collection that has all the
     * current top-level Collections as children, and returns a
     * {@link cscie97.asn4.ecommerce.collection.CollectionIterator} which will be able to iterate over every item in
     * the Collection catalog.
     *
     * @param collectionId  the unique collection ID to look up the RoleIterator for; may also be null (in which
     *                      case a virtual root Collection is defined, and the iterator for that is returned)
     * @return              the RoleIterator for the Collection with matching collectionId, or a
     *                      RoleIterator for a virtual "root" level Collection which may traverse all
     *                      Collectibles in the entire catalog
     */
    public CollectionIterator getCollectionIterator(String collectionId);

    /**
     * Finds all {@link cscie97.asn4.ecommerce.collection.Collection}s whose
     * {@link cscie97.asn4.ecommerce.collection.Collection#name} or
     * {@link cscie97.asn4.ecommerce.collection.Collection#description} contains any part of the searchCriteria passed.
     * Note that the search is case-insensitive.
     *
     * To conduct the search, constructs a virtual "root" Collection that has all the top-level Collections as
     * immediate children, and then iterates over all the {@link cscie97.asn4.ecommerce.collection.Collectible}s in
     * this virtual Collection aggregate and finds matching {@link cscie97.asn4.ecommerce.collection.Collection}s.
     *
     * @param searchCriteria  text to find in all Collections name or description
     * @return                unique set of Collections that match any part of the searchCriteria
     */
    public Set<Collection> searchCollections(String searchCriteria);

    /**
     * Restricted interface; will validate GUID token before adding content to a collection.  Looks up the
     * {@link cscie97.asn4.ecommerce.collection.Collection} with matching collectionId in the catalog, ensures that
     * the found collection is actually a {@link cscie97.asn4.ecommerce.collection.DynamicCollection}, and then
     * sets the {@link cscie97.asn4.ecommerce.product.ContentSearch} searchCriteria object on the Collection.
     * Note that at the time the search criteria on a {@link cscie97.asn4.ecommerce.collection.DynamicCollection} is
     * defined, it is immediately executed so that the child elements of the
     * {@link cscie97.asn4.ecommerce.collection.DynamicCollection} are present.
     *
     * @param guid            the string access token to check for authentication and authorization for carrying out
     *                        restricted actions on the CollectionServiceAPI
     * @param collectionId    the collection ID of the {@link cscie97.asn4.ecommerce.collection.DynamicCollection} to
     *                        define the add the search criteria for
     * @param searchCriteria  the collectible (which may actually be either a
     *                        {@link cscie97.asn4.ecommerce.collection.ContentProxy} or a
     *                        {@link cscie97.asn4.ecommerce.collection.Collection} item) to add to the found Collection
     */
    public void setDynamicCollectionSearchCriteria(String guid, String collectionId, ContentSearch searchCriteria);

    /**
     * Verifies that the <b>guid</b> access token passed is authenticated and authorized for carrying out
     * restricted actions on the CollectionServiceAPI (such as adding new Collections, adding Content to Collection,
     * etc.).
     * <b>Note that for this version of the CollectionServiceAPI, this method is mocked and will return true for
     * any string passed.</b>
     *
     * @param guid  the string access token to check for authentication and authorization for carrying out
     *              restricted actions on the CollectionServiceAPI
     * @return      true if guid is authenticated and authorized to execute restricted actions on CollectionServiceAPI,
     *              false otherwise
     */
    public boolean validateAccessToken(String guid);

}