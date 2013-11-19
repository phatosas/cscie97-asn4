package cscie97.asn4.ecommerce.authentication;

/**
 * Interface that marks implementing classes as able to accept an
 * {@link cscie97.asn4.ecommerce.authentication.IAuthenticationVisitor} for building up a printable product inventory.
 *
 * @author David Killeffer &lt;rayden7@gmail.com&gt;
 * @version 1.0
 * @see cscie97.asn4.ecommerce.authentication.Service
 * @see cscie97.asn4.ecommerce.authentication.Entitlement
 * @see cscie97.asn4.ecommerce.authentication.Role
 * @see cscie97.asn4.ecommerce.authentication.Permission
 * @see cscie97.asn4.ecommerce.authentication.User
 * @see cscie97.asn4.ecommerce.authentication.IAuthenticationVisitor
 */
public interface IAuthenticationVisitable {

    /**
     * Accept a visiting class for the purpose of building up a printable product inventory.
     *
     * @param visitor  the visiting class
     * @return  a string representation of the object being visited for inclusion in a printable inventory
     */
    public String acceptVisitor(IAuthenticationVisitor visitor);

}
