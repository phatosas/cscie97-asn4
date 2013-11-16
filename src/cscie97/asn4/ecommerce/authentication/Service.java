package cscie97.asn4.ecommerce.authentication;

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
public class Service extends Item {

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




    public String acceptVisitor(AuthenticationVisitor visitor) {
        return String.format("ID: %s, NAME: %s, DESCRIPTION: %s", this.getID(), this.getName(), this.getDescription());
    }

}
