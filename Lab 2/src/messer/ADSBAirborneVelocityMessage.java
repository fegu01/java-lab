package messer;
import senser.ADSBSentence;
/**
 * Created by Matthias on 12.04.2016.
 */
public class ADSBAirborneVelocityMessage extends ADSBMessage implements iADSBAirborneVelocityMessage{
// Variablen

    private int mSubType;
    private int mIntentChange;
    private int mReservedA;

    private int mNavigationAccuracy;
    private int eastWestDirection; //0=east 1=west;
    private int eastWestVelocity; //

    private int northSouthDirection; //0=north 1=south
    private int northSouthVelocity; //

    private float mSpeed;
    private float mHeading;
    private int mVerticalRateSign;
    private int mVerticalRateSource;
    private int mVerticalSpeed; //



    //Constructor

    /*
    * */
    public ADSBAirborneVelocityMessage(ADSBSentence sentence, int type) {
        //super(timestamp, icao, type, downlinkFormat, capability, payload);
        super(sentence.getIcao()
                , type
                , sentence.getDf()
                , sentence.getCa()
                , sentence.getPayload()
                , sentence.getTimestamp());

        encode(sentence.getBinaryPayload());
    }

        //Encode methode zum auslesen
        private void encode(String binPayload){

            this.mSubType = Integer.parseInt(binPayload.substring(5,8));
            //intent change
            this.mIntentChange = Integer.parseInt(binPayload.substring(8,9));
            this.mReservedA = Integer.parseInt(binPayload.substring(9,10));
            this.mNavigationAccuracy = Integer.parseInt(binPayload.substring(10,13));
            this.mVerticalRateSource = Integer.parseInt(binPayload.substring(35,36));
            this.mVerticalRateSign=Integer.parseInt(binPayload.substring(36,37));
            this.mVerticalSpeed = ((Integer.parseInt(binPayload.substring(37,46),2))-1)*64;



            if (this.mSubType == 1 || this.mSubType == 2) {
                this.eastWestDirection = Integer.parseInt(binPayload.substring(13,14),2);
                this.eastWestVelocity = Integer.parseInt(binPayload.substring(14,24),2);
                this.northSouthDirection = Integer.parseInt(binPayload.substring(24,25),2);
                this.northSouthVelocity = Integer.parseInt((binPayload.substring(25,35)),2);
                // float northSouthVelocity = Integer.parseInt(binPayload.substring(25,35));

                if(this.northSouthVelocity > 0)
                    this.northSouthVelocity -=1;

                if(this.eastWestVelocity > 0)
                    this.eastWestVelocity -=1;

                //pythagoras
                this.mSpeed = (float) Math.sqrt((eastWestVelocity * eastWestVelocity) + (northSouthVelocity * northSouthVelocity));

                // this.mSpeed = (float) Math.sqrt(Math.pow(eastWestVelocity, 2) + Math.pow(northSouthVelocity, 2));

                this.mHeading = (float) ((Math.atan(northSouthVelocity
                        / eastWestVelocity) * 360) / (2 * Math.PI));

                if (eastWestDirection == 0 && northSouthDirection == 0) {
                    this.mHeading = 90 - this.mHeading;
                } else if (eastWestDirection == 0 && northSouthDirection == 1) {
                    this.mHeading += 90;
                } else if (eastWestDirection == 1 && northSouthDirection == 1) {
                    this.mHeading = 270 - this.mHeading;
                } else {
                    this.mHeading += 270;

                } // */


                //subtype ist großer also 3||4
            }else{
                if(Integer.parseInt(binPayload.substring(13,14)) != 0){
                    this.mHeading = Float.parseFloat(binPayload.substring(14,24))* 0.3515625f;
                }
                if(Integer.parseInt(binPayload.substring(24,25)) == 0 ){
                    // Indicaded Airspeed
                    this.mSpeed = Float.parseFloat(binPayload.substring(25,34))-1;
                }
                else{
                    // True Airspeed
                }
                    this.mSpeed = Float.parseFloat(binPayload.substring(25,34))-1;
                }
        }
        //subtype
    public String toString(){


        return this.getIcao() + " Airborne Velocity Message\n"
                + "Speed:    " + this.getSpeed() + "\n"
                + "Heading:  " + this.getHeading() + "\n"
                + "Vertic:   " + this.getVertialSpeed() + "\n\n";
                // */
    }


// Alle Getter alle Ohne Exceptions

    public int getSubType() {
        return this.mSubType;
    }

    public int getIntentChange() {
        return this.mIntentChange;
    }

    public int getReservedA() {
        return this.mReservedA;
    }

    public int getNavigationAccuracy() {
        return this.mNavigationAccuracy;
    }

    public float getSpeed() {
        return this.mSpeed;
    }

    public float getHeading() {
        return this.mHeading;
    }

    public int getVerticalRateSource() {
        return this.mVerticalRateSource;
    }

    public int getVertialSpeed() {
        return this.mVerticalSpeed;
    }

}
