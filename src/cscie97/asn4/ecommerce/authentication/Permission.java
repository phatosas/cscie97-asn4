package cscie97.asn4.ecommerce.authentication;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Permissions represent actions that registered Users may carry out on the
 * {@link cscie97.asn4.ecommerce.authentication.IAuthenticationServiceAPI},
 * {@link cscie97.asn4.ecommerce.product.IProductAPI}, and
 * {@link cscie97.asn4.ecommerce.collection.ICollectionServiceAPI}.  Permissions <b>must</b> be added to
 * {@link cscie97.asn4.ecommerce.authentication.Service}s, and may be added to
 * {@link cscie97.asn4.ecommerce.authentication.Role}s.  A {@link cscie97.asn4.ecommerce.authentication.User} may be
 * granted a Permission either directly or through a {@link cscie97.asn4.ecommerce.authentication.Role}.
 *
 * @author David Killeffer &lt;rayden7@gmail.com&gt;
 * @version 1.0
 * @see cscie97.asn4.ecommerce.authentication.Item
 * @see cscie97.asn4.ecommerce.authentication.Entitlement
 * @see cscie97.asn4.ecommerce.authentication.Role
 * @see cscie97.asn4.ecommerce.authentication.User
 * @see cscie97.asn4.ecommerce.authentication.PermissionType
 */
public class Permission extends Entitlement implements IAuthenticationVisitable {

    /**
     * No-argument class constructor.
     */
    public Permission() { }

    /**
     * Class constructor.
     *
     * @param id           the id of the Permission
     * @param name         the name of the Permission
     * @param description  a brief description of the Permission
     */
    public Permission(String id, String name, String description) {
        super(id, name, description);
    }

    /**
     * Class constructor.
     *
     * @param permissionType  the PermissionType for the Permission
     * @param name            the name of the Permission
     * @param description     a brief description of the Permission
     */
    public Permission(PermissionType permissionType, String name, String description) {
        super(permissionType.getPermissionName(), name, description);
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
        if (!(compare instanceof Permission))
            return false;
        if (compare == this)
            return true;

        Permission rhs = (Permission) compare;
        return new EqualsBuilder()
                    .append(this.getID(), rhs.getID())
                    .append(this.getName(), rhs.getName())
                    .append(this.getDescription(), rhs.getDescription())
                    .isEquals();
    }

    /**
     * Since {@link cscie97.asn4.ecommerce.authentication.Item} objects may be added to collections, and also since
     * the {@link cscie97.asn4.ecommerce.authentication.IAuthenticationServiceAPI} enforces that all authentication
     * items be unique, this method provides a way to get the unique hash code for the current item.  Uses the Apache
     * Commons {@link org.apache.commons.lang3.builder.HashCodeBuilder} to generate a unique hash code for the current
     * item based on two randomly chosen unique prime numbers and all the object properties.
     *
     * @return  a unique integer hash code for this particular object
     * @see <a href="http://stackoverflow.com/questions/27581/overriding-equals-and-hashcode-in-java">http://stackoverflow.com/questions/27581/overriding-equals-and-hashcode-in-java</a>
     * @see <a href="http://commons.apache.org/proper/commons-lang/javadocs/api-2.6/org/apache/commons/lang/builder/HashCodeBuilder.html">http://commons.apache.org/proper/commons-lang/javadocs/api-2.6/org/apache/commons/lang/builder/HashCodeBuilder.html</a>
     * @see <a href="http://primes.utm.edu/lists/small/1000.txt">http://primes.utm.edu/lists/small/1000.txt</a>
     */
    @Override
    public int hashCode() {
        return new HashCodeBuilder(653, 73)
                    .append(this.getID())
                    .append(this.getName())
                    .append(this.getDescription())
                    .toHashCode();
    }

}