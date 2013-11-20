package cscie97.asn4.ecommerce.authentication;

/**
 * Interface for defining the visit methods that the implementing class must overwrite; follows the Visitor pattern.
 *
 * @author David Killeffer &lt;rayden7@gmail.com&gt;
 * @version 1.0
 * @see cscie97.asn4.ecommerce.authentication.Service
 * @see cscie97.asn4.ecommerce.authentication.Entitlement
 * @see cscie97.asn4.ecommerce.authentication.Role
 * @see cscie97.asn4.ecommerce.authentication.Permission
 * @see cscie97.asn4.ecommerce.authentication.User
 */
public interface IAuthenticationVisitor {

    /**
     * Visits an {@link cscie97.asn4.ecommerce.authentication.Entitlement} object and returns a string representing
     * its properties.  For {@link cscie97.asn4.ecommerce.authentication.Role} objects, will use the
     * {@link cscie97.asn4.ecommerce.authentication.RoleIterator} to iterate over all the child
     * {@link cscie97.asn4.ecommerce.authentication.Role}s and
     * {@link cscie97.asn4.ecommerce.authentication.Permission}s and include them in the returned string.
     *
     * @param entitlement  either a Role or Permission to get the properties for
     * @return  a string containing the salient properties of the Entitlement
     */
    public String visitEntitlement(Entitlement entitlement);

    /**
     * Visits a {@link cscie97.asn4.ecommerce.authentication.Permission} object and returns a string representing
     * its properties.
     *
     * @param permission  a Permission to get the properties for
     * @return  a string containing the salient properties of the Permission
     */
    public String visitPermission(Permission permission);

    /**
     * Visits an {@link cscie97.asn4.ecommerce.authentication.Role} object and returns a string representing
     * its properties.  Will use the {@link cscie97.asn4.ecommerce.authentication.RoleIterator} to iterate over all
     * the child {@link cscie97.asn4.ecommerce.authentication.Role}s and
     * {@link cscie97.asn4.ecommerce.authentication.Permission}s and include them in the returned string.
     *
     * @param role  a Role to get the properties for
     * @return  a string containing the salient properties of the Role
     */
    public String visitRole(Role role);

    /**
     * Visits a {@link cscie97.asn4.ecommerce.authentication.Service} and prints out the salient properties of
     * the object.
     *
     * @param service  the Service to get the properties for
     * @return  a string containing the salient properties of the Service
     */
    public String visitService(Service service);

    /**
     * Visits a {@link cscie97.asn4.ecommerce.authentication.User} and prints out the salient properties of
     * the object.
     *
     * @param user  the User to get the properties for
     * @return  a string containing the salient properties of the User
     */
    public String visitUser(User user);

    /**
     * Calls the appropriate visit* method based on the object type passed.
     *
     * @param item  an item that is visitable from the Authentication Service, such as User, Service, Role, or Permission
     * @return  a string representing the salient properties of the object
     */
    public String visit(Object item);

}