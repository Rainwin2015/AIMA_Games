/**
 * Store Features.Feature and value pairs in a hash table.
 */
package AI;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;

/**
 * @author ZhangKai
 */
public class FeaturesMatrix {

    private HashMap<Features.Feature, String> hash = new HashMap<>();

    //
    // Getter and Setter Methods - START
    public void set(Features.Feature key, int value) {
        hash.put(key, Integer.toString(value));
    }

    public void set(Features.Feature key, double value) {
        hash.put(key, Double.toString(value));
    }

    public void set(Features.Feature key, String value) {
        hash.put(key, value);
    }

    public double getDouble(Features.Feature key) {
        return new Double(hash.get(key)).doubleValue();
    }

    public double getInt(Features.Feature key) {
        return new Integer(hash.get(key)).intValue();
    }

    public String get(Features.Feature key) {
        return hash.get(key);
    }

    public Set<Features.Feature> getKeySet() {
        return hash.keySet();
    }

    public void clear() {

    }
    // Getter and Setter Methods - END
    //

    /**
     * Returns a FeaturesMatrix containing the default values defined in Class
     * Features.Feature {@link Features.Feature} .
     *
     * @return FeaturesMatrix weightMatrix
     */
    public static FeaturesMatrix getDefaultFeaturesMatrix() {
        FeaturesMatrix matrix = new FeaturesMatrix();
        for (Features.Feature feature : Features.Feature.values()) {
            matrix.set(feature, feature.getDefaultValue());
        }

        return matrix;
    }

    /**
     * Returns a FeaturesMatrix containing zero value for all given features.
     *
     * @param keySet
     * @return FeaturesMatrix zeroMatrix
     */
    public static FeaturesMatrix getZeroFeaturesMatrix(Set<Features.Feature> keySet) {
        FeaturesMatrix matrix = new FeaturesMatrix();
        Iterator<Features.Feature> iterator = keySet.iterator();
        while (iterator.hasNext()) {
            matrix.set(iterator.next(), 0);
        }

        return matrix;
    }

    public String toString() {
        return hash.toString();
    }

}
