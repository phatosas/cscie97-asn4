package cscie97.asn4.ecommerce.collection;

/**
 * Marker class for StaticCollections.  StaticCollections are used to add discrete, specific
 * {@link cscie97.asn4.ecommerce.product.Content} items to a collection.  The logic for how to add content items and
 * their properties are all contained in the parent classes {@link cscie97.asn4.ecommerce.collection.Collection} and
 * its parent class, {@link cscie97.asn4.ecommerce.collection.Collectible}.
 *
 * @author David Killeffer &lt;rayden7@gmail.com&gt;
 * @version 1.0
 * @see cscie97.asn4.ecommerce.collection.Collection
 * @see cscie97.asn4.ecommerce.collection.Collectible
 */
public class StaticCollection extends Collection {

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
