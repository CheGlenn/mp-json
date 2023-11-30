import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Random;

/**
 * JSON hashes/objects.
 */
public class JSONHash<K,V> {

  // +--------+------------------------------------------------------
  // | Fields |
  // +--------+

  /**
   * The number of values currently stored in the hash table. We use this to
   * determine when to expand the hash table.
   */
  int size = 0;

  /**
   * The array that we use to store the ArrayList of key/value pairs. 
   */
  Object[] buckets;


  /**
   * An optional reporter to let us observe what the hash table is doing.
   * @author Sam R.
   */
  Reporter reporter;

  /**
   * Do we report basic calls?
   * @author Sam R.
   */
  boolean REPORT_BASIC_CALLS = false;

  /**
   * Our helpful random number generator, used primarily when expanding the size
   * of the table..
   */
   
   Random rand;

  // +--------------+------------------------------------------------
  // | Constructors |
  // +--------------+

  /**
   * creates a JSONHash table
   */
  public JSONHash(){
    this.rand = new Random();
  }

  // +-------------------+-------------------------------------------
  // | SimpleMap methods |
  // +-------------------+

  /**
   * Determine if the hash table contains a particular key.
   */
  @Override
  public boolean containsKey(K key) {
    // STUB/HACK
    try {
      get(key);
      return true;
    } catch (Exception e) {
      return false;
    } // try/catch
  } // containsKey(K)

  /**
   * Apply a function to each key/value pair.
   */
  public void forEach(BiConsumer<? super K, ? super V> action) {
    for (Pair<K,V> pair : this) {
      action.accept(pair.key(), pair.value());
    } // for
  } // forEach(BiConsumer)

  /**
   * Get the value for a particular key.
   */
  @Override
  public V get(K key) {
    int index = find(key);
    @SuppressWarnings("unchecked")
    ArrayList<Pair<K,V>> alist = (ArrayList<Pair<K,V>>) buckets[index];
    if (alist == null) {
      if (REPORT_BASIC_CALLS && (reporter != null)) {
        reporter.report("get(" + key + ") failed");
      } // if reporter != null
      throw new IndexOutOfBoundsException("Invalid key: " + key);
    } else {
      Pair<K,V> pair = alist.get(0);
      if (REPORT_BASIC_CALLS && (reporter != null)) {
        reporter.report("get(" + key + ") => " + pair.value());
      } // if reporter != null
      return pair.value();
    } // get
  } // get(K)

  /**
   * Iterate the keys in some order.
   */
  public Iterator<K> keys() {
    return MiscUtils.transform(this.iterator(), (pair) -> pair.key());
  } // keys()

  /**
   * Remove a key/value pair.
   */
  @Override
  public V remove(K key) {
    // STUB
    return null;
  } // remove(K)

  /**
   * Set a value.
   */
  @SuppressWarnings("unchecked")
  public V set(K key, V value) {
    V result = null;
    // If there are too many entries, expand the table.
    if (this.size > (this.buckets.length * LOAD_FACTOR)) {
      expand();
    } // if there are too many entries

    // Find out where the key belongs and put the pair there.
    int index = find(key);
    ArrayList<Pair<K,V>> alist = (ArrayList<Pair<K,V>>) this.buckets[index];
    // Special case: Nothing there yet
    if (alist == null) {
      alist = new ArrayList<Pair<K,V>>();
      this.buckets[index] = alist;
    }
    alist.add(new Pair<K,V>(key, value));
    ++this.size;

    // Report activity, if appropriate
    if (REPORT_BASIC_CALLS && (reporter != null)) {
      reporter.report("adding '" + key + ":" + value + "' to bucket " + index);
    } // if reporter != null

    // And we're done
    return result;
  } // set(K,V)

  /**
   * Get the size of the dictionary - the number of values stored.
   */
  @Override
  public int size() {
    return this.size;
  } // size()

  /**
   * Iterate the values in some order.
   */
  public Iterator<V> values() {
    return MiscUtils.transform(this.iterator(), (pair) -> pair.value());
  } // values()

  // +-------------------------+-------------------------------------
  // | Standard object methods |
  // +-------------------------+

  /**
   * Convert to a string (e.g., for printing).
   */
  public String toString() {
    String str = "{ "
    while (this.buckets.iterator().hasNext()){
      str += this.buckets.iterator().next().toString();
    }

    return str + " }";
  } // toString()

  /**
   * Compare to another object.
   */
  public boolean equals(Object other) {
    if((object instanceof JSONHash)){
      //check that both hash tables are the same
    }
    return true;        // STUB
  } // equals(Object)

  /**
   * Compute the hash code.
   */
  public int hashCode() {
    return buckets.hashCode();
  } // hashCode()

  // +--------------------+------------------------------------------
  // | Additional methods |
  // +--------------------+

  /**
   * Write the value as JSON.
   */
  public void writeJSON(PrintWriter pen) {
                        // STUB
  } // writeJSON(PrintWriter)

