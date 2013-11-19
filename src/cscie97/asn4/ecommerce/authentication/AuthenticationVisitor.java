package cscie97.asn4.ecommerce.authentication;

/**
 * Used to aid in building up a printable inventory of the
 * {@link cscie97.asn4.ecommerce.authentication.IAuthenticationServiceAPI} to list out all the
 * {@link cscie97.asn4.ecommerce.authentication.Service}, {@link cscie97.asn4.ecommerce.authentication.Entitlement}
 * (which are subclassed as {@link cscie97.asn4.ecommerce.authentication.Role}s and
 * {@link cscie97.asn4.ecommerce.authentication.Permission}s), and {@link cscie97.asn4.ecommerce.authentication.User}.
 * This class is a primary actor in the Visitor pattern usage for building up a printable inventory of Authentication
 * items.
 *
 * @author David Killeffer &lt;rayden7@gmail.com&gt;
 * @version 1.0
 * @see cscie97.asn4.ecommerce.authentication.Service
 * @see cscie97.asn4.ecommerce.authentication.Entitlement
 * @see cscie97.asn4.ecommerce.authentication.Role
 * @see cscie97.asn4.ecommerce.authentication.Permission
 * @see cscie97.asn4.ecommerce.authentication.User
 */
public class AuthenticationVisitor implements IAuthenticationVisitor {

    /**
     * Helper method to retrieve the printable properties of all classes that inherit from
     * {@link cscie97.asn4.ecommerce.authentication.Item}.
     *
     * @param item  an object that inherits from Item so has shared properties
     * @return  a string representation of the shared Item properties
     */
    private String getItemProperties(Item item) {
        String id = item.getID();
        String name = item.getName();
        return String.format("ID: [%s] Name: [%s]", id, name);
    }

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
    public String visitEntitlement(Entitlement entitlement) {
        String type = null;
        StringBuilder extra = new StringBuilder();
        if (entitlement instanceof Role) {
            type = "Role";
            RoleIterator iterator = ((Role)entitlement).getIterator();
            if (iterator.hasNext()) {
                extra.append(String.format("\n\t\tChildren:"));
            }
            while ( iterator.hasNext() ) {
                Entitlement entitlementChild = iterator.next();
                String childType = null;
                if (entitlementChild instanceof Role) {
                    childType = "Role";
                } else if (entitlementChild instanceof Permission) {
                    childType = "Permission";
                }
                extra.append(String.format("\n\t\t\t[%s] ID: [%s] Name: [%s]", childType, entitlementChild.getID(), entitlementChild.getName()));
            }
        } else if (entitlement instanceof Permission) {
            type = "Permission";
        }
        return String.format("\t[%s]: %s %s\n", type, getItemProperties(entitlement), extra.toString());
    }

    /**
     * Visits a {@link cscie97.asn4.ecommerce.authentication.Permission} object and returns a string representing
     * its properties.
     *
     * @param permission  a Permission to get the properties for
     * @return  a string containing the salient properties of the Permission
     */
    public String visitPermission(Permission permission) {
        String type = "Permission";
        StringBuilder extra = new StringBuilder();
        return String.format("\t[%s]: %s %s\n", type, getItemProperties(permission), extra.toString());
    }

    /**
     * Visits an {@link cscie97.asn4.ecommerce.authentication.Role} object and returns a string representing
     * its properties.  Will use the {@link cscie97.asn4.ecommerce.authentication.RoleIterator} to iterate over all
     * the child {@link cscie97.asn4.ecommerce.authentication.Role}s and
     * {@link cscie97.asn4.ecommerce.authentication.Permission}s and include them in the returned string.
     *
     * @param role  a Role to get the properties for
     * @return  a string containing the salient properties of the Role
     */
    public String visitRole(Role role) {
        String type = "Role";
        StringBuilder extra = new StringBuilder();
        RoleIterator iterator = role.getIterator();
        if (iterator.hasNext()) {
            extra.append(String.format("\n\t\tChildren:"));
        }
        while ( iterator.hasNext() ) {
            Entitlement entitlementChild = iterator.next();
            String childType = null;
            if (entitlementChild instanceof Role) {
                childType = "Role";
            } else if (entitlementChild instanceof Permission) {
                childType = "Permission";
            }
            extra.append(String.format("\n\t\t\t[%s] ID: [%s] Name: [%s]", childType, entitlementChild.getID(), entitlementChild.getName()));
        }
        return String.format("\t[%s]: %s %s\n", type, getItemProperties(role), extra.toString());
    }

    /**
     * Visits a {@link cscie97.asn4.ecommerce.authentication.Service} and prints out the salient properties of
     * the object.
     *
     * @param service  the Service to get the properties for
     * @return  a string containing the salient properties of the Service
     */
    public String visitService(Service service) {
        return String.format("\t[Service]: %s\n", getItemProperties(service));
    }

    /**
     * Visits a {@link cscie97.asn4.ecommerce.authentication.User} and prints out the salient properties of
     * the object.
     *
     * @param user  the User to get the properties for
     * @return  a string containing the salient properties of the User
     */
    public String visitUser(User user) {
        return String.format("\t[User]: %s\n", getItemProperties(user));
    }

    /**
     * Calls the appropriate visit* method based on the object type passed.
     *
     * @param item  an item that is visitable from the Authentication Service, such as User, Service, Role, or Permission
     * @return  a string representing the salient properties of the object
     */
    public String visit(Object item) {
        if (item instanceof Service) {
            return visitService((Service)item);
        }
        else if (item instanceof Role) {
            return visitRole((Role)item);
        }
        else if (item instanceof Permission) {
            return visitPermission((Permission)item);
        }
        else if (item instanceof User) {
            return visitUser((User)item);
        }
        return null;
    }
}