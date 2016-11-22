package messer;

import senser.ADSBSentence;
import java.util.*;

/**
 * Created by Matthias on 12.04.2016.
 */
public class ADSBAircraftIdentMessage extends ADSBMessage implements iADSBAircraftIdentMessage  {

    // Variablen

    private int emitterCategory;                                // field for emitter category
    private String aircraftID;                                  // field for Aircraft ID
    private String aircraftIDParts[] = new String[8];           // contains 6 bit per row, representing a char of the Aircraft ID
    private String airCraftIdBinary = "";                       // contains the binary representation of the Aircraft ID

    // Konstruktor

    public ADSBAircraftIdentMessage(ADSBSentence sentence, int type){

        // Superconstructor
        // For df and ca: Parse the Hex Value to binary, split in 5 and 3 Bit, and parse as Int
        super(sentence.getIcao()
                , type
                , sentence.getDf()
                , sentence.getCa()
                , sentence.getPayload()
                , sentence.getTimestamp());

        // Fill flieds of the AircraftID object

        // Fill emitter Category - just an Integer?
        emitterCategory = Integer.parseInt(sentence.getBinaryPayload().substring(5,7),2);

        // getting the aircaftID binary
        airCraftIdBinary = sentence.getBinaryPayload().substring(8,56);

        // sepparate the 8 Byte Aircraft ID
        splitAircraftID(airCraftIdBinary, aircraftIDParts);

        // parse the parts to byte, the Aircraft ID is returned as string
        aircraftID = parseAircraftID(aircraftIDParts);
    }

    private void splitAircraftID(String aircraftID, String aircraftIDParts[]){
        for(int i = 0; i < 8; i++){
            aircraftIDParts[i] = aircraftID.substring(0 + (i * 6), 6 + (i * 6));
        }
    }

    private String parseAircraftID(String airCraftIdParts[]){

        // Variablen
        StringBuilder result = new StringBuilder("");                     // Result string will contain Aircraft ID
        Map<String,Character>charTable = new HashMap<String,Character>(); // Contains the ASCII Table

        // Fill the Table
        fillCharTable(charTable);

        // Parse binary to character
        for(int i = 0; i < 8; i++){
            result.append(charTable.get(airCraftIdParts[i]));
        }

        return result.toString();
    }

    private void fillCharTable(Map<String, Character>charTable){

        charTable.put("000000" , '@');
        charTable.put("000001" , 'A');
        charTable.put("000010" , 'B');
        charTable.put("000011" , 'C');
        charTable.put("000100" , 'D');
        charTable.put("000101" , 'E');
        charTable.put("000110" , 'F');
        charTable.put("000111" , 'G');
        charTable.put("001000" , 'H');
        charTable.put("001001" , 'I');
        charTable.put("001010" , 'J');
        charTable.put("001011" , 'K');
        charTable.put("001100" , 'L');
        charTable.put("001101" , 'M');
        charTable.put("001110" , 'N');
        charTable.put("001111" , 'O');
        charTable.put("010000" , 'P');
        charTable.put("010001" , 'Q');
        charTable.put("010010" , 'R');
        charTable.put("010011" , 'S');
        charTable.put("010100" , 'T');
        charTable.put("010101" , 'U');
        charTable.put("010110" , 'V');
        charTable.put("010111" , 'W');
        charTable.put("011000" , 'X');
        charTable.put("011001" , 'Y');
        charTable.put("011010" , 'Z');
        charTable.put("011011" , '[');
        charTable.put("011100" , '=');
        charTable.put("011101" , ']');
        charTable.put("011110" , 'ˆ');
        charTable.put("011111" , '_');
        charTable.put("100000" , ' ');
        charTable.put("100001" , '!');
        charTable.put("100010" , '̈');
        charTable.put("100011" , '#');
        charTable.put("100100" , '$');
        charTable.put("100101" , '%');
        charTable.put("100110" , '&');
        charTable.put("100111" , '’');
        charTable.put("101000" , '(');
        charTable.put("101001" , ')');
        charTable.put("101010" , '*');
        charTable.put("101011" , '+');
        charTable.put("101100" , ',');
        charTable.put("101101" , '-');
        charTable.put("101110" , '.');
        charTable.put("101111" , '/');
        charTable.put("110000" , '0');
        charTable.put("110001" , '1');
        charTable.put("110010" , '2');
        charTable.put("110011" , '3');
        charTable.put("110100" , '4');
        charTable.put("110101" , '5');
        charTable.put("110110" , '6');
        charTable.put("110111" , '7');
        charTable.put("111000" , '8');
        charTable.put("111001" , '9');
        charTable.put("111010" , ':');
        charTable.put("111011" , ';');
        charTable.put("111100" , '<');
        charTable.put("111101" , '=');
        charTable.put("111110" , '>');
        charTable.put("111111" , '?');
    }

    @Override
    public String toString(){
        return this.getIcao() + " Aircraft Identification and Category Message\n"
        +"Ident:    " + this.getAircraftID() +"\n"
        +"Categ:    " + this.getEmitterCatergory() + "\n\n";
    }

    // Getter

    @Override
    public int getEmitterCatergory() {
        return emitterCategory;
    }

    @Override
    public String getAircraftID() {
        return aircraftID;
    }
}
