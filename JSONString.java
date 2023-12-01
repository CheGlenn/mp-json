import java.io.PrintWriter;
import java.util.Comparator;

/**
 * JSON strings.
 * @author Sam R. , Che Glenn, Maria Rodriguez
 */
public class JSONString implements JSONValue{

  // +--------+------------------------------------------------------
  // | Fields |
  // +--------+

  /**
   * The underlying string.
   */
  String value;




  // +--------------+------------------------------------------------
  // | Constructors |
  // +--------------+

  /**
   * Build a new JSON string for a particular string.
   */
  public JSONString(String value) {
    this.value = value;
  } // JSONString(String)

  // +-------------------------+-------------------------------------
  // | Standard object methods |
  // +-------------------------+

  /**
   * Convert to a string (e.g., for printing).
   */
  public String toString() {
    if (value == null) {
      return "null";
    } else {
      return this.value;
    }
  } // toString()

  /**
   * Compare to another object.
   */
  public boolean equals(Object other) {

    return ( ( (other instanceof JSONString) 
               && (this.value == ((JSONString) other).value) )
             || (this.value == other) );
    
  } // equals(Object)

  /**
   * Compute the hash code.
   */
  public int hashCode() {
    if (this.value == null) {
      return 0;
    } else {
      return this.value.hashCode();
    }
  } // hashCode()

  // +--------------------+------------------------------------------
  // | Additional methods |
  // +--------------------+

  /**
   * Write the value as JSON.
   */
  public void writeJSON(PrintWriter pen) {
    pen.println(writeJSONHelper(this.value));
  } // writeJSON(PrintWriter)


  /**
   * Helper of writeJSON, takes in a str and translates to JSON
   * @param str
   * @post target string translates to JSON
   */
  public String writeJSONHelper(String str){
    String target = "";
    
    for (int i = 0; i < str.length(); i++) {
      if (str.charAt(i) == '"'){
        target += '\"';
      } else {
        target += str.charAt(i);
      }
    }

    return target;
  }

  /**
   * Get the underlying value.
   */
  public String getValue() {
    return this.value;
  } // getValue()

} // class JSONString
