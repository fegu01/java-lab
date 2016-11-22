package senser;

public interface ADSBSentenceInterface
{
	public String getTimestamp();
	public String getDfca();
	public String getIcao();
	public String getParity();
	public String getPayload();

	public String getBinaryPayload();
	public int getDf();
	public int getCa();
	public String getBinaryIcao();
	public String getBinaryParity();

	String toBinary(String hexString);
}
