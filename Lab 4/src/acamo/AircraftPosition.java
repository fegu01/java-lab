package acamo;

import messer.ADSBAirbornePositionMessage;

/**
 * Created by Phillip on 01.06.2016.
 *
 * This class is used to handle the interpretation of the position messages.
 */
public class AircraftPosition {

    //Determines if there are two subsequent messages available.
    private boolean hasSubsequentMessages = false;

    //Last Message
    private ADSBAirbornePositionMessage lastMessage = null;

    //Last Even Message
    private ADSBAirbornePositionMessage lastEvenMessage = null;

    //Last Odd Message
    private ADSBAirbornePositionMessage lastOddMessage = null;

    //Contains Recovered Latitude Position for Even and ODD.
    //Init Value is Double.NEGATIVE_INFINITY
    private double r_lat_0 = Double.NEGATIVE_INFINITY;
    private double r_lat_1 = Double.NEGATIVE_INFINITY;

    //Contains Recovered Longitude Position for Even and ODD.
    //Init Value is Double.NEGATIVE_INFINITY
    private double r_lon_0 = Double.NEGATIVE_INFINITY;
    private double r_lon_1 = Double.NEGATIVE_INFINITY;

    //Contains Reference Point from GlobalLat and GlobalLon
    //Init Value is Double.NEGATIVE_INFINITY
    private double ref_lat = Double.NEGATIVE_INFINITY;
    private double ref_lon = Double.NEGATIVE_INFINITY;



    public void updateMessage(ADSBAirbornePositionMessage msg) {
        //A new message arrived. Identify Pairity and

        switch (msg.getCprFormat()) {
            case 0:
                //Even Parity
                this.lastEvenMessage = msg;
                break;
            case 1:
                //Odd Pairity
                this.lastOddMessage = msg;
                break;
            default:
                //TODO Fehlerbehandlung
                return;
        }

        //If program hasnt returned yet, it means there has been a valid pairity. Remember dat
        this.lastMessage = msg;

        //Are there two subsequent messages yet?
        if (this.lastEvenMessage != null && this.lastOddMessage != null) {
            this.hasSubsequentMessages = true;

            //TODO: Possible breakdown.
            if (this.ref_lat == Double.NEGATIVE_INFINITY)
                this.decodeGlobal();
            else
                this.decodeLocalPosition();
        }
    }


    public double getLatitude() {
        return (this.r_lat_0 != Double.NEGATIVE_INFINITY) ? this.r_lat_0 : Double.NEGATIVE_INFINITY;
    }

    public double getLongitude() {
        return (this.r_lon_0 != Double.NEGATIVE_INFINITY) ? this.r_lon_0 : Double.NEGATIVE_INFINITY;
    }

    /**
     * Decodes the CPR-Position Messages for the global latetude and longitude val.
     * This is where the magic happens.
     *
     */
    private void decodeGlobal() {
        //Two subsequent Position Messages needed
        if(this.hasSubsequentMessages) {
            //Variables needed
            double j,m = 0.0; //Zone Index j

            double d_lat_0 = 360 / (4 * (15) - 0);   //Height of Latitude Zone for Even
            double d_lat_1 = 360 / (4 * (15) - 1);   //Height of Latitude Zone for Odd


            //Calc zone index j. Equation (2) from Documentation. Note: 2^Nb = 2^17 = 131072.
            j = Math.floor( (59 * this.lastEvenMessage.getCprLatitude() - 60 * this.lastOddMessage.getCprLatitude()) / 131072 + 0.5 );

            //Calc Recovered Latitude even R_lat_0 - Equation 3
            this.r_lat_0 = d_lat_0 * (this.MOD(j, 60 - 0) + this.lastEvenMessage.getCprLatitude() / 131072);
            //Calc Recovered Latitude even R_lat_0 - Equation 3
            this.r_lat_1 = d_lat_1 * (this.MOD(j, 60 - 1) + this.lastOddMessage.getCprLatitude() / 131072);


            //Calculate Longitude
            if (this.nl(this.r_lat_0) == this.nl(r_lat_1)) {
                //Calc zone index m. Equation (6) from Documentation. Note: 2^Nb = 2^17 = 131072.
                m = Math.floor(  ((this.nl(this.r_lat_1) - 1) * this.lastEvenMessage.getCprLongitude() - this.nl(this.r_lat_1) * this.lastOddMessage.getCprLongitude()) / 131072   + 0.5   );

                double d_lon_0 = 360 / Math.max((this.nl(this.r_lat_1)) - 0, 1 )    ;   //Height of Latitude Zone for Even
                double d_lon_1 = 360 / Math.max((this.nl(this.r_lat_1)) - 1, 1 );   //Height of Latitude Zone for Even

                this.r_lon_0 = d_lon_0 * (this.MOD(m, this.nl(this.r_lat_1) - 0) + this.lastEvenMessage.getCprLongitude() / 131072);
                this.r_lon_1 = d_lon_1 * (this.MOD(m, this.nl(this.r_lat_1) - 1) + this.lastOddMessage.getCprLongitude() / 131072);


                //Forgot to set the "ref_lat" and "ref_lon" values... >.<
                if (this.lastMessage.getCprFormat() == 0)
                {
                    this.ref_lat = this.r_lat_0;
                    this.ref_lon = this.r_lon_0;
                } else {
                    this.ref_lat = this.r_lat_1;
                    this.ref_lon = this.r_lon_1;
                }
            }else{
                System.out.println("[!!] NL is Wrong. Local Message?");
                //this.decodeLocalPosition();
                return;
            }
        }else{
            System.out.println("[!!] There are no two subsequent messages");
            return;
        }
    }

