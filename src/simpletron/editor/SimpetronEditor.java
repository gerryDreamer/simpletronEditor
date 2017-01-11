/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package simpletron.editor;

import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
/**
 * This object runs the SimpletronInterface object within a main method, and also provides a system dependent 
 * look and feel for the GUI
 * @see simpletron.editor.SimpletronInterface
 * @see javax.swing.UIManager
 * @author gerry dreamer
 */
public class SimpetronEditor {
/**
 * the default interface frame for the editor
 */
private static SimpletronInterface smlInterface;// = new SimpletronInterface();
/**
 * 
 * @param args the command line arguments
 */
public static void main(String[] args) {
        /**
                         * set the look and feel of this GUI to the native interface
                         */
             try
             {
              UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
             createGUI();
             }
              catch(IllegalAccessException e)
                             {
                                  JOptionPane.showMessageDialog(smlInterface, e.getMessage()+" Illegal access to the default System UI toolkit", e.getMessage()+" Illegal access to the default System UI toolkit", JOptionPane.ERROR_MESSAGE);
                             }
                             catch(InstantiationException e)
                             {
                                 JOptionPane.showMessageDialog(smlInterface, e.getMessage()+" An error occurred while attempting to instantiate the native system UI toolkit", e.getMessage()+" An error occurred while attempting to instantiate the native system UI toolkit", JOptionPane.ERROR_MESSAGE);             
                             }
                             catch(ClassNotFoundException e)
                             {
                                 JOptionPane.showMessageDialog(smlInterface, e.getMessage(), e.getMessage(), JOptionPane.ERROR_MESSAGE);             
                             }
                             catch(UnsupportedLookAndFeelException e)
                             {
                                 JOptionPane.showMessageDialog(smlInterface, e.getMessage(), e.getMessage(), JOptionPane.ERROR_MESSAGE);             
                             }//end catch      
}//end main

public static void createGUI()
{
                smlInterface = new SimpletronInterface();
}
}//end class
