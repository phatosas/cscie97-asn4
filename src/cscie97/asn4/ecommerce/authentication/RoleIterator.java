package cscie97.asn4.ecommerce.authentication;

import cscie97.asn4.ecommerce.authentication.Entitlement;
import cscie97.asn4.ecommerce.authentication.Role;
import cscie97.asn4.ecommerce.authentication.Permission;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Stack;


/**
 * Allows for the iteration of {@link cscie97.asn4.ecommerce.authentication.Role} items.
 * {@link cscie97.asn4.ecommerce.collection.Collectible}s follow a two-part Composite design pattern; at the
 * lowest level, Collectibles can be instances of either {@link cscie97.asn4.ecommerce.collection.StaticCollection} or
 * {@link cscie97.asn4.ecommerce.collection.DynamicCollection}, both of which inherit from
 * {@link cscie97.asn4.ecommerce.collection.Collection}.  These {@link cscie97.asn4.ecommerce.collection.StaticCollection}
 * and {@link cscie97.asn4.ecommerce.collection.DynamicCollection} objects may be iterated over using this
 * RoleIterator.  At a higher level, {@link cscie97.asn4.ecommerce.collection.Collectible}s may either be
 * {@link cscie97.asn4.ecommerce.collection.Collection} instances or
 * {@link cscie97.asn4.ecommerce.collection.ContentProxy} instances, and the two types share several common attributes.
 *
 * However, since {@link cscie97.asn4.ecommerce.collection.Collectible} objects may children of
 * {@link cscie97.asn4.ecommerce.collection.Collection}, they may have several children and at several layers of
 * depth, making this iterator necessary to traverse a Collection.  This iterator will traverse the Collectible
 * depth-first.
 *
 * @author David Killeffer &lt;rayden7@gmail.com&gt;
 * @version 1.0
 * @see cscie97.asn4.ecommerce.collection.Collectible
 * @see cscie97.asn4.ecommerce.collection.ContentProxy
 * @see cscie97.asn4.ecommerce.collection.Collection
 * @see cscie97.asn4.ecommerce.collection.DynamicCollection
 * @see cscie97.asn4.ecommerce.collection.StaticCollection
 */
public class RoleIterator implements Iterator {

    /**
     * Private stack is used to "flatten" the tree structure of the Collection and return items in depth-first order.
     */
    private Stack<Entitlement> itemStack = new Stack<Entitlement>();

    /**
     * Keeps a reference to the current item that the hidden internal iterator "pointer" is positioned over.
     */
    private Entitlement current = null;

    /**
     * Class constructor.  Takes a reference to the "top" level of the
     * {@link cscie97.asn4.ecommerce.collection.Collectible} to be iterated over.
     *
     * @param top  the Collectible to iterate over
     */
    public RoleIterator(Role top) {
        this.itemStack.push(top);
        buildItemStack(top);
    }

    /**
     * Convenience method to get the current item without moving the internal pointer of the iterator (calling
     * {@link CollectionIterator#next()} will move the hidden internal pointer, but this method does not have that
     * side-effect).
     *
     * @return  the current item the iterator is positioned at; will return null if {@link CollectionIterator#next()}
     *          has never been called
     */
    public Entitlement getCurrent() {
        return current;
    }

    /**
     * Traverse the Collectible and return the next item.
     *
     * @return  the next Collectible in the Collection
     */
    public Entitlement next() {
        if (!hasNext()) {
            throw new NoSuchElementException("no more entitlements!");
        }
        current = itemStack.pop();
        return current;
    }

    private void buildItemStack(Entitlement item) {
        if (item instanceof Role) {
            for (Entitlement curChild : ((Role)item).getChildren()) {
                itemStack.push(curChild);
                if (curChild instanceof Role) {
                    buildItemStack((Role)curChild);
                }
            }
        } else {
            itemStack.push(item);
        }
    }

    /**
     * Does the iterator have any more elements to iterate over?
     *
     * @return  true if there are more elements to traverse, false otherwise
     */
    public boolean hasNext() {
        return !this.itemStack.empty();
    }

    /**
     * Currently do not support remove, as it was not specified as a requirement in the original requirements document.
     */
    public void remove() {
        throw new UnsupportedOperationException();
    }

}