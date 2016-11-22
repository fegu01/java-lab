package senser;
import messer.ADSBMessage;
import messer.ADSBMessageDisplay;
import messer.ADSBMessageFactory;

import java.util.Observable;

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
		
		while (true)
		{
			sentence = factory.fromWebdisJson(getSentence());
			
			if (sentence != null)
			{
				//Display the sentence
				//senDisplay.display(sentence);

				//Create Messages (OZ)
				message = mfactory.fromADSBSentence(sentence);

				//Display the message
				msgDisplay.display(message);

				//Notify all observers
				setChanged();
				notifyObservers(sentence);
			}			
		}		
	}
}
