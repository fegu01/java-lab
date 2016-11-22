package messer;

/**
 * Created by Matthias on 12.04.2016.
 */
public class ADSBMessage implements iADSBMessage {

    // Variablen
    private String timestamp;
    private int type;
    private String icao;
    private int downlinkFormat;
    private int capability;

    ADSBMessage(String icao, int type, int df, int ca, String payload, String timestamp){

        this.capability = ca;
        this.icao = icao;
        this.type = type;
        this.downlinkFormat = df;
        this.timestamp = timestamp;
    }

    public String toString(){

        return this.getIcao() + " Other Message\n"
        + "Type:     " + this.getType() + "\n\n";
        // */

    }

    @Override
    public String getTimestamp(){
        return timestamp;
    };

    @Override
    public int getType() {
        return type;
    }

    @Override
    public String getIcao() {
        return icao;
    }

    @Override
    public int getDownlinkFormat() {
        return downlinkFormat;
    }

    @Override
    public int getCapability() {
        return capability;
    }
}
