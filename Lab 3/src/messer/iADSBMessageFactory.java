package messer;

import senser.ADSBSentence;

/**
 * Created by Matthias on 12.04.2016.
 */
public interface iADSBMessageFactory {

    public ADSBMessage fromADSBSentence(ADSBSentence sentence);

}
