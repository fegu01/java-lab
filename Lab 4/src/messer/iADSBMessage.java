package messer;

/**
 * Created by Phillip on 12.04.2016.
 */
public interface iADSBMessage {
    public String getTimestamp();
    public int getType();
    public String getIcao();
    public int getDownlinkFormat();
    public int getCapability();
}
