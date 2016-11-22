package senser;
import acamo.ADSB_Form;
import acamo.AircraftManager;
import com.sun.corba.se.impl.presentation.rmi.ExceptionHandlerImpl;
import messer.ADSBMessage;
import messer.ADSBMessageDisplay;
import messer.ADSBMessageFactory;
import com.sun.net.httpserver.*;
import java.net.InetSocketAddress;
import java.util.Observable;
import java.io.IOException;
import java.io.OutputStream;
import acamo.Aircraft;

class ResourceHandler implements HttpHandler {
	public void handle(HttpExchange t) throws IOException {
		String response = "Alles OK."; // create a string response
		t.sendResponseHeaders(200, response.length());
		OutputStream os = t.getResponseBody();
		os.write(response.getBytes());
		os.close();
	}
}

public class Senser extends Observable implements Runnable
{
	StreamingWebClient client;

	public Senser(String uri)
	{
		client = new StreamingWebClient(uri, 512);
	}

	private String getSentence()
	{
		//TODO: Define an regular expression to read in only relevant sentences
		String filter = "^\\{.*?\\!ADS\\-B\\*[0-9A-F]{28}\\;.*?\\}$";
		// to find . or * you must use \\. or \\*;

		return client.readChunk(filter);
	}
	
	public void run()
	{
		ADSBSentence sentence;
		ADSBMessage message;
		
		//DONE: Create factory and display object
		ADSBSentenceFactory factory = new ADSBSentenceFactory();
		ADSBSentenceDisplay senDisplay = new ADSBSentenceDisplay();
		ADSBMessageDisplay msgDisplay = new ADSBMessageDisplay();
		ADSBMessageFactory mfactory = new ADSBMessageFactory();
		AircraftManager manager = new AircraftManager();



		//ADSB_Form gui = new ADSB_Form();

		//Start HTTP
		try {
			System.out.println("Öffne Server-Socket");
			HttpServer server = HttpServer.create(new InetSocketAddress(25593), 0);
			server.createContext( "/resource", new ResourceHandler () );
			server.setExecutor(null); // create a default executor
			server.start();
		}catch (Exception e) {
			System.out.println("Fehler: " + e.getStackTrace());
		}

		System.out.println("Müsste geklappt haben");

		
		while (true)
		{
			sentence = factory.fromWebdisJson(getSentence());
			
			if (sentence != null)
			{
				//Create Messages (OZ)
				message = mfactory.fromADSBSentence(sentence);

				//Display the message in terminal
				msgDisplay.display(message);

				// Update the Aircraftlist
				manager.update(message);

				//Notify all observers
				setChanged();
				notifyObservers(sentence);
			}			
		}		
	}
}
