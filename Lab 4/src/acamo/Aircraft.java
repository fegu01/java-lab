package acamo;

import messer.ADSBAirbornePositionMessage;
import messer.ADSBAirborneVelocityMessage;
import messer.ADSBAircraftIdentMessage;
import messer.ADSBMessage;

import java.util.Date;

/**
 * Created by Phillip on 25.05.2016.
 *
 * Represents an individual Aircraft.
 * Different Messages will arrive for one particular aircraft.
 */
public class Aircraft {

    //Identifier of Aircraft. Every Message refers to it. Cannot be changed (like const in C++)
    public final String ICAO;

    //Timestamp of Last Activity. If there has been no activity for t+240s
    //the aircraft is inactive. Timestamp will be updates if new Message Arrives.
    public String timestamp;

    //Containing the last received position message
    public ADSBAirbornePositionMessage lastPositionMessage;

    //This Object handles the position interpretation of the messages.
    private AircraftPosition pos = new AircraftPosition();

    //Containing the last received velocity message
    public ADSBAirborneVelocityMessage lastVelocityMessasge;

    //Containing the last received identification message
    public ADSBAircraftIdentMessage lastIdentificationMessage;

    public Aircraft(ADSBMessage msg) {
        this.ICAO = msg.getIcao();

        //Update the "initial" message
        this.updateMessage(msg);
    }

    /**
     * If a new ADSB-Message gets received, this function will handle the logic to
     * update the aircraft.
     *
     * @param msg The new message.
     */
    public void updateMessage(ADSBMessage msg) {

        //Detemine the implemented interface of msg-obj
        if(msg instanceof ADSBAirbornePositionMessage) {
            //We need to cast the msg to its child-object-type
            this.setLastPositionMessage((ADSBAirbornePositionMessage) msg);

            //the pos obj should take care
            pos.updateMessage((ADSBAirbornePositionMessage) msg);
        }else if(msg instanceof ADSBAirborneVelocityMessage) {
            //We need to cast the msg to its child-object-type
            this.setLastVelocityMessasge((ADSBAirborneVelocityMessage) msg);
        }else if(msg instanceof ADSBAircraftIdentMessage) {
            //We need to cast the msg to its child-object-type
            this.setLastIdentificationMessage((ADSBAircraftIdentMessage) msg);
        }else{
            //Seems to be an unknown object.
            System.out.println("[!!] Unknown Object while updating Aircraft-Messages");
            //TODO: Error Handling
        }

        //Last but not least, updating timestamp
        this.setTimestamp(msg.getTimestamp());
    }

    /**
     * Checks if the Aircraft is equal to a given ICAO.
     * Should be simple now to iterate through all planes and updateing the message :D
     * @param ICAO Compare this to the Aircraft.
     * @return True whether the given ICAO is the defined ICAO.
     */
    public boolean isAircraft(String ICAO) {
        return ICAO.equals(this.getICAO());
    }

    /**
     * Checks whether the last activity is NOT 4 Minutes ago.
     * @return True if the last activity is NOT 4 Minutes ago.
     */
    public boolean isActive() {
        //Creating necessary date objects
        String[] times = this.getTimestamp().split("\\.");
        Date timestampDate = new Date(Long.parseLong(times[0]) * 1000);
        Date now = new Date(System.currentTimeMillis());

        //Calculating difference in seconds
        long seconds = (now.getTime()-timestampDate.getTime())/1000;

        return (seconds < 240);
    }

    /**
     * Returns the Call Sign (Aircraft-ID) of an aircraft, if there has been an Identification Message.
     * @return String The Call Sign
     */
    public String getCallSign() {
        //of course there must have been an id-message before
        if (this.getLastIdentificationMessage() != null) {
            return this.getLastIdentificationMessage().getAircraftID();
        }else{
            //otherwise return not available
            return "n.a.";
        }
    }

    /**
     * Returns the Horizontal Speed of an aircraft, if there has been a velocity Message.
     * @return String The Horizontal Speed
     */
    public String getHorizontalSpeed() {
        //of course there must have been an velocity-message before
        if (this.getLastVelocityMessasge() != null) {
            return Float.toString(this.getLastVelocityMessasge().getSpeed());
        }else{
            //otherwise return not available
            return "n.a.";
        }
    }

    /**
     * Returns the Vertical Speed of an aircraft, if there has been a velocity Message.
     * @return String The Vertical Speed
     */
    public String getVerticalSpeed() {
        //of course there must have been an velocity-message before
        if (this.getLastVelocityMessasge() != null) {
            return Float.toString(this.getLastVelocityMessasge().getVertialSpeed());
        }else{
            //otherwise return not available
            return "n.a.";
        }
    }

