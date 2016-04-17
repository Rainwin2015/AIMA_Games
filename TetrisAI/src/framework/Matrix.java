package framework;

import java.util.Hashtable;

/**
 * Created by RayZK on 05/04/16.
 */
public interface Matrix<E, K> {

    public K get(E key);

    public void set(E key, K value);
}
