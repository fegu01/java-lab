package messer;

import senser.ADSBSentence;

/**
 * Created by Matthias on 12.04.2016.
 */
public class ADSBMessageFactory implements iADSBMessageFactory {

    public ADSBMessage fromADSBSentence(ADSBSentence sentence) {

        // Variables

        String typeHexSubtring = "";           // Contains the type as hex
        String typeBinSubstring = "";          // Contains the type as binary
        int type = -1;                         // Contains the type
        ADSBMessage result;                    // Reference for the Object returned

        // Trim payload and convert to binary

        typeHexSubtring = sentence.getPayload().substring(0,2); // Get the first two bytes
        typeBinSubstring = Integer.toBinaryString(Integer.parseInt(typeHexSubtring,16));

        // Leading zeros are lost!
        while(typeBinSubstring.length() < 8){
            typeBinSubstring = 0 + typeBinSubstring;
        }

        // Determine the type
        // Trim from 8 to 5 bits

        typeBinSubstring = typeBinSubstring.substring(0,5);
        type = Integer.parseInt(typeBinSubstring,2);

        // Crate Message Object

        switch(type){

            // Caseblock for Velocity Message
            case(19):
                result = new ADSBAirborneVelocityMessage(sentence, type);
                break;

            // Caseblock for Position Message
            case(0):
            case(1):
            case(2):
            case(3):
            case(4):
                result = new ADSBAircraftIdentMessage(sentence,type);
                break;

            // Caseblock for Position Message
            case( 9):
            case( 10):
            case( 11):
            case( 12):
            case( 13):
            case( 14):
            case( 15):
            case( 16):
            case( 17):
            case( 18):
                result = new ADSBAirbornePositionMessage(sentence, type);
                break;

            // Default Create Others Message Object
            default:
                result = new ADSBMessage(sentence.getIcao(),
                        type,
                        Integer.parseInt(sentence.getDfca().substring(0,1),16),
                        Integer.parseInt(sentence.getDfca().substring(1),16),
                        sentence.getPayload(),
                        sentence.getTimestamp()); // */
        }
        return result;
    }
}
