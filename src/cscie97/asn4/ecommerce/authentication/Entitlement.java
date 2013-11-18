package cscie97.asn4.ecommerce.authentication;


import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Created with IntelliJ IDEA.
 * User: dkilleffer
 * Date: 11/13/13
 * Time: 10:55 AM
 * To change this template use File | Settings | File Templates.
 */
public abstract class Entitlement extends Item {

    /**
     * Class constructor.
     *
     * @param id                  the unique authentication item ID
     * @param name                the authentication item name
     * @param description         authentication item description
     */
    public Entitlement(String id, String name, String description)
    {
        //super(id,name,description);
        this.setID(id);
        this.setName(name);
        this.setDescription(description);

    }

    public Entitlement() {

    }

    public String acceptVisitor(AuthenticationVisitor visitor) {
        return String.format("ID: %s, NAME: %s, DESCRIPTION: %s", this.getID(), this.getName(), this.getDescription());
    }


    /**
     * Since {@link cscie97.asn4.ecommerce.product.Content} objects may be added to collections, and also since
     * the {@link cscie97.asn4.ecommerce.product.IProductAPI} enforces that all content items be unique, this method
     * provides a way to determine if another {@link cscie97.asn4.ecommerce.product.Content} item is the same as the
     * current one.  Uses the Apache Commons {@link org.apache.commons.lang3.builder.EqualsBuilder} to determine if
     * the two objects are indeed equal to each other.
     *
     * @param compare  the {@link cscie97.asn4.ecommerce.product.Content} item to compare to the current object to test for equality
     * @return  true if the objects are the same, false otherwise
     * @see <a href="http://stackoverflow.com/questions/27581/overriding-equals-and-hashcode-in-java">http://stackoverflow.com/questions/27581/overriding-equals-and-hashcode-in-java</a>
     * @see <a href="http://www.java-tutorial.ch/core-java-tutorial/equalsbuilder">http://www.java-tutorial.ch/core-java-tutorial/equalsbuilder</a>
     * @see <a href="http://commons.apache.org/proper/commons-lang/javadocs/api-2.6/org/apache/commons/lang/builder/EqualsBuilder.html">http://commons.apache.org/proper/commons-lang/javadocs/api-2.6/org/apache/commons/lang/builder/EqualsBuilder.html</a>
     */
    @Override
    public boolean equals(Object compare){
        if (compare == null)
            return false;
        if (!(compare instanceof Entitlement))
            return false;
        if (compare == this)
            return true;

        Entitlement rhs = (Entitlement) compare;
        return new EqualsBuilder()
                //.appendSuper(super.equals(compare))
                .append(this.getID(), rhs.getID())
                .append(this.getName(), rhs.getName())
                .append(this.getDescription(), rhs.getDescription())
                        //.append(this.authorName, rhs.getAuthorName())
                        //.append(this.rating, rhs.getRating())
                        //.append(this.categories, rhs.getCategories())
                        //.append(this.compatibleDevices, rhs.getCompatibleDevices())
                        //.append(this.price, rhs.getPrice())
                        //.append(this.allowedInCountries, rhs.getAllowedInCountries())
                        //.append(this.supportedLanguages, rhs.getSupportedLanguages())
                        //.append(this.imageURL, rhs.getImageURL())
                        //.append(this.contentType, rhs.getContentType())
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
        return new HashCodeBuilder(829, 2617)
                .append(this.getID())
                .append(this.getName())
                .append(this.getDescription())
                        //.append(this.rating)
                        //.append(this.categories)
                        //.append(this.compatibleDevices)
                        //.append(this.price)
                        //.append(this.allowedInCountries)
                        //.append(this.supportedLanguages)
                        //.append(this.imageURL)
                        //.append(this.contentType)
                .toHashCode();
    }


}