    /**
     * Returns the heading of an aircraft, if there has been a velocity Message.
     * @return String The Heading
     */
    public String getHeading() {
        //of course there must have been an velocity-message before
        if (this.getLastVelocityMessasge() != null) {
            return Float.toString(this.getLastVelocityMessasge().getHeading());
        }else{
            //otherwise return not available
            return "n.a.";
        }
    }

    /*
        Generated GETTER AND SETTER
     */
    public String getICAO() {
        return ICAO;
    }

    public String getTimestamp() {
        return timestamp;
    }

    private void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public ADSBAirbornePositionMessage getLastPositionMessage() {
        return lastPositionMessage;
    }

    private void setLastPositionMessage(ADSBAirbornePositionMessage lastPositionMessage) {
        this.lastPositionMessage = lastPositionMessage;
    }

    public ADSBAirborneVelocityMessage getLastVelocityMessasge() {
        return lastVelocityMessasge;
    }

    private void setLastVelocityMessasge(ADSBAirborneVelocityMessage lastVelocityMessasge) {
        this.lastVelocityMessasge = lastVelocityMessasge;
    }

    public ADSBAircraftIdentMessage getLastIdentificationMessage() {
        return lastIdentificationMessage;
    }

    private void setLastIdentificationMessage(ADSBAircraftIdentMessage lastIdentificationMessage) {
        this.lastIdentificationMessage = lastIdentificationMessage;
    }

    @Override
    public String toString() {

        String ICAO = (this.getICAO() == null) ? "n.a" : this.getICAO();
        String Timestamp = (this.getTimestamp() == null) ? "n.a" : this.getTimestamp();
        String CallSign = this.getCallSign();
        String Latitude = (this.pos == null && this.pos.getLatitude() != Double.NEGATIVE_INFINITY) ? "n.a" : Double.toString(this.pos.getLatitude());
        String Longitude = (this.pos == null && this.pos.getLongitude() != Double.NEGATIVE_INFINITY) ? "n.a" : Double.toString(this.pos.getLongitude());
        String Altitude = (this.getLastPositionMessage() == null) ? "n.a" : Double.toString(this.getLastPositionMessage().getAltitude());
        String hSpeed = (this.getHorizontalSpeed());
        String vSpeed = (this.getVerticalSpeed());
        String heading = (this.getHeading());

        return ICAO + "\t \t" + Timestamp + "\t \t" + CallSign +
            "\t \t (" + Latitude + ", " + Longitude + ", " + Altitude + ")" +
            "\t \t (" + hSpeed + ", " + vSpeed + ", " + heading + ")";

    }

    public String toKMLString() {
        String ICAO = (this.getICAO() == null) ? "n.a" : this.getICAO();
        String Timestamp = (this.getTimestamp() == null) ? "n.a" : this.getTimestamp();
        String CallSign = this.getCallSign();
        String Latitude = (this.pos == null && this.pos.getLatitude() != Double.NEGATIVE_INFINITY) ? "n.a" : Double.toString(this.pos.getLatitude());
        String Longitude = (this.pos == null && this.pos.getLongitude() != Double.NEGATIVE_INFINITY) ? "n.a" : Double.toString(this.pos.getLongitude());
        String Altitude = (this.getLastPositionMessage() == null) ? "n.a" : Double.toString(this.getLastPositionMessage().getAltitude());
        String hSpeed = (this.getHorizontalSpeed());
        String vSpeed = (this.getVerticalSpeed());
        String heading = (this.getHeading());
        StringBuilder sb = new StringBuilder();
        sb.append("<kml xmlns=\"http://www.opengis.net/kml/2.2\">");

            sb.append("<Document>");
                sb.append("<Style id=\""+ICAO+"\">");
                    sb.append("<IconStyle>");
                        sb.append("<scale>0.7</scale>");
                        sb.append("<heading>" + heading + "</heading>");
                        sb.append("<icon>");
                            sb.append("<href>http://localhost:3333/icons/plane09.png</href>");
                        sb.append("</icon>");
                    sb.append("</IconStyle>");
                sb.append("</Style>");

                sb.append("<Placemark>");
                    sb.append("<name>"+ICAO+"</name>");

                    sb.append("<description>BESCHREIBUNG BIATCH</description>");
                    sb.append("<styleUrl>#"+ICAO+"</styleUrl>");

                    sb.append("<point>");
                        sb.append("<coordinates>"+Longitude+", "+Latitude+", "+Altitude+"</coordinates>");
                        sb.append("<altitudeMode>relativeToGround</altitudeMode>");
                        sb.append("<extrude>1</extrude>");
                    sb.append("</point>");
                sb.append("</Placemark>");
            sb.append("</Document>");
        sb.append("</kml>");

        return sb.toString();
    }
}
