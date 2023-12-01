import java.io.File;
import java.io.PrintWriter;
import java.util.Hashtable;

/**
 * A simple experiment with probed hash tables.
 */
public class JSONHashExperiments {

  // +------+------------------------------------------------------------
  // | Main |
  // +------+

  /**
   * Do whatever experiments seem reasonable.
   */
  public static void main(String[] args) throws Exception{
    // Create our normal output mechanism.
    final PrintWriter pen = new PrintWriter(System.out, true);
    // Convert the PrintWriter to a Reporter.
    Reporter rept = new Reporter() {
      public void report(String str) {
        pen.println("*** " + str);
      } // report(String)
    }; // new Reporter()

    File name = new File("test.txt");
    //PrintWriter pen = new PrintWriter(System.out, true);
    pen.println(JSON.parseFile("test.txt").toString());  

    // Create a new hash table
    JSONHash<JSONString, JSONValue> htab =
        new JSONHash<JSONString, JSONValue>(rept);

        JSONHash<JSONString,JSONValue> other = new JSONHash<JSONString,JSONValue>(rept);
    

    // Most of the time, we don't care about the basic calls
    htab.reportBasicCalls(false);

    // Conduct some of the experiments
    //HashTableExperiments.matchingKeysExpt(pen, htab);
    //HashTableExperiments.equalsExpt(pen, htab, other);
    //HashTableExperiments.repeatedSetExpt(pen, htab);
    // HashTableExperiments.matchingSetExpt(pen, htab);
    // HashTableExperiments.multipleSetExpt(pen, htab);
    // HashTableExperiments.removeExpt(pen, htab);
    //HashTableExperiments.toStringExperiment(pen, htab);
  } // main(String[])

} // class ChainedHashTableExperiments