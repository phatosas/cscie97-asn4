package cscie97.asn4.ecommerce.authentication;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import java.util.Set;


/**
 * Abstract class representing an authentication item (could be a {@link cscie97.asn4.ecommerce.authentication.Role},
 * {@link cscie97.asn4.ecommerce.authentication.Permission}, or {@link cscie97.asn4.ecommerce.authentication.Service})
 * that is included in the Mobile Application Store's Authentication catalog.  Attributes here are common to all items
 * in the Authentication catalog, regardless of type.  All authentication items (regardless of type) have the
 * following attributes:
 * <ul>
 *     <li>must have an <b>ID</b> that is a unique GUID</li>
 *     <li>must have a <b>name</b></li>
 *     <li>must have a <b>description</b></li>
 * </ul>
 *
 * Certain specific types of Authentication items may have additional required attributes.  Each
 * {@link cscie97.asn4.ecommerce.authentication.Item} that lives in the Authentication catalog is a unique item.
 * Authentication items may be added to the authentication catalog and made returnable by calling
 * {@link IAuthenticationServiceAPI#getInventory()}.
 *
 * @author David Killeffer &lt;rayden7@gmail.com&gt;
 * @version 1.0
 * @see cscie97.asn4.ecommerce.authentication.Role
 * @see cscie97.asn4.ecommerce.authentication.Service
 * @see cscie97.asn4.ecommerce.authentication.Permission
 * @see cscie97.asn4.ecommerce.authentication.Entitlement
 * @see cscie97.asn4.ecommerce.authentication.IAuthenticationServiceAPI
 */
public abstract class Item {

    /**
     * A unique string identifier for each authentication item; should be a GUID
     */
    private String id;

    /**
     * Name of the authentication item
     */
    private String name;

    /**
     * A brief description of what this authentication item is and it's features.
     */
    private String description;

    /**
     * Returns the unique authentication ID of the item.
     *
     * @return  the unique authentication ID
     */
    public String getID() {
        return id;
    }

    /**
     * Sets the unique ID for this authentication item.
     *
     * @param id  the unique ID to use for the authentication item
     */
    protected void setID(String id) {
        this.id = id;
    }

    /**
     * Returns the name of the authentication item.
     *
     * @return  the authentication item name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name for this authentication item.
     *
     * @param name  the name to use for the authentication item
     */
    protected void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the authentication item description.
     *
     * @return  authentication item description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the authentication item description
     *
     * @param description  the new authentication item description to use
     */
    protected void setDescription(String description) {
        this.description = description;
    }

    /**
     * Class constructor.
     *
     * @param id                  the unique authentication item ID
     * @param name                the authentication item name
     * @param description         authentication item description
     */
    /*
    public Item(String id, String name, String description)
    {
        this.id = id;
        this.name = name;
        this.description = description;
    }
    */

    /**
     * Public static method that checks that all required fields are set, and that all authentication item values are
     * valid (e.g., ratings are 0 to 5, where 5 is best, item price should only allow positive numbers, etc.).
     *
     * @param content  the item to be validated for correct properties
     * @return  true if all properties are valid, false otherwise
     */
    public static boolean validateItem(Item content) {
        return (
                   (content.getID() != null && content.getID().length() > 0) &&
                   (content.getName() != null && content.getName().length() > 0) &&
                   (content.getDescription() != null && content.getDescription().length() > 0)
        );
    }

    /**
     * Returns a string representation of the authentication item; useful for debugging.
     *
     * @return  string representation of the authenticaion item
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("AUTHENTICATION ITEM:\n") );
        sb.append(String.format("\tID: [%s]\n", this.getID()));
        sb.append(String.format("\tNAME: [%s]\n", this.getName()));
        sb.append(String.format("\tDESCRIPTION: [%s]\n", this.getDescription()));
        return sb.toString();
    }

    /**
     * Since {@link cscie97.asn4.ecommerce.authentication.Item} objects may be added to collections, and also since
     * the {@link cscie97.asn4.ecommerce.authentication.IAuthenticationServiceAPI} enforces that all authentication
     * items be unique, this method provides a way to determine if another
     * {@link cscie97.asn4.ecommerce.authentication.Item} item is the same as the current one.  Uses the Apache
     * Commons {@link org.apache.commons.lang3.builder.EqualsBuilder} to determine if the two objects are indeed equal
     * to each other.
     *
     * @param compare  the {@link cscie97.asn4.ecommerce.authentication.Item} to compare to the current object to test for equality
     * @return  true if the objects are the same, false otherwise
     * @see <a href="http://stackoverflow.com/questions/27581/overriding-equals-and-hashcode-in-java">http://stackoverflow.com/questions/27581/overriding-equals-and-hashcode-in-java</a>
     * @see <a href="http://www.java-tutorial.ch/core-java-tutorial/equalsbuilder">http://www.java-tutorial.ch/core-java-tutorial/equalsbuilder</a>
     * @see <a href="http://commons.apache.org/proper/commons-lang/javadocs/api-2.6/org/apache/commons/lang/builder/EqualsBuilder.html">http://commons.apache.org/proper/commons-lang/javadocs/api-2.6/org/apache/commons/lang/builder/EqualsBuilder.html</a>
     */
    @Override
    public boolean equals(Object compare){
        if (compare == null)
            return false;
        if (!(compare instanceof Item))
            return false;
        if (compare == this)
            return true;

        Item rhs = (Item) compare;
        return new EqualsBuilder()
                .appendSuper(super.equals(compare))
                .append(this.id, rhs.getID())
                .append(this.name, rhs.getName())
                .append(this.description, rhs.getDescription())
                .isEquals();
    }

    /**
     * Since {@link cscie97.asn4.ecommerce.authentication.Item} objects may be added to collections, and also since
     * the {@link cscie97.asn4.ecommerce.authentication.IAuthenticationServiceAPI} enforces that all authentication
     * items be unique, this method provides a way to get the unique hash code for the current authentication item.
     * Uses the Apache Commons {@link org.apache.commons.lang3.builder.HashCodeBuilder} to generate a unique hash code
     * for the current item based on two randomly chosen unique prime numbers and all the object properties.
     *
     * @return  a unique integer hash code for this particular object
     * @see <a href="http://stackoverflow.com/questions/27581/overriding-equals-and-hashcode-in-java">http://stackoverflow.com/questions/27581/overriding-equals-and-hashcode-in-java</a>
     * @see <a href="http://commons.apache.org/proper/commons-lang/javadocs/api-2.6/org/apache/commons/lang/builder/HashCodeBuilder.html">http://commons.apache.org/proper/commons-lang/javadocs/api-2.6/org/apache/commons/lang/builder/HashCodeBuilder.html</a>
     * @see <a href="http://primes.utm.edu/lists/small/1000.txt">http://primes.utm.edu/lists/small/1000.txt</a>
     */
    @Override
    public int hashCode() {
        return new HashCodeBuilder(6581, 4507)
                .append(this.id)
                .append(this.name)
                .append(this.description)
                .toHashCode();
    }

}
