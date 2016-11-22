package messer;

/**
 * Created by Phillip on 12.04.2016.
 */
public class ADSBMessageDisplay implements iADSBMessageDisplay {
    @Override
    public void display(ADSBMessage msg) {
        System.out.println(msg);
    }
}
