package acamo;

import messer.ADSBAirbornePositionMessage;
import messer.ADSBAirborneVelocityMessage;
import messer.ADSBAircraftIdentMessage;
import messer.ADSBMessage;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import javax.swing.*;



/**
 * Created by Matthias on 01.06.2016.
 */


public class ADSB_Form extends JFrame implements ActionListener
{
    private		JTabbedPane tabbedPane;
    private		JPanel		panel1;
    private		JPanel		panel2;
    private		JPanel		panel3;
    private     JPanel      panel4;

    private     JList       list1;
    private     JList       list2;
    private     JList       list3;
    private     JList       list4;

    private     DefaultListModel<Aircraft>                      ActiveAircraftList;
    private     DefaultListModel<ADSBAirbornePositionMessage>   PositionMassageList;
    private     DefaultListModel<ADSBAirborneVelocityMessage>   VelocityMessageList;
    private     DefaultListModel<ADSBAircraftIdentMessage>      IdentificationMessageList;

    public ADSB_Form()
    {

        setTitle( "ADSB Form" );
        setSize( 1024, 800 );
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //OZ: Standard-Val ist bei HIDE_ON_CLOSE, was das Fenster nur versteckt, aber den Prozess weiter laufen lässt.

        JPanel topPanel = new JPanel();
        topPanel.setLayout( new BorderLayout() );
        getContentPane().add( topPanel );

        // Create the tab pages
        createPage1();
        createPage2();
        createPage3();
        createPage4();

        // Create a tabbed pane
        tabbedPane = new JTabbedPane();
        tabbedPane.addTab( "Active Aircraft", panel1 );
        tabbedPane.addTab( "Position Messages", panel2 );
        tabbedPane.addTab( "Velocity Messages", panel3 );
        tabbedPane.addTab( "Aircraft Identification Messages" , panel4 );
        topPanel.add( tabbedPane, BorderLayout.CENTER );

        this.setVisible( true );
    }

    public void createPage1()
    {
        panel1 = new JPanel();
        ActiveAircraftList = new DefaultListModel<>();
        list1 = new JList(ActiveAircraftList);
        panel1.add(list1);
    }

    /** Styling von Page 2 */
    public void createPage2()
    {
        panel2 = new JPanel();
        PositionMassageList = new DefaultListModel<>();
        list2 = new JList(PositionMassageList);
        panel2.add(list2);

    }

    /** Styling von Page 3 */
    public void createPage3()
    {
        panel3 = new JPanel();
        VelocityMessageList = new DefaultListModel<>();
        list3 = new JList(VelocityMessageList);
        panel3.add(list3);
    }

    /** Styling von Page 4 **/
    public void createPage4()
    {
        panel4 = new JPanel();
        IdentificationMessageList = new DefaultListModel<>();
        list4 = new JList(IdentificationMessageList);
        panel4.add(list4);

    }

    public void actionPerformed (ActionEvent ae) {

    }

    public void fillList(ADSBMessage message) {

        // Check if the Airplane already exits, if not a new Aircraft is created
        boolean found = false;
        for (int i = 0; i < ActiveAircraftList.size(); i++) {
            if (ActiveAircraftList.elementAt(i).getICAO().equals(message.getIcao())) {
                ActiveAircraftList.elementAt(i).updateMessage(message);
                found = true;
            }
        }

        if (!found) {
            ActiveAircraftList.add(0,new Aircraft(message));
        }

        String myClass = message.getClass().toString();
        // Append the Message to the correct List, append to the top.
        switch (myClass) {
            case "class messer.ADSBAirbornePositionMessage":

                PositionMassageList.add(0,(ADSBAirbornePositionMessage) message);
                break;
            case "class messer.ADSBAirborneVelocityMessage":
                VelocityMessageList.add(0,(ADSBAirborneVelocityMessage) message);
                break;

            case "class messer.ADSBAircraftIdentMessage":
                IdentificationMessageList.add(0,(ADSBAircraftIdentMessage) message);
                break;

            case"class messer.ADSBMessage":
                // Branch for all other Message types, this branch should catch all "other" messages
                break;

            default:
                // this branch should NEVER be reached
                JOptionPane.showMessageDialog(null, "Ich bin der Defaultzweig, und ich will niemals was tun! Also tu du was!", "Hilfe!", JOptionPane.OK_CANCEL_OPTION);
        }


        // go through the ActiveAircraftList and search for inactive Aircrafts
        ArrayList<Integer> toDelete = new ArrayList<>();

        for (int i = 0; i < ActiveAircraftList.size(); i++) {
            if (!ActiveAircraftList.elementAt(i).isActive())
                toDelete.add(i);
        }

        if (toDelete.size() > 0) {
            for (int i = ActiveAircraftList.size(); i > 0; i--) {
                ActiveAircraftList.remove(i);

            }
        }
    }
}
