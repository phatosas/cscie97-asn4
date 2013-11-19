package cscie97.asn4.ecommerce.authentication;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Stack;

/**
 * Allows for the iteration of {@link cscie97.asn4.ecommerce.authentication.Role} items, which may contain child
 * Roles or Permissions.  This iterator will traverse the Collectible depth-first.
 *
 * @author David Killeffer &lt;rayden7@gmail.com&gt;
 * @version 1.0
 * @see cscie97.asn4.ecommerce.authentication.Permission
 * @see cscie97.asn4.ecommerce.authentication.Role
 */
public class RoleIterator implements Iterator {

    /**
     * Private stack is used to "flatten" the tree structure of the Role and return items in depth-first order.
     */
    private Stack<Entitlement> itemStack = new Stack<Entitlement>();

    /**
     * Keeps a reference to the current item that the hidden internal iterator "pointer" is positioned over.
     */
    private Entitlement current = null;

    /**
     * Class constructor.  Takes a reference to the "top" level of the
     * {@link cscie97.asn4.ecommerce.authentication.Role} to be iterated over.
     *
     * @param top  the Role to iterate over
     */
    public RoleIterator(Role top) {
        this.itemStack.push(top);
        buildItemStack(top);
    }

    /**
     * Convenience method to get the current item without moving the internal pointer of the iterator (calling
     * {@link RoleIterator#next()} will move the hidden internal pointer, but this method does not have that
     * side-effect).
     *
     * @return  the current item the iterator is positioned at; will return null if {@link RoleIterator#next()}
     *          has never been called
     */
    public Entitlement getCurrent() {
        return current;
    }

    /**
     * Traverse the Role and return the next item.
     *
     * @return  the next Entitlement in the Role
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