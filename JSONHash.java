import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;


/**
 * JSON hashes/objects.
 * Much of the hash table code was taken from the "Chaining in HashTables" lab 
 * @author Sam R., Che Glenn, Maria Rodriguez
 */
public class JSONHash<K,V> implements JSONValue{

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
   * @author Sam R.
   */
   
   Random rand;

   /**
   * The load factor for expanding the table.
   * @author Sam R.
   */
  static final double LOAD_FACTOR = 0.5;

  // +--------------+------------------------------------------------
  // | Constructors |
  // +--------------+

  /**
   * creates a JSONHash table
   */
  public JSONHash(){
    this.rand = new Random();
    this.clear();
    this.reporter = null;

  }

    /**
   * Create a new hash table that reports activities using a reporter.
   * @author Sam R.
   */
  public JSONHash(Reporter reporter) {
    this();
    this.reporter = reporter;
  } // ChainedHashTable(Reporter)


  // +-------------------------+-------------------------------------
  // | Standard object methods |
  // +-------------------------+

  /**
   * Convert to a string (e.g., for printing).
   */
  public String toString() {
    String returnString = "{";

    /**
     * Loop through each bucket, and the array list in each bucket, to print every KV pair in hash
     */
    for (int i = 0; i < this.buckets.length; i++) {
      @SuppressWarnings("unchecked")
      ArrayList<Pair<K,V>> alist = (ArrayList<Pair<K,V>>) this.buckets[i];
      if (alist != null) {
        for (int j = 0; j < alist.size(); j++) {
          if (j == alist.size()) {
            returnString += "'" + alist.get(j).key() + "': " + alist.get(j).value();
          } else{
            returnString += "'" + alist.get(j).key() + "': " + alist.get(j).value() + ",";
          }
        }
      }
    }

    return returnString + "}";

  } // toString()

  /**
   * Compare to another object.
   */
  public boolean equals(Object other) {
    
    /**
     * check if other object is a hash
     */
    if ((other instanceof JSONHash)) {

      /**
       * Create new hash using other, not neccessary but is cleaner 
       */

      @SuppressWarnings("unchecked")
      JSONHash<JSONString,JSONValue> otherHash = (JSONHash<JSONString,JSONValue>) other;

      /**
       * check if hashes size are the same, then check if the array lists in each last are the same
       * if not the same return false
       */
      if (this.size == otherHash.size) {
          for (int i = 0; i < this.buckets.length; i++) {
            if (this.buckets[i] != null){
              if (!(this.buckets[i].equals(otherHash.buckets[i]))) {
                return false;
              } else{
                return true;
              }
            }
          }
      }
      return false;
    }
    return false;      
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
    this.toString();
  } // writeJSON(PrintWriter)

  /**
   * Get the underlying value.
   */
  public Iterator<Pair<K,V>> getValue() {
    return this.iterator();
  } // getValue()

  // +-------------------+-------------------------------------------
  // | Hashtable methods |
  // +-------------------+

  /**
   * Determine if the hash table contains a particular key.
   */
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
   * Get the value associated with a key.
   */
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
  public Iterator<Pair<K,V>> iterator() {
    return new Iterator<Pair<K,V>>() {
      int i = 0;
      public boolean hasNext() {
        if(buckets[i+1] == null){
          i++;
          hasNext();
        }//if
        i++;
        return true;
      } // hasNext()

      public Pair<K,V> next() {

        @SuppressWarnings("unchecked")
        ArrayList<Pair<K, V>> alist = (ArrayList<Pair<K, V>>) buckets[i];
        /** The position in the underlying array */
        int j = 0;
        return alist.get(j);
        
      } // next()
    }; // new Iterator
  } // iterator()

  /**
   * Set the value associated with a key.
   */
  public V set(K key, V value) {
    V result = null;
    // If there are too many entries, expand the table.
    if (this.size > (this.buckets.length * LOAD_FACTOR)) {
      expand();
    } // if there are too many entries

    // Find out where the key belongs and put the pair there.
    int index = find(key);
    @SuppressWarnings("unchecked")
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
   * Find the index of the entry with a given key. If there is no such entry,
   * return the index of an entry we can use to store that key.
   * @author Sam R.
   */
  int find(K key) {
    return Math.abs(key.hashCode()) % this.buckets.length;
  } // find(K)


  /**
   * Iterate the values in some order.
   */
  public Iterator<V> values() {
    return MiscUtils.transform(this.iterator(), (pair) -> pair.value());
  } // values()


  /**
   * Expand the size of the table.
   */
  void expand() {
    // Figure out the size of the new table
    int newSize = 2 * this.buckets.length + rand.nextInt(10);
    if (REPORT_BASIC_CALLS && (reporter != null)) {
      reporter.report("Expanding to " + newSize + " elements.");
    } // if reporter != null
    // STUB
  } // expand()


  /**
   * Clear the whole table.
   * @author Sam R.
   */
  public void clear() {
    this.buckets = new Object[41];
    this.size = 0;
  } // clear()


    /**
   * Should we report basic calls? Intended mostly for tracing.
   */
  public void reportBasicCalls(boolean report) {
    REPORT_BASIC_CALLS = report;
  } // reportBasicCalls


  /**
   * Dump the hash table.
   */
  public void dump(PrintWriter pen) {
    pen.println("Capacity: " + this.buckets.length + ", Size: " + this.size);
    for (int i = 0; i < this.buckets.length; i++) {
      @SuppressWarnings("unchecked")
      ArrayList<Pair<K,V>> alist = (ArrayList<Pair<K,V>>) this.buckets[i];
      if (alist != null) {
        for (Pair<K,V> pair : alist) {
          pen.println("  " + i + ": <" + pair.key() + "(" + pair.key().hashCode()
              + "):" + pair.value() + ">");
        } // for each pair in the bucket
      } // if the current bucket is not null
    } // for each bucket
  } // dump(PrintWriter)

} // class JSONHash