    private void decodeLocalPosition() {
        //To use local latitude calculation, a global position (reference) has to be calculated.
        if (this.ref_lat != Double.NEGATIVE_INFINITY) {
            // Variables Needed
            double j,m = 0.0;
            double i = this.lastMessage.getCprFormat();

            //Defined in Equation (1) and (13)
            double d_lat_0 = 360 / (4 * 15 - 0);
            double d_lat_1 = 360 / (4 * 15 - 1);
            double d_lon_0 = (this.nl(this.r_lat_0) - 0) > 0 ? 360 / (this.nl(this.r_lat_0) - 0) : 360 ;
            double d_lon_1 = (this.nl(this.r_lat_1) - 0) > 0 ? 360 / (this.nl(this.r_lat_1) - 1) : 360 ;

            //Calculating j from Equation (9)
            if (i == 0)
                j = Math.floor(this.ref_lat / d_lat_0) + Math.floor( this.MOD(this.ref_lat, d_lat_0) / d_lat_0 - this.r_lat_0 / 131072 + 0.5    );
            else
                j = Math.floor(this.ref_lat / d_lat_1) + Math.floor( this.MOD(this.ref_lat, d_lat_1) / d_lat_1 - this.r_lat_1 / 131072 + 0.5    );

            //Calc Equation (10)
            this.r_lat_0 = d_lat_0  * (j + r_lat_0 / 131072);
            this.r_lat_1 = d_lat_1  * (j + r_lat_1 / 131072);

            //Calculating m from Equation (11)
            if (i == 0)
                m = Math.floor(this.ref_lon / d_lon_0) + Math.floor( this.MOD(this.ref_lon, d_lon_0) / d_lon_0 - this.r_lon_0 / 131072 + 0.5    );
            else
                m = Math.floor(this.ref_lon / d_lon_1) + Math.floor( this.MOD(this.ref_lon, d_lon_1) / d_lon_1 - this.r_lon_0 / 131072 + 0.5    );

            //Calc Equation (12)
            this.r_lon_0 = d_lon_0  * (m + r_lon_0 / 131072);
            this.r_lon_1 = d_lon_1  * (m + r_lon_1 / 131072);

            return;
        }else{
            System.out.println("[!!] No Reference Point available");
            return;
        }
    }

    /**
     * MOD Function defiend in Equation 4 from Documentation
     * @param x
     * @param y
     * @return
     */
    private double MOD(double x, double y) {
        return x - y * Math.floor(x/y);
    }

    /**
     * This is the lookup table for NL.
     * @param latitude
     * @return
     */
    private int nl (double latitude) {
        // this lookup procedure associates a number of
        // longitude (NL) zones with a given latitude
        // taken from 1090-WP-9-14: "Transition Table for NL(lat) Function"
        // this implementation does not work close to the poles (some entries missing).
        double lat = Math.abs(latitude);
        if (lat < 10.47047130)
            return 59;
        else if (lat < 14.82817437)
            return 58;
        else if (lat < 18.18626357)
            return 57;
        else if (lat < 21.02939493)
            return 56;
        else if (lat < 23.54504487)
            return 55;
        else if (lat < 25.82924707)
            return 54;
        else if (lat < 27.93898710)
            return 53;
        else if (lat < 29.911356862)
            return 52;
        else if (lat < 31.77209708)
            return 51;
        else if (lat < 33.53993436)
            return 50;
        else if (lat < 35.22899598)
            return 49;
        else if (lat < 36.85025108)
            return 48;
        else if (lat < 38.41241892)
            return 47;
        else if (lat < 39.92256684)
            return 46;
        else if (lat < 41.38651832)
            return 45;
        else if (lat < 42.80914012)
            return 44;
        else if (lat < 44.19454951)
            return 43;
        else if (lat < 45.54626723)
            return 42;
        else if (lat < 46.86733252)
            return 41;
        else if (lat < 48.16039128)
            return 40;
        else if (lat < 49.42776439)
            return 39;
        else if (lat < 50.67150166)
            return 38;
        else if (lat < 51.89342469)
            return 37;
        else if (lat < 53.09516153)
            return 36;
        else if (lat < 54.27817472)
            return 35;
        else if (lat < 55.44378444)
            return 34;
        else if (lat < 56.59318756)
            return 33;
        else if (lat < 57.72747354)
            return 32;
        else if (lat < 58.84763776)
            return 31;
        else if (lat < 59.95459277)
            return 30;
        else if (lat < 61.04917774)
            return 29;
        else if (lat < 62.13216659)
            return 28;
        else if (lat < 63.20427479)
            return 27;
        else if (lat < 64.26616523)
            return 26;
        else if (lat < 65.31845310)
            return 25;
        else if (lat < 66.36171008)
            return 24;
        else if (lat < 67.39646774)
            return 23;
        else if (lat < 68.42322022)
            return 22;
        else if (lat < 69.44242631)
            return 21;
        else if (lat < 70.45451075)
            return 20;
        else if (lat < 71.45986473)
            return 19;
        else if (lat < 72.45884545)
            return 18;
        else if (lat < 73.45177442)
            return 17;
        else if (lat < 74.43893416)
            return 16;
        else if (lat < 75.42056257)
            return 15;
        else if (lat < 76.39684391)
            return 14;
        else if (lat < 77.36789461)
            return 13;
        else if (lat < 78.33374083)
            return 12;
        else if (lat < 79.29428225)
            return 11;
        else
            // north/south of 80.24923213
            return 10;
    }
}

