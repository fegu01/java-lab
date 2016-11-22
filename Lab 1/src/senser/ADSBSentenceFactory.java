package senser;
import java.util.regex.Pattern;
import java.util.regex.Matcher;


public class ADSBSentenceFactory implements ADSBSentenceFactoryInterface
{
	//DONE: define a regular express to filter only relevant messages:
	//{"subscribe":["message","ads.sentence","1379574427.9127481!ADS-B*a;\r\n"]}

	// http://www.regexplannt.com/advanced/java/index.html zum Testen
	//{"subscribe":["message","ads.sentence","1459343083.6589904!ADS-B*8D43BE87585140A498079DE9A7FA;\r\n"]}
	//private static final String patAdsbJson = "\\{  \"subscribe\" \\: \\[ \" message \" \\, \" ads\\.sentence\" [\d\.]+ \\!ADS\\-B\\*[0-9A-F]{28}\\;.*?\\}";

	private static final String patAdsbJson = "([0-9]+.[0-9]+)!ADS\\-B\\*([0-9A-F]{2})([0-9A-F]{6})([0-9A-F]{14})([0-9A-F]{6})";
	private static final Pattern pattern = Pattern.compile(patAdsbJson);
	@Override
	public ADSBSentence fromWebdisJson(String json)
	{
		Matcher matcher = pattern.matcher(json);

		if ( matcher.find() )
		{					
			//DONE: Get distinct values from the json string
		    String timestamp = matcher.group(1);
			String dfca 	 = matcher.group(2);
			String icao 	 = matcher.group(3);
			String payload 	 = matcher.group(4);
			String parity 	 = matcher.group(5);
			
			return new ADSBSentence(timestamp, dfca, icao, payload, parity);
		}
		else
		{
			return null;
		}
	}

	@Override
	public void getObserver() {

	}
}
