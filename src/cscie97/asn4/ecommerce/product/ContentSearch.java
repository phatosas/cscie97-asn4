package cscie97.asn4.ecommerce.product;

import java.util.HashSet;
import java.util.Set;

/**
 * Represents a user search query of the {@link cscie97.asn4.ecommerce.product.IProductAPI} for
 * {@link cscie97.asn4.ecommerce.product.Content} items that match the criteria in the search object.  Since content
 * objects will be changing (adding new {@link cscie97.asn4.ecommerce.product.ContentType} in the future to add more
 * types that may have different attributes, the ContentSearch object can abstract away the differences in types and
 * also simplify the querying of content rather than pass around very long parameter lists to query (just pass around
 * a ContentSearch object instead).  Future expandability and features based on this object could be things such as:
 * <ul>
 *     <li>logging search queries to the ProductAPI</li>
 *     <li>saving and analyzing historical queries for trends, popular searches, terms, etc.</li>
 * </ul>
 *
 * @author David Killeffer &lt;rayden7@gmail.com&gt;
 * @version 1.0
 * @see IProductAPI
 * @see Content
 * @see Application
 * @see Ringtone
 * @see Wallpaper
 */
public class ContentSearch {

    /**
     * Save the original string that was used to create this content search object
     */
    private String rawQuery = "";

    /**
     * Content categories to search content for
     */
    private Set<String> categories = new HashSet<String>(){ };

    /**
     * Text to use for searching content items for; will search on content name, description, and author name
     */
    private String textSearch = "";

    /**
     * Minimum rating to use when searching for content.  NOTE: must use a default value that is outside the range of
     * the valid values for content, and since this is a "minimum" search comparison value, it must be higher than
     * the top end of the rating range, so set it to 6 (otherwise the search will match all items since this
     * search value will be set to zero)
     */
    private int minimumRating = 6;

    /**
     * Maximum price to use when searching for content.  NOTE: must use a default value that is lower than the
     * minimum allowed price for content (which is zero - e.g., "free"), otherwise the search would match all content
     * since by defaulting to zero would mean everything matches on the price attribute.  Default to -1 and require
     * that the search be set to a higher value.
     */
    private float maximumPrice = -1;

    /**
     * All the supported language codes to match on
     */
    private Set<String> supportedLanguages = new HashSet<String>(){ };

    /**
     * All the allowed countries where the searched content can be downloaded
     */
    private Set<Country> countries = new HashSet<Country>(){ };

    /**
     *
     */
    private Set<Device> devices = new HashSet<Device>(){ };

    /**
     * Unique content types to use when searching for matching content
     */
    private Set<ContentType> contentTypes = new HashSet<ContentType>(){ };

    /**
     * Public class constructor with no arguments.  The no-arg constructor is used for building out new ContentSearch
     * objects that get modified as the {@link cscie97.asn4.ecommerce.csv.SearchEngine} loops over a CSV query
     * import file and constructs new ContentSearch objects for each unique search.
     */
    public ContentSearch() { }

    /**
     * Class constructor.  ContentSearch objects are created by the {@link cscie97.asn4.ecommerce.csv.SearchEngine}
     * to capture the search criteria that is from the input CSV files, and then the ContentSearch objects are passed
     * to the Singleton instance of the {@link cscie97.asn4.ecommerce.product.ProductAPI} for executing the actual
     * content search.
     *
     * @param rawQuery            the actual raw string query passed (including commas)
     * @param categories          distinct categories to search for, pipe-separated
     * @param textSearch          the text to search across name, author, and description for
     * @param minimumRating       the minimum rating that all matching content items should be, or higher; defaults to 6 so that ALL content items which are on the 1-5 rating scale will be included
     * @param maximumPrice        the maximum price to match content items on; defaults to -1 if not passed so that ALL content items will be included
     * @param supportedLanguages  set of languages that found content items must support
     * @param countries           set of countries that matching content items must be able to be exported to
     * @param devices             set of devices that matching content items must support
     * @param contentTypes        set of content types that matching content items must be
     */
    public ContentSearch(String rawQuery, Set<String> categories, String textSearch, int minimumRating, float maximumPrice,
                         Set<String> supportedLanguages, Set<Country> countries, Set<Device> devices, Set<ContentType> contentTypes
    ) {
        this.rawQuery = rawQuery;
        this.categories = categories;
        this.textSearch = textSearch;
        this.minimumRating = minimumRating;
        this.maximumPrice = maximumPrice;
        this.supportedLanguages = supportedLanguages;
        this.countries = countries;
        this.devices = devices;
        this.contentTypes = contentTypes;
    }

    public String getRawQuery() {
        return rawQuery;
    }

    public void setRawQuery(String rawQuery) {
        this.rawQuery = rawQuery;
    }

    public Set<String> getCategories() {
        return categories;
    }

    public void setCategories(Set<String> categories) {
        this.categories = categories;
    }

    public String getTextSearch() {
        return textSearch;
    }

    public void setTextSearch(String textSearch) {
        this.textSearch = textSearch;
    }

    public int getMinimumRating() {
        return minimumRating;
    }

    public void setMinimumRating(int minimumRating) {
        this.minimumRating = minimumRating;
    }

    public float getMaximumPrice() {
        return maximumPrice;
    }

    public void setMaximumPrice(float maximumPrice) {
        this.maximumPrice = maximumPrice;
    }

    public Set<String> getSupportedLanguages() {
        return supportedLanguages;
    }

    public void setSupportedLanguages(Set<String> supportedLanguages) {
        this.supportedLanguages = supportedLanguages;
    }

    public Set<Country> getCountries() {
        return countries;
    }

    public void setCountries(Set<Country> country) {
        this.countries = country;
    }

    public Set<Device> getDevices() {
        return devices;
    }

    public void setDevices(Set<Device> devices) {
        this.devices = devices;
    }

    public Set<ContentType> getContentTypes() {
        return contentTypes;
    }

    public void setContentTypes(Set<ContentType> contentTypes) {
        this.contentTypes = contentTypes;
    }

    /**
     * Returns a string representation of the content search; useful for debugging.
     *
     * @return  string representation of the content search
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("CONTENT SEARCH: [") );
        sb.append(String.format(" RAW QUERY: [%s]", this.getRawQuery()));
        sb.append(String.format(" CATEGORIES: [%s]", this.getCategories().toString()));
        sb.append(String.format(" TEXT: [%s]", this.getTextSearch()));
        sb.append(String.format(" MINIMUM RATING: [%d]", this.getMinimumRating()));
        sb.append(String.format(" MAXIMUM PRICE: [%f]", this.getMaximumPrice()));
        sb.append(String.format(" SUPPORTED LANGUAGES: [%s]", this.getSupportedLanguages().toString()));
        sb.append(String.format(" ALLOWED COUNTRIES: [%s]", this.getCountries().toString()));
        sb.append(String.format(" DEVICES: [%s]", this.getDevices().toString()));
        sb.append(String.format(" CONTENT TYPES: [%s]", this.getContentTypes().toString()));
        sb.append("]");
        return sb.toString();
    }

}
