package senser;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ADSBSentence implements ADSBSentenceInterface
{
	//Done: Create relevant fields
	private String timestamp ="", dfca ="", icao="", payload="", parity="";


	public ADSBSentence(String timestamp, String dfca, String icao, String payload, String parity)
	{
		//DONE: Fill relevant fields
		this.timestamp = timestamp;
		this.dfca = dfca;
		this.icao = icao;
		this.payload = payload;
		this.parity = parity;

	}

	//Done: Create relevant getter and setter methods


	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getDfca() {
		return dfca;
	}

	public void setDfca(String dfca) {
		this.dfca = dfca;
	}

	public String getIcao() {
		return icao;
	}

	public void setIcao(String icao) {
		this.icao = icao;
	}

	public String getPayload() {
		return payload;
	}

	public void setPayload(String payload) {
		this.payload = payload;
	}

	public String getParity() {
		return parity;
	}

	public void setParity(String parity) {
		this.parity = parity;
	}

	//Done: Overwrite toString() method to print our relevant fields
	public String toString()
	{
		//1379574427.9127481			
		String[] times = this.getTimestamp().split("\\.");

		//Done: Define date format
		SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd.mm.yyyy, hh:mm:ss");
		//Done: Create a DAte object
		Date date = new Date(Long.parseLong(times[0]) * 1000);
		//Create time string
		String time = dateFormat.format(date) + "." + times[1];
		
		return "Time:\t\t " + time + "\n" +
				"Dfca:\t\t " + this.getDfca() + "\n" +
				"Originator:\t " + this.getIcao() + "\n" +
				"Payload:\t " + this.getPayload() + "\n" +
				"Parity:\t\t " + this.getParity() + "\n";
	}

	// Special getter for binary and sparating df and ca

	@Override
	public String toBinary(String hexString){
		String result = "";

		result = Long.toBinaryString(Long.parseLong(hexString,16));

		while(result.length() < hexString.length() * 4){
			result = 0 + result;
		}

		// result = Integer.toBinaryString(Integer.parseInt(hexString,16));
		return result;
	}

	@Override
	public String getBinaryPayload() {return toBinary(this.payload);}

	@Override
	public int getDf() {
		return Integer.parseInt(Integer.toBinaryString(Integer.parseInt(this.getDfca(),16)).substring(0,4));
	}

	@Override
	public int getCa() {
		return Integer.parseInt( Integer.toBinaryString(Integer.parseInt(this.getDfca(),16)).substring(5,7));
	}

	@Override
	public String getBinaryIcao() {return toBinary(this.getIcao());}

	@Override
	public String getBinaryParity() {return toBinary(this.getParity());}
}