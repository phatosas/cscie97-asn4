package cscie97.asn4.ecommerce.authentication;

/**
 * Created with IntelliJ IDEA.
 * User: dkilleffer
 * Date: 11/18/13
 * Time: 10:16 AM
 * To change this template use File | Settings | File Templates.
 */
public enum PermissionType {

    DEFINE_SERVICE ("define_service"),

    DEFINE_PERMISSION ("define_permission"),

    DEFINE_ROLE ("define_role"),

    ADD_ENTITLEMENT_TO_ROLE ("add_entitlement_to_role"),

    CREATE_USER ("create_user"),

    ADD_CREDENTIAL_TO_USER ("add_credential_to_user"),

    ADD_ENTITLEMENT_TO_USER ("add_entitlement_to_user"),

    CREATE_COLLECTION("create_collection"),

    ADD_CONTENT("add_content"),

    DEFINE_COLLECTION_SEARCH_CRITERIA("define_collection_dynamic_search"),

    CREATE_PRODUCT("create_product"),

    CREATE_COUNTRY("create_country"),

    CREATE_DEVICE("create_device");

    private final String permissionName;

    PermissionType(String name) {
        this.permissionName = name;
    }

    public String getPermissionName() {
        return this.permissionName;
    }
}