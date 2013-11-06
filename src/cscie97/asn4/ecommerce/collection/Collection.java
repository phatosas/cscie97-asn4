package cscie97.asn4.ecommerce.collection;

/**
 * Abstract class for Collections.  Currently supports two abstract methods for the creation of either
 * {@link cscie97.asn4.ecommerce.collection.StaticCollection} or
 * {@link cscie97.asn4.ecommerce.collection.DynamicCollection}, and also validating that the required fields for an
 * existing Collection are valid.
 *
 * @author David Killeffer &lt;rayden7@gmail.com&gt;
 * @version 1.0
 * @see cscie97.asn4.ecommerce.collection.Collection
 * @see cscie97.asn4.ecommerce.collection.Collectible
 * @see cscie97.asn4.ecommerce.collection.DynamicCollection
 * @see cscie97.asn4.ecommerce.collection.StaticCollection
 */
 public abstract class Collection extends Collectible {

    /**
     * Factory method to create an empty "shell" collection, with no properties.  Collections can currently only be
     * either {@link cscie97.asn4.ecommerce.collection.StaticCollection} or
     * {@link cscie97.asn4.ecommerce.collection.DynamicCollection}.
     *
     * @param type  which type of collection to create; can be either "static" or "dynamic"
     * @return      the newly created collection, which will be either
     *              {@link cscie97.asn4.ecommerce.collection.StaticCollection},
     *              {@link cscie97.asn4.ecommerce.collection.DynamicCollection}, or null (if an invalid type was passed)
     */
    public static Collection createCollection(String type) {
        if (type != null && type.length() > 0) {
            if (type.equalsIgnoreCase("static")) {
                return new StaticCollection();
            } else if (type.equalsIgnoreCase("dynamic")) {
                return new DynamicCollection();
            }
        }
        return null;
    }

    /**
     * Performs a validation check to ensure that the collection contains a minimal set of required attribute
     * properties.  Currently only requires that id, name, and description are set.
     *
     * @param collection  the collection to validate
     * @return   true if the collection has the minimal set of required properties, false otherwise
     */
    public static boolean validateCollection(Collection collection) {
        return (collection != null &&
                collection.getId() != null && collection.getId().length() > 0 &&
                collection.getName() != null && collection.getName().length() > 0 &&
                collection.getDescription() != null && collection.getDescription().length() > 0
        );
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
