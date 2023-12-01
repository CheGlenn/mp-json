import java.io.PrintWriter;

/**
 * A simple set of experiments using our new hash tables.
 * Also containes experiments for JSONHash
 *
 * @author Samuel A. Rebelsky
 * @author Che Glenn
 */
public class HashTableExperiments {

  // +--------+------------------------------------------------------
  // | Fields |
  // +--------+

  /**
   * A word list stolen from some tests that SamR wrote in the distant past.
   */
  static String[] words = {"aardvark", "anteater", "antelope", "bear", "bison",
      "buffalo", "chinchilla", "cat", "dingo", "elephant", "eel",
      "flying squirrel", "fox", "goat", "gnu", "goose", "hippo", "horse",
      "iguana", "jackalope", "kestrel", "llama", "moose", "mongoose", "nilgai",
      "orangutan", "opossum", "red fox", "snake", "tarantula", "tiger",
      "vicuna", "vulture", "wombat", "yak", "zebra", "zorilla"};

  // +-------------+-----------------------------------------------------
  // | Experiments |
  // +-------------+

  /**
   * A short experiment with getting a value from the hash table.
   */
  public static void checkGet(PrintWriter pen, JSONHash<JSONString,JSONValue> htab,
      JSONString key) {
    pen.print("Getting " + key + " ... ");
    pen.flush();
    try {
      pen.println(htab.get(key));
    } catch (Exception e) {
      pen.println("Failed because " + e);
    } // try/catch
  } // checkGet(PrintWriter, HashTable<String,String>, String)

  /**
   * checks if the the JSONHash's are equal by calling JSONHash.equals()
   * @param pen
   * @param htab
   * @param other
   */
  public static void checkEquals(PrintWriter pen, JSONHash<JSONString,JSONValue> htab, JSONHash<JSONString,JSONValue> other) {
    pen.print("Comparing hash tables ...");
    pen.flush();
    try {
      pen.println(htab.equals(other));
    } catch (Exception e) {
      pen.println("Failed beacause " + e);
    }
  }

  /**
   * To string experiment
   * @param pen
   * @param htab
   */
  public static void toStringExperiment(PrintWriter pen, JSONHash<JSONString,JSONValue> htab) {
    htab.set(new JSONString("anteater"), new JSONString("anteater"));
    htab.set(new JSONString("buffalo"), new JSONString("buffalo"));
    pen.println(htab.toString());
  }

  /**
   * Explore what happens when we use set with a repeated key.
   */
  public static void repeatedSetExpt(PrintWriter pen,
      JSONHash<JSONString,JSONValue> htab) {


        htab.set(new JSONString("anteater"), new JSONString("anteater"));
        checkGet(pen, htab, new JSONString("anteater"));

        htab.set(new JSONString("buffalo"), new JSONString("buffalo"));
        checkGet(pen, htab, new JSONString("buffalo"));


  } // repeatedSetExpt(PrintWriter, HashTable)

  /**
   * Explore what happens when we use two keys that map to the same location.
   */
  public static void matchingKeysExpt(PrintWriter pen,
      JSONHash<JSONString,JSONValue> htab) {
    pen.println("Setting anteater");
    htab.set(new JSONString("anteater"), new JSONString("anteater"));
    htab.set(new JSONString("buffalo"), new JSONString("buffalo"));
    checkGet(pen, htab, new JSONString("anteater"));
    checkGet(pen, htab, new JSONString("buffalo"));   
    htab.dump(pen);
    pen.println();
  } // matchingKeysExpt(PrintWriter, HashTable)

  /**
   * Equals experiment
   */

   public static void equalsExpt(PrintWriter pen, JSONHash<JSONString,JSONValue> htab, JSONHash<JSONString,JSONValue> other){
    htab.set(new JSONString("anteater"), new JSONString("anteater"));
    htab.set(new JSONString("buffalo"), new JSONString("buffalo"));

    other.set(new JSONString("anteater"), new JSONString("anteater"));
    other.set(new JSONString("buffalo"), new JSONString("buffalo"));

    checkEquals(pen, htab, other);
   }
} // class HashTableExpt