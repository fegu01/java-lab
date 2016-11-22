package acamo;
import messer.ADSBMessage;
import java.util.ArrayList;
import redis.clients.jedis.Jedis;

/**
 * Created by tommel on 16/06/16.
 */
public class AircraftManager {

    private ArrayList<Aircraft>aircraftList;

    private Jedis myJedis;

    public AircraftManager(){

        // List containing Aircraft
        aircraftList = new ArrayList<>();
        // Jedisclient to fill Redis-Databse with kml Strings
        Jedis myJedis = new Jedis("localhost");
    }

    public void update(ADSBMessage m){

        // Check if the Airplane already exits, if not a new Aircraft is created
        boolean found = false;
        for (int i = 0; i < aircraftList.size(); i++) {
            if(aircraftList.get(i).getICAO().equals(m.getIcao())){
                aircraftList.get(i).updateMessage(m);
                myJedis.set(aircraftList.get(i), aircraftList.get(i).toString());
                found = true;
            }
        }

        if (!found) {
            Aircraft a = new Aircraft(m);

            myJedis.set(a.getICAO(), a.toString());
            aircraftList.add(new Aircraft(m));
        }
    }
}
