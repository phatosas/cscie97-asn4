package cscie97.asn4.ecommerce.collection;

import cscie97.asn4.ecommerce.product.Content;
import cscie97.asn4.ecommerce.product.ContentSearch;
import cscie97.asn4.ecommerce.product.ProductAPI;

/**
 * DynamicCollections contain a {@link cscie97.asn4.ecommerce.product.ContentSearch} object which is executed upon
 * defining the {@link cscie97.asn4.ecommerce.product.ContentSearch}.  DynamicCollections may initially be created as
 * empty "shell" objects that have not yet defined the
 * {@link cscie97.asn4.ecommerce.collection.DynamicCollection#searchCriteria} property for.  Once the
 * {@link cscie97.asn4.ecommerce.collection.DynamicCollection#searchCriteria} is declared (either by calling
 * {@link cscie97.asn4.ecommerce.collection.DynamicCollection#setSearchCriteria(cscie97.asn4.ecommerce.product.ContentSearch)}
 * or by passing it to the class constructor) however, the actual search is immediately executed by calling the
 * {@link cscie97.asn4.ecommerce.product.IProductAPI#searchContent(cscie97.asn4.ecommerce.product.ContentSearch)}
 * method and passing the search criteria object.  Returned matching {@link cscie97.asn4.ecommerce.product.Content}
 * items are wrapped as {@link cscie97.asn4.ecommerce.collection.ContentProxy} objects because they inherit from
 * {@link cscie97.asn4.ecommerce.collection.Collectible}.  By executing the search criteria as soon as it is defined
 * the DynamicCollection will have children objects available immediately after defining the search criteria, which
 * aids in iteration (see also {@link cscie97.asn4.ecommerce.collection.CollectionIterator}).
 *
 * @author David Killeffer &lt;rayden7@gmail.com&gt;
 * @version 1.0
 * @see cscie97.asn4.ecommerce.collection.Collection
 * @see cscie97.asn4.ecommerce.collection.Collectible
 * @see cscie97.asn4.ecommerce.product.Content
 * @see cscie97.asn4.ecommerce.product.ContentSearch
 */
public class DynamicCollection extends Collection {

    /**
     * ContentSearch object used by the collection to define what criteria to use when searching for matching
     * {@link cscie97.asn4.ecommerce.product.Content} items that will first be wrapped as
     * {@link cscie97.asn4.ecommerce.collection.ContentProxy} objects, and then included as children of the
     * DynamicCollection.
     */
    private ContentSearch searchCriteria = null;

    /**
     * Class constructor.  Allows for "empty" DynamicCollections to be created.
     */
    public DynamicCollection() { }

    /**
     * Class constructor.  When the ContentSearch argument is passed, the class property is set and also the
     * search is immediately executed.
     *
     * @param criteria    a {@link cscie97.asn4.ecommerce.product.ContentSearch} object defining the search parameters
     *                    for {@link cscie97.asn4.ecommerce.product.Content} items
     */
    public DynamicCollection(ContentSearch criteria) {
        this.searchCriteria = criteria;
        executeSearch();
    }

    /**
     * Retrieves the {@link cscie97.asn4.ecommerce.product.ContentSearch} object defined for the
     * current DynamicCollection.  Note that if the se
     *
     * @return  the search object for the current collection.
     */
    public ContentSearch getSearchCriteria() {
        return searchCriteria;
    }

    /**
     * Defines and executes the search for {@link cscie97.asn4.ecommerce.product.Content} items.
     *
     * @param searchCriteria  the {@link cscie97.asn4.ecommerce.product.ContentSearch} to run when finding
     *                        {@link cscie97.asn4.ecommerce.product.Content} for the DynamicCollection
     */
    public void setSearchCriteria(ContentSearch searchCriteria) {
        this.searchCriteria = searchCriteria;
        // in case the collection does not yet have any children, try to execute the search and find some content
        executeSearch();
    }

    /**
     * Executes the {@link cscie97.asn4.ecommerce.product.ContentSearch} for the DynamicCollection against the
     * {@link cscie97.asn4.ecommerce.product.IProductAPI}.  The found {@link cscie97.asn4.ecommerce.product.Content}
     * items will be wrapped as {@link cscie97.asn4.ecommerce.collection.ContentProxy} objects, so that all items in
     * a collection share the same properties and may likewise be iterated over simply, despite having different
     * attributes and features.
     */
    public void executeSearch() {
        if (this.searchCriteria != null) {
            for (Content content : ProductAPI.getInstance().searchContent(this.searchCriteria)) {
                ContentProxy cp = new ContentProxy(content);
                this.add(cp);
            }
        }
    }

    /**
     * Convenience method to print out the salient properties of the object (useful in debugging).
     *
     * @return  a string representation of the salient properties of the object
     */
    @Override
    public String toString() {
        return String.format("Type: [%s]\nID: [%s]\nName: [%s]\nDescription: [%s]\n",
                this.getClass().getSimpleName(),
                this.getId(),
                this.getName(),
                this.getDescription());
    }

}
