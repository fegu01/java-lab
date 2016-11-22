package messer;

/**
 * Created by Matthias on 12.04.2016.
 * *
 * Weitergeführt von Felix, eingefügt von Phillip
 */
interface iADSBAirborneVelocityMessage {

    public int getSubType();

    public int getIntentChange();

    public int getReservedA();

    public int getNavigationAccuracy();

    public float getSpeed();

    public float getHeading();

    public int getVerticalRateSource();

    public int getVertialSpeed();

}
