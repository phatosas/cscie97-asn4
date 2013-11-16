package cscie97.asn4.ecommerce.authentication;

import cscie97.asn4.ecommerce.collection.CollectionIterator;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: dkilleffer
 * Date: 11/13/13
 * Time: 10:55 AM
 * To change this template use File | Settings | File Templates.
 */
public class Role extends Entitlement {

    private RoleIterator iterator = null;

    /**
     * Returns the iterator for the current Role.  The iterator also follows the Singleton pattern; once the
     * iterator has been declared and initialized, the already-declared one will be returned.  If the iterator has
     * not been defined when getIterator() is called, a new
     * {@link cscie97.asn4.ecommerce.authentication.RoleIterator} will be created and initialized.
     *
     * @return  the iterator for the Role
     */
    public RoleIterator getIterator() {
        /*
        if (iterator == null) {
            this.iterator = new RoleIterator(this);
        }
        return this.iterator;
        */

        this.iterator = new RoleIterator(this);
        return this.iterator;
    }


    private List<Entitlement> children = new ArrayList<Entitlement>();

    public Role(String id, String name, String description) {
        super(id, name, description);
    }

    public Role() {

    }

    public List<Entitlement> getChildren() {
        return children;
    }

    public void setChildren(List<Entitlement> children) {
        this.children = children;
    }

    public void addChild(Entitlement entitlement) {
        this.children.add(entitlement);
    }

    public void addChildren(Set<Entitlement> entitlements) {
        this.children.addAll(entitlements);
    }

}
