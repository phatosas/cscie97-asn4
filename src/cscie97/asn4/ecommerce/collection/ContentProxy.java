package cscie97.asn4.ecommerce.collection;

import cscie97.asn4.ecommerce.product.Content;
import cscie97.asn4.ecommerce.product.ProductAPI;

import java.util.List;

/**
 * Wrapper class for {@link cscie97.asn4.ecommerce.product.Content} objects that may be contained in Collections.
 * Since all items that may be part of a {@link cscie97.asn4.ecommerce.collection.Collection} must be able to be
 * easily iterated over, {@link cscie97.asn4.ecommerce.product.Content} items must be wrapped so that they can
 * inherit the same properties and be treated similarly by the
 * {@link cscie97.asn4.ecommerce.collection.CollectionIterator}.
 *
 * @author David Killeffer &lt;rayden7@gmail.com&gt;
 * @version 1.0
 * @see cscie97.asn4.ecommerce.product.Content
 * @see cscie97.asn4.ecommerce.collection.Collection
 * @see cscie97.asn4.ecommerce.collection.Collectible
 * @see cscie97.asn4.ecommerce.collection.DynamicCollection
 * @see cscie97.asn4.ecommerce.collection.StaticCollection
 */
public class ContentProxy extends Collectible {

    /**
     * Wrapped {@link cscie97.asn4.ecommerce.product.Content} item
     */
    private Content contentItem = null;

    /**
     * Gets the wrapped {@link cscie97.asn4.ecommerce.product.Content} item
     *
     * @return  the content item
     */
    public Content getContentItem() {
        return contentItem;
    }

    /**
     * Sets the wrapped {@link cscie97.asn4.ecommerce.product.Content} item
     *
     * @param item  the content item to wrap
     */
    public void setContentItem(Content item) {
        this.contentItem = item;
    }

    /**
     * Class constructor.  Given the passed contentId, looks up the associated
     * {@link cscie97.asn4.ecommerce.product.Content} item in the {@link cscie97.asn4.ecommerce.product.IProductAPI},
     * and wraps the returned object so that the item inherits from {@link cscie97.asn4.ecommerce.collection.Collectible}
     * and can be easily iterated over by {@link cscie97.asn4.ecommerce.collection.CollectionIterator} and contained in
     * {@link cscie97.asn4.ecommerce.collection.Collectible}s.
     *
     * @param contentId  the content ID of the associated {@link cscie97.asn4.ecommerce.product.Content} item to wrap
     */
    public ContentProxy(String contentId) {
        Content content = ProductAPI.getInstance().getContentByID(contentId);
        if (content != null) {
            this.setContentItem(content);
            this.setId(content.getID());
            this.setName(content.getName());
            this.setDescription(content.getDescription());
        }
    }

    /**
     * Class constructor.  Given the passed {@link cscie97.asn4.ecommerce.product.Content} item, wraps it around
     * the ContentProxy so that the item inherits from {@link cscie97.asn4.ecommerce.collection.Collectible} and can
     * be easily iterated over by {@link cscie97.asn4.ecommerce.collection.CollectionIterator} and contained in
     * {@link cscie97.asn4.ecommerce.collection.Collectible}s.
     *
     * @param contentItem  the content item to wrap
     */
    public ContentProxy(Content contentItem) {
        if (contentItem != null) {
            this.setContentItem(contentItem);
            this.setId(contentItem.getID());
            this.setName(contentItem.getName());
            this.setDescription(contentItem.getDescription());
        }
    }

    /**
     * Overrides method in {@link cscie97.asn4.ecommerce.collection.Collectible}; since ContentProxy objects may only
     * contain single {@link cscie97.asn4.ecommerce.product.Content} items, there is no need for an iterator.
     *
     * @return  always returns null because ContentProxy items are singular
     */
    @Override
    public CollectionIterator getIterator() {
        return null;
    }

    /**
     * Overrides method in {@link cscie97.asn4.ecommerce.collection.Collectible}; since ContentProxy items do not
     * contain child collections, does nothing.
     *
     * @return  since ContentProxy items do not contain children, does nothing
     */
    @Override
    public List<Collectible> getChildren() {
        return null;
    }

    /**
     * Overrides method in {@link cscie97.asn4.ecommerce.collection.Collectible}; since ContentProxy items do not
     * contain child collections, does nothing.
     *
     * @param collectible  not used; method does not modify object in any way
     */
    @Override
    public void add(Collectible collectible) {
        return;
    }

}