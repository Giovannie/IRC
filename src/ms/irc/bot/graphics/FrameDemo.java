package ms.irc.bot.graphics;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.text.html.HTMLDocument;

/* FrameDemo.java requires no other files. */
public class FrameDemo {
    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("FrameDemo");
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        //main input TextArea
        JTextArea mainIn = new JTextArea("Type your Command here.", 3, 1);
        
        //main Output Display Displays htmlDocuments
        JTextPane mainOut = new JTextPane(new HTMLDocument());
        mainOut.setPreferredSize(new Dimension(800, 600));
        
        //add components to Pane
        frame.getContentPane().add(mainOut);
        frame.getContentPane().add(mainIn);

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
}
