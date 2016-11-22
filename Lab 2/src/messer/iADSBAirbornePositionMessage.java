package messer;

/**
 * Created by Matthias on 12.04.2016.
 */
public interface iADSBAirbornePositionMessage {

    public int getSurveillanceStatus();
    public int getNicSupplement();
    public int getAltitude();
    public int getTimeFlag();
    public int getCprFormat();
    public int getCprLongitude();
    public int getCprLatitude();

}
