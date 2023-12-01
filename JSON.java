import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.text.ParseException;

/**
 * Utilities for our simple implementation of JSON.
 */
public class JSON {
  // +---------------+-----------------------------------------------
  // | Static fields |
  // +---------------+

  /**
   * The current position in the input.
   */
  static int pos;

  // +----------------+----------------------------------------------
  // | Static methods |
  // +----------------+

  /**
   * Parse a string into JSON.
   */
  public static JSONValue parse(String source) throws ParseException, IOException {
    return parse(new StringReader(source));
  } // parse(String)

  /**
   * Parse a file into JSON.
   */
  public static JSONValue parseFile(String filename) throws ParseException, IOException {
    FileReader reader = new FileReader(filename);
    JSONValue result = parse(reader);
    reader.close();
    return result;
  } // parseFile(String)

  /**
   * Parse JSON from a reader.
   */
  public static JSONValue parse(Reader source) throws ParseException, IOException {
    pos = 0;
    JSONValue result = parseKernel(source);
    if (-1 != skipWhitespace(source)) {
      throw new ParseException("Characters remain at end", pos);
    }
    return result;
  } // parse(Reader)

  // +---------------+-----------------------------------------------
  // | Local helpers |
  // +---------------+

  /**
   * Parse JSON from a reader, keeping track of the current position
   */
  static JSONValue parseKernel(Reader source) throws ParseException, IOException {
    int ch;
    String str = "";
    JSONHash<JSONString,JSONValue> hash = new JSONHash<>();
    /**
    we might want our key var here, but then there might eb an issue where it might not be initialized
    in that case, we could simply define the first string as the first key, and then redefine from there (within our switch)
     */
    ch = skipWhitespace(source);
    if (-1 == ch) {
      throw new ParseException("Unexpected end of file", pos);
    }

    switch((char) ch){
      case '{':
        while(ch != '}'){
          //ch = skipWhitespace(source);
          JSONString key = new JSONString(parseKernel(source).toString()); //this would be our key
          //parseKernel(source);
          hash.set(key, parseKernel(source));
          hash.toString();
        }
        
        return hash;
        //recursively call parse
      case '"':
        str = ""; //reset our string
        ch = skipWhitespace(source);
        while((char) ch != '"'){
          str += (char) ch;
          ch = skipWhitespace(source);
        } // while
        ch = skipWhitespace(source); // ""
        if("true".equals(str) || "false".equals(str) || str == null){
          JSONConstant bool = new JSONConstant(str);
          return bool; // return constant
        }
        return new JSONString(str); // return string
      case ':':
        ch = skipWhitespace(source);
        parseKernel(source); //this would be our val
      case ',':
        // i think here is where we reach the end of our KVpair and want to add it to the hash

        // or we use this as a lobby/waiting room, and move to next char
        ch = skipWhitespace(source);
      case '[':
        JSONArray arr = new JSONArray();
        //ch = skipWhitespace(source);
        while(ch != ']'){
          arr.add(parseKernel(source)); //keep adding things to the list
          //ch = skipWhitespace(source);
        } // while
        return arr; // return array
      case '}':
        return hash;
      default: //here we look for nums

        while(ch != ','){
          str += (char) ch; //this adds numbers to the string until it reaches whitespace
          ch = skipWhitespace(source);
          
        }//while
        for(int i = 0; i < str.length(); i++){
          if(str.charAt(i) == '.' || str.charAt(i) == 'e' || str.charAt(i) == 'E' ){
            JSONReal real = new JSONReal(str);
            return real; // returns real
          }// if real number
        }//for
        if(str.charAt(0) == '-'){// if it's a negative
          if(str.substring(1, 3) == "00"){
            System.err.println("Negative numbers with leading zeroes not permitted");
          }//if leading zeros
        }//if
        JSONInteger num = new JSONInteger(str);
        return num; //return num
      }//switch
    

    //return hash;
  } // parseKernel

  /**
   * Get the next character from source, skipping over whitespace.
   */
  static int skipWhitespace(Reader source) throws IOException {
    int ch;
    do {
      ch = source.read();
      ++pos;
    } while (isWhitespace(ch));
    return ch;
  } // skipWhitespace(Reader)

  /**
   * Determine if a character is JSON whitespace (newline, carriage return,
   * space, or tab).
   */
  static boolean isWhitespace(int ch) {
    return (' ' == ch) || ('\n' == ch) || ('\r' == ch) || ('\t' == ch);
  } // isWhiteSpace(int)

} // class JSON
