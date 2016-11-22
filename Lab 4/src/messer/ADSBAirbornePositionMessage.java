package messer;
import senser.ADSBSentence;

/**
 * Created by Matthias on 12.04.2016.
 */
public class ADSBAirbornePositionMessage extends ADSBMessage implements iADSBAirbornePositionMessage {

    private int surveilanceStatus;
    private int nic;
    private int altitude;
    private int timeFlag;
    private int crpFormat;
    private int crpLongitude;
    private int crpLatitude;

    public ADSBAirbornePositionMessage(ADSBSentence value, int type){

        // ToDo
        super(value.getIcao()
                , type
                , value.getDf()
                , value.getCa()
                , value.getPayload(),value.getTimestamp());

        encodeDate(value.getBinaryPayload());
    }

    public String toString(){

        return this.getIcao() + " Airbourne Position Message\n"
                + "Type:     " + this.getType() + "\n"
                + "Alti:     " + this.getAltitude() + "\n"
                + "Latlon:   " + this.getCprLatitude() + " : " + getCprLongitude() + "\n"
                + "Format:   " + this.getCprFormat() + "\n\n";
                // */



    }

    private void encodeDate (String binPayload) {

        // Surveillance Status auslesen
        String temp = binPayload.substring(6, 7);
        this.surveilanceStatus = Integer.parseInt(temp, 2);

        // Nic auslesen
        temp = binPayload.substring(8, 9);
        this.nic = Integer.parseInt(temp, 2);

        // Altitude auslesen
        temp = binPayload.substring(9, 14);
        temp += binPayload.substring(16, 19);
        this.altitude = Integer.parseInt(temp, 2);
        if (binPayload.substring(15, 15).equals("0")) {
            this.altitude = 100 * this.altitude;
        }
        else{
            this.altitude = 25 * this.altitude;
        }

        // Time Flag auslesen
        temp = binPayload.substring(20,21);
        this.timeFlag = Integer.parseInt(temp,2);

        // CRP Format auslesen
        temp = binPayload.substring(21,22);
        this.crpFormat = Integer.parseInt(temp,2);

        // CRP Latitude auslesen
        temp = binPayload.substring(22,38);
        this.crpLatitude = Integer.parseInt(temp,2);

        // CRP Longitude auslesen
        temp = binPayload.substring(39, binPayload.length());
        this.crpLongitude = Integer.parseInt(temp,2);
    }

    public int getSurveillanceStatus(){
        return this.surveilanceStatus;
    }

    public int getNicSupplement(){
        return this.nic;
    }

    public int getAltitude(){
        return this.altitude;
    }

    public int getTimeFlag(){
        return this.timeFlag;
    }

    public int getCprFormat(){
        return this.crpFormat;
    }

    public int getCprLongitude(){
        return this.crpLongitude;
    }

    public int getCprLatitude(){
        return this.crpLatitude;
    }

}
