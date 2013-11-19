package cscie97.asn4.ecommerce.authentication;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: dkilleffer
 * Date: 11/13/13
 * Time: 10:55 AM
 * To change this template use File | Settings | File Templates.
 */
public class Service extends Item implements IAuthenticationVisitable {

    private Set<Permission> permissions = new HashSet<Permission>();

    /**
     * Class constructor.
     *
     * @param id                  the unique authentication Service ID
     * @param name                the authentication service name
     * @param description         authentication service description
     */
    public Service(String id, String name, String description)
    {
        this.setID(id);
        this.setName(name);
        this.setDescription(description);
    }

    public Service() { }

    public Set<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    public void addPermission(Permission permission) {
        this.permissions.add(permission);
    }

    public void addPermissions(Set<Permission> permissions) {
        this.permissions.addAll(permissions);
    }

    /**
     * Accepts a visitor object for the purposes of building up an inventory of items in the AuthenticationService.
     *
     * @param visitor  the visiting object used to build up the inventory
     * @return  the string representation of the current object for inclusion in a printable inventory
     */
    public String acceptVisitor(IAuthenticationVisitor visitor) {
        return visitor.visitService(this);
    }


    /**
     * Since {@link cscie97.asn4.ecommerce.authentication.Service} objects may be added to collections, and also
     * since the {@link cscie97.asn4.ecommerce.authentication.IAuthenticationServiceAPI} enforces that all entitlement
     * items be unique, this method provides a way to determine if another
     * {@link cscie97.asn4.ecommerce.authentication.Service} item is the same as the current one based on shared
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
        if (!(compare instanceof Service))
            return false;
        if (compare == this)
            return true;

        Service rhs = (Service) compare;
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
        return new HashCodeBuilder(6247, 6011)
                    .append(this.getID())
                    .append(this.getName())
                    .append(this.getDescription())
                    .toHashCode();
    }

}
