package cscie97.asn4.ecommerce.authentication;


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

}
