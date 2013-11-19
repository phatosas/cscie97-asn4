package cscie97.asn4.ecommerce.authentication;

/**
 * Created with IntelliJ IDEA.
 * User: dkilleffer
 * Date: 11/13/13
 * Time: 10:57 AM
 * To change this template use File | Settings | File Templates.
 */
public class AuthenticationVisitor {

    private String getItemProperties(Item item) {
        String id = item.getID();
        String name = item.getName();
        String description = item.getDescription();
        return String.format("ID: [%s] Name: [%s]", id, name);
    }

    public String visitEntitlement(Entitlement entitlement) {
        String type = null;
        StringBuilder extra = new StringBuilder();
        if (entitlement instanceof Role) {
            type = "Role";
            RoleIterator iterator = ((Role)entitlement).getIterator();
            if (iterator.hasNext()) {
                extra.append(String.format("Children: \n"));
            }

            while ( iterator.hasNext() ) {
                Entitlement entitlementChild = iterator.next();
                String childType = null;
                if (entitlementChild instanceof Role) {
                    childType = "Role";
                } else if (entitlementChild instanceof Permission) {
                    childType = "Permission";
                }
                //extra.append(String.format("\t\t[%s] ID: [%s] Name: [%s] Description: [%s]\n", childType, entitlementChild.getID(), entitlementChild.getName(), entitlementChild.getDescription()));
                extra.append(String.format("\t\t[%s] ID: [%s] Name: [%s]\n", childType, entitlementChild.getID(), entitlementChild.getName()));
            }
        } else if (entitlement instanceof Permission) {
            type = "Permission";
        }
        return String.format("\t[%s]: %s %s\n", type, getItemProperties(entitlement), extra.toString());
    }

    public String visitService(Service service) {
        return String.format("\t[Service]: %s\n", getItemProperties(service));
    }

    public String visitUser(User user) {
        return String.format("\t[User]: %s\n", getItemProperties(user));
    }

}