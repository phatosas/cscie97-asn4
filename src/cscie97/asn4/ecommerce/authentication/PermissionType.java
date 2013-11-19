package cscie97.asn4.ecommerce.authentication;

/**
 * Enumeration of all Permission types that the {@link cscie97.asn4.ecommerce.authentication.IAuthenticationServiceAPI}
 * uses, as well as the Permission types that are used by the {@link cscie97.asn4.ecommerce.product.IProductAPI} and
 * {@link cscie97.asn4.ecommerce.collection.ICollectionServiceAPI}.
 *
 * @author David Killeffer &lt;rayden7@gmail.com&gt;
 * @version 1.0
 * @see cscie97.asn4.ecommerce.authentication.IAuthenticationServiceAPI
 * @see cscie97.asn4.ecommerce.product.IProductAPI
 * @see cscie97.asn4.ecommerce.collection.ICollectionServiceAPI
 */
public enum PermissionType {

    /** used by the {@link cscie97.asn4.ecommerce.authentication.IAuthenticationServiceAPI} */
    DEFINE_SERVICE ("define_service"),

    /** used by the {@link cscie97.asn4.ecommerce.authentication.IAuthenticationServiceAPI} */
    DEFINE_PERMISSION ("define_permission"),

    /** used by the {@link cscie97.asn4.ecommerce.authentication.IAuthenticationServiceAPI} */
    DEFINE_ROLE ("define_role"),

    /** used by the {@link cscie97.asn4.ecommerce.authentication.IAuthenticationServiceAPI} */
    ADD_ENTITLEMENT_TO_ROLE ("add_entitlement_to_role"),

    /** used by the {@link cscie97.asn4.ecommerce.authentication.IAuthenticationServiceAPI} */
    CREATE_USER ("create_user"),

    /** used by the {@link cscie97.asn4.ecommerce.authentication.IAuthenticationServiceAPI} */
    ADD_CREDENTIAL_TO_USER ("add_credential"),

    /** used by the {@link cscie97.asn4.ecommerce.authentication.IAuthenticationServiceAPI} */
    ADD_ENTITLEMENT_TO_USER ("add_entitlement_to_user"),

    /** used by the {@link cscie97.asn4.ecommerce.collection.ICollectionServiceAPI} */
    CREATE_COLLECTION("create_collection"),

    /** used by the {@link cscie97.asn4.ecommerce.collection.ICollectionServiceAPI} */
    ADD_CONTENT("add_content"),

    /** used by the {@link cscie97.asn4.ecommerce.collection.ICollectionServiceAPI} */
    DEFINE_COLLECTION_SEARCH_CRITERIA("define_collection_dynamic_search"),

    /** used by the {@link cscie97.asn4.ecommerce.product.IProductAPI} */
    CREATE_PRODUCT("create_product"),

    /** used by the {@link cscie97.asn4.ecommerce.product.IProductAPI} */
    CREATE_COUNTRY("create_country"),

    /** used by the {@link cscie97.asn4.ecommerce.product.IProductAPI} */
    CREATE_DEVICE("create_device");

    private final String permissionName;

    PermissionType(String name) {
        this.permissionName = name;
    }

    public String getPermissionName() {
        return this.permissionName;
    }

}