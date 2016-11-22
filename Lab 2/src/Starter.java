import client.Client;
import messer.ADSBMessageFactory;
import senser.ADSBSentence;
import senser.Senser;

import messer.ADSBMessage;
import messer.iADSBMessageFactory;




public class Starter
{

	public static void main(String[] args)
	{

		String urlString = "http://flugmon-it.hs-esslingen.de/subscribe/ads.sentence";
		Senser server = new Senser(urlString);
		
		Client client = new Client();
		server.addObserver(client);

		client.start();
		new Thread(server).start();
		// */

	}
}
