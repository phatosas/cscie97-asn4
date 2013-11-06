package cscie97.asn4.ecommerce.collection;

import java.util.List;
import java.util.ArrayList;

/**
 * Abstract parent class for {@link cscie97.asn4.ecommerce.collection.Collection} and
 * {@link cscie97.asn4.ecommerce.collection.ContentProxy} objects.  Collectibles can contain child Collectibles,
 * which makes them a tree structure.  Because arbitrary numbers of child Collectibles can be added, the class
 * supports a {@link cscie97.asn4.ecommerce.collection.CollectionIterator} for iterating over all child elements.
 *
 * The object types that may be created that are children of the Collectible class are:
 * <ul>
 *     <li>{@link cscie97.asn4.ecommerce.collection.StaticCollection}</li>
 *     <li>{@link cscie97.asn4.ecommerce.collection.DynamicCollection}</li>
 *     <li>{@link cscie97.asn4.ecommerce.collection.ContentProxy}</li>
 * </ul>
 * All of these types will inherit their shared attributes ({@link cscie97.asn4.ecommerce.collection.Collectible#id},
 * {@link cscie97.asn4.ecommerce.collection.Collectible#name},
 * {@link cscie97.asn4.ecommerce.collection.Collectible#description}) from this class.
 *
 *
 * @author David Killeffer &lt;rayden7@gmail.com&gt;
 * @version 1.0
 * @see cscie97.asn4.ecommerce.collection.Collection
 * @see cscie97.asn4.ecommerce.collection.Collectible
 * @see cscie97.asn4.ecommerce.collection.DynamicCollection
 * @see cscie97.asn4.ecommerce.collection.StaticCollection
 * @see cscie97.asn4.ecommerce.collection.ContentProxy
 */
public abstract class Collectible {

    /**
     * Unique identifier for the Collectible
     */
    private String id;

    /**
     * Name of the Collectible
     */
    private String name;

    /**
     * Textual description of what this Collectible is
     */
    private String description;

    /**
     * All children Collectibles of the current Collectible; this makes the Collectible a tree structure
     */
    private List<Collectible> children = new ArrayList<Collectible>();

    /**
     * Used for iterating over this Collectible and all children elements.
     */
    private CollectionIterator iterator = null;

    /**
     * Returns the iterator for the current collection.  The iterator also follows the Singleton pattern; once the
     * iterator has been declared and initialized, the already-declared one will be returned.  If the iterator has
     * not been defined when getIterator() is called, a new
     * {@link cscie97.asn4.ecommerce.collection.CollectionIterator} will be created and initialized.
     *
     * @return  the iterator for the Collectible
     */
    public CollectionIterator getIterator() {
        if (iterator == null) {
            this.iterator = new CollectionIterator(this);
        }
        return this.iterator;
    }

    /**
     * Returns the current set of children {@link cscie97.asn4.ecommerce.collection.Collectible} objects that are
     * contained by this Collectible.  Note that while the children of the current Collectibles are all themselves
     * {@link cscie97.asn4.ecommerce.collection.Collectible} objects, their actual types may be one of:
     * <ul>
     *     <li>{@link cscie97.asn4.ecommerce.collection.ContentProxy} (used to wrap the
     *          {@link cscie97.asn4.ecommerce.product.Content} objects that get returned by the
     *          {@link cscie97.asn4.ecommerce.product.IProductAPI}</li>
     *     <li>{@link cscie97.asn4.ecommerce.collection.StaticCollection}</li>
     *     <li>{@link cscie97.asn4.ecommerce.collection.DynamicCollection}</li>
     * </ul>
     * Note that if the current Collectible is actually a {@link cscie97.asn4.ecommerce.collection.DynamicCollection},
     * this method will still return the found {@link cscie97.asn4.ecommerce.collection.ContentProxy} objects that
     * are found by executing the search (no need to separately execute the search).
     *
     * @return  the list of child {@link cscie97.asn4.ecommerce.collection.ContentProxy} objects contained in
     *          the current Collectible
     */
    public List<Collectible> getChildren() {
        return this.children;
    }

    /**
     * Adds a child Collectible to the current Collectible.  Since the children of the current Collectible will
     * be modified as a result, a side effect of adding a new Collectible is to null out the current iterator (so
     * that the next time a client wishes to iterate over the Collectible the new item will be included).
     *
     * @param collectible  the new Collectible to be added to the Collectible
     */
    public void add(Collectible collectible) {
        this.iterator = null;  // since we're modifying the collection, ensure that the next time the iterator is referenced it is re-created
        children.add(collectible);
    }

    /**
     * Gets the unique identifier for the current collectible.
     *
     * @return  the unique string ID for the current collectible
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the unique identifier for the current collectible.
     *
     * @param  id  the unique identifier to set for the collectible
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Gets the collectible name.
     *
     * @return  the name of the current collectible
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name for the current collectible.
     *
     * @param  name  the name to set for the collectible
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the collectible description.
     *
     * @return  the description of the current collectible
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the textual description for the current collectible.
     *
     * @param  description  the textual description for the collectible
     */
    public void setDescription(String description) {
        this.description = description;
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
