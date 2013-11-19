package cscie97.asn4.ecommerce.authentication;

import cscie97.asn4.ecommerce.collection.CollectionIterator;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Roles represent aggregations of {@link cscie97.asn4.ecommerce.authentication.Permission}s and other
 * {@link cscie97.asn4.ecommerce.authentication.Role}s.  A Role defines "who a user is"; for example, a role called
 * "authentication_admin_role" would contain all the {@link cscie97.asn4.ecommerce.authentication.Permission}s that
 * collectively define all the restricted actions that only an AuthenticationServiceAPI administrator could perform.
 *
 * @author David Killeffer &lt;rayden7@gmail.com&gt;
 * @version 1.0
 * @see cscie97.asn4.ecommerce.authentication.Entitlement
 * @see cscie97.asn4.ecommerce.authentication.Role
 * @see cscie97.asn4.ecommerce.authentication.Permission
 * @see cscie97.asn4.ecommerce.authentication.User
 */
public class Role extends Entitlement implements IAuthenticationVisitable {

    /**
     * Defines the iterator for the current role
     */
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
        this.iterator = new RoleIterator(this);
        return this.iterator;
    }

    /**
     * Defines the child elements of the Role, which may be other Roles or Permissions.
     */
    private List<Entitlement> children = new ArrayList<Entitlement>();

    /**
     * Class constructor.
     *
     * @param id           the id of the role
     * @param name         the name of the role
     * @param description  a brief description of the role
     */
    public Role(String id, String name, String description) {
        super(id, name, description);
    }

    /**
     * No-argument class constructor.
     */
    public Role() { }

    /**
     * Gets the children Entitlements of the Role
     *
     * @return  the children Entitlements of the Role
     */
    public List<Entitlement> getChildren() {
        return children;
    }

    /**
     * Sets the current children Entitlements of the Role
     *
     * @param children  the child Entitlements of the Role
     */
    public void setChildren(List<Entitlement> children) {
        this.children = children;
    }

    /**
     * Adds a child Entitlement to the Role
     *
     * @param entitlement  the children Entitlement to add to the Role
     */
    public void addChild(Entitlement entitlement) {
        this.children.add(entitlement);
    }

    /**
     * Adds multiple children Entitlements to the Role
     *
     * @param entitlements  the set of children Entitlements to add to the Role
     */
    public void addChildren(Set<Entitlement> entitlements) {
        this.children.addAll(entitlements);
    }

    /**
     * Accepts a visitor object for the purposes of building up an inventory of items in the AuthenticationService.
     *
     * @param visitor  the visiting object used to build up the inventory
     * @return  the string representation of the current object for inclusion in a printable inventory
     */
    public String acceptVisitor(IAuthenticationVisitor visitor) {
        return visitor.visit(this);
    }

    /**
     * Since {@link cscie97.asn4.ecommerce.authentication.Entitlement} objects may be added to collections, and also
     * since the {@link cscie97.asn4.ecommerce.authentication.IAuthenticationServiceAPI} enforces that all entitlement
     * items be unique, this method provides a way to determine if another
     * {@link cscie97.asn4.ecommerce.authentication.Entitlement} item is the same as the current one based on shared
     * properties.  Uses the Apache Commons {@link org.apache.commons.lang3.builder.EqualsBuilder} to determine if
     * the two objects are indeed equal to each other.
     *
     * @param compare  the item to compare to the current object to test for equality
     * @return  true if the objects are the same, false otherwise
     * @see <a href="http://stackoverflow.com/questions/27581/overriding-equals-and-hashcode-in-java">http://stackoverflow.com/questions/27581/overriding-equals-and-hashcode-in-java</a>
     * @see <a href="http://www.java-tutorial.ch/core-java-tutorial/equalsbuilder">http://www.java-tutorial.ch/core-java-tutorial/equalsbuilder</a>
     * @see <a href="http://commons.apache.org/proper/commons-lang/javadocs/api-2.6/org/apache/commons/lang/builder/EqualsBuilder.html">http://commons.apache.org/proper/commons-lang/javadocs/api-2.6/org/apache/commons/lang/builder/EqualsBuilder.html</a>
     */
    @Override
    public boolean equals(Object compare){
        if (compare == null)
            return false;
        if (!(compare instanceof Role))
            return false;
        if (compare == this)
            return true;

        Role rhs = (Role) compare;
        return new EqualsBuilder()
                    .append(this.getID(), rhs.getID())
                    .append(this.getName(), rhs.getName())
                    .append(this.getDescription(), rhs.getDescription())
                    .isEquals();
    }

    /**
     * Since {@link cscie97.asn4.ecommerce.product.Content} objects may be added to collections, and also since
     * the {@link cscie97.asn4.ecommerce.product.IProductAPI} enforces that all content items be unique, this method
     * provides a way to get the unique hash code for the current content item.  Uses the Apache Commons
     * {@link org.apache.commons.lang3.builder.HashCodeBuilder} to generate a unique hash code for the current item
     * based on two randomly chosen unique prime numbers and all the object properties.
     *
     * @return  a unique integer hash code for this particular object
     * @see <a href="http://stackoverflow.com/questions/27581/overriding-equals-and-hashcode-in-java">http://stackoverflow.com/questions/27581/overriding-equals-and-hashcode-in-java</a>
     * @see <a href="http://commons.apache.org/proper/commons-lang/javadocs/api-2.6/org/apache/commons/lang/builder/HashCodeBuilder.html">http://commons.apache.org/proper/commons-lang/javadocs/api-2.6/org/apache/commons/lang/builder/HashCodeBuilder.html</a>
     * @see <a href="http://primes.utm.edu/lists/small/1000.txt">http://primes.utm.edu/lists/small/1000.txt</a>
     */
    @Override
    public int hashCode() {
        return new HashCodeBuilder(1787, 3299)
                    .append(this.getID())
                    .append(this.getName())
                    .append(this.getDescription())
                    .toHashCode();
    }
}