  /**
   * Get the underlying value.
   */
  public Iterator<KVPair<JSONString,JSONValue>> getValue() {
    return this.iterator();
  } // getValue()

  // +-------------------+-------------------------------------------
  // | Hashtable methods |
  // +-------------------+

  /**
   * Determine if the hash table contains a particular key.
   */
  @Override
  public boolean containsKey(K key) {
    // STUB/HACK
    try {
      get(key);
      return true;
    } catch (Exception e) {
      return false;
    } // try/catch
  } // containsKey(K)

  /**
   * Apply a function to each key/value pair.
   */
  public void forEach(BiConsumer<? super K, ? super V> action) {
    for (Pair<K,V> pair : this) {
      action.accept(pair.key(), pair.value());
    } // for
  } // forEach(BiConsumer)

  /**
   * Get the value associated with a key.
   */
  public JSONValue get(JSONString key) {
    int index = find(key);
    
    @SuppressWarnings("unchecked")
    ArrayList<Pair<K,V>> alist = (ArrayList<Pair<K,V>>) buckets[index];
    
    if (alist == null) {
      if (REPORT_BASIC_CALLS && (reporter != null)) {
        reporter.report("get(" + key + ") failed");
      } // if reporter != null
      throw new IndexOutOfBoundsException("Invalid key: " + key);
    } else {
      for (Pair<K,V> pair : alist){
        if(key.equals(pair.key())){
          if (REPORT_BASIC_CALLS && (reporter != null)) {
            reporter.report("get(" + key + ") => " + pair.value());
          } // if reporter != null
          return pair.value();
        }//if
      }//for
      
    }//if 

    return null;
  } // get(JSONString)

  /**
   * Get all of the key/value pairs.
   */
  public Iterator<KVPair<JSONString,JSONValue>> iterator() {
    return new Iterator<KVPair<JSONString,JSONValue>>() {
      int i = 0;
      public boolean hasNext() {
        if(buckets[i+1] == null){
          i++;
          return false;
        }//if
        return true;
      } // hasNext()

      public Pair<JSONString,JSONValue> next() {
      
        ArrayList<Pair<JSONString, JSONValue>> alist = (ArrayList<Pair<JSONString, JSONValue>>) this.buckets[i];
        /** The position in the underlying array */
        int j = 0;
        return alist.get(j++);
        
      } // next()
    }; // new Iterator
  } // iterator()

  /**
   * Set the value associated with a key.
   */
  public void set(JSONString key, JSONValue value) {
    V result = null;
    // If there are too many entries, expand the table.
    if (this.size > (this.buckets.length * LOAD_FACTOR)) {
      expand();
    } // if there are too many entries

    // Find out where the key belongs and put the pair there.
    int index = find(key);
    ArrayList<Pair<K,V>> alist = (ArrayList<Pair<K,V>>) this.buckets[index];
    // Special case: Nothing there yet
    if (alist == null) {
      alist = new ArrayList<Pair<K,V>>();
      this.buckets[index] = alist;
    }
    for (int i = 0; i < alist.size(); i++){
      if(alist.get(i).key().equals(key)){
        alist.set(i, new Pair<K,V>(key, value));
        result = alist.get(i).value();
      }      
    }
    if(result == null){
      alist.add(new Pair<K,V>(key, value));
      ++this.size;
    }

    // And we're done
    return result;
  } // set(JSONString, JSONValue)

  /**
   * Find out how many key/value pairs are in the hash table.
   */
  public int size() {
    return size;
  } // size()

  /**
   * Iterate the keys in some order.
   */
  public Iterator<K> keys() {
    return MiscUtils.transform(this.iterator(), (pair) -> pair.key());
  } // keys()

  /**
   * Set a value.
   */
  @SuppressWarnings("unchecked")
  public V set(K key, V value) {
    V result = null;
    // If there are too many entries, expand the table.
    if (this.size > (this.buckets.length * LOAD_FACTOR)) {
      expand();
    } // if there are too many entries

    // Find out where the key belongs and put the pair there.
    int index = find(key);
    ArrayList<Pair<K,V>> alist = (ArrayList<Pair<K,V>>) this.buckets[index];
    // Special case: Nothing there yet
    if (alist == null) {
      alist = new ArrayList<Pair<K,V>>();
      this.buckets[index] = alist;
    }
    alist.add(new Pair<K,V>(key, value));
    ++this.size;

    // Report activity, if appropriate
    if (REPORT_BASIC_CALLS && (reporter != null)) {
      reporter.report("adding '" + key + ":" + value + "' to bucket " + index);
    } // if reporter != null

    // And we're done
    return result;
  } // set(K,V)


  /**
   * Iterate the values in some order.
   */
  public Iterator<V> values() {
    return MiscUtils.transform(this.iterator(), (pair) -> pair.value());
  } // values()

} // class JSONHash
