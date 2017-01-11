/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package simpletron.editor;

import com.dreamer.color.Colors;
import com.dreamer.file.JEditorPaneInput;
import com.dreamer.file.JEditorPaneOutput;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.util.ArrayList;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import simpletron.hardware.Processor;
import simpletron.records.CreateSimpletronRecord;
import simpletron.records.ReadSimpletronRecord;
/**
 * This object provides the GUI interface for manipulating both the simple language 
 * <br>and the SML within a simulator
 * @author gerry dreamer
 */
public class SimpletronInterface extends JFrame{
    /**
     * the current text editor object
     * @see javax.swing.JEditorPane
     */
   private JEditorPane editorPane = new JEditorPane();
   /**
    * the top menu bar of this object 
    */
   private JMenuBar topMenuBar = new JMenuBar();
   /**
    * the control panel or ribbon for this object
    * acts as a container for the combo boxes for this object
    */
   private JPanel controlPanel = new JPanel();
   /**
    * The container holding items to be added to the status bar
    */
   private JPanel statusPanel = new JPanel(); 
   /**
    * the default color scheme for the frame's background
    */
   private Color defaultThemeColor = new Color(250,250,250);
   /**
    * the default file menu
    */
   private JMenu fileMenu = new JMenu("File");
    /**
    * the default view menu
    */
     private JMenu viewMenu = new JMenu("View");
      /**
    * the default edit menu
    */
       private JMenu editMenu = new JMenu("Edit");
        /**
    * the default help menu
    */
         private JMenu helpMenu = new JMenu("Help");
         //file menu items
         /**
          * create sml menu item
          */
        private JMenuItem createSML = new JMenuItem("Create SML");
         /**
          * read sml menu item
          */
         private JMenuItem readSML = new JMenuItem("Read SML");
          /**
          * compile sml menu item
          */
          private JMenuItem compileSML = new JMenuItem("Compile SML");
           /**
          * quit editor menu item
          */
        private JMenuItem quit = new JMenuItem("Quit");
        //edit menu items
        /**
         * save text menu item
         */
          private JMenuItem saveText = new JMenuItem("SaveText");
           /**
         * clear text menu item
         */
            private JMenuItem clear = new JMenuItem("Clear");
             /**
         * read text menu item
         */
              private JMenuItem readText = new JMenuItem("ReadText");
        //view menu items

        //help menu items

        //control panel components
                               /**
 * these are the current combo boxes that support manipulation of this component frame
 */
private JComboBox logicalFontBox, fontSizeBox,fontStyleBox,colorBox;
/**
 * the default color implementation for this object
 */
private Colors colors = new Colors();
/**
 * the current color name list
 */
private ArrayList<String> colorNames = new ArrayList<String>();
/**
 * the current color objects list
 */
private ArrayList<Color> colorObjects = new ArrayList<Color>();
/**
 * the default logical font names supported by Java
 */
private String logicalFontNames[] = {"Dialog","DialogInput","Monospaced","Serif","SansSerif"};
/**
 * sets the current font style names
 */
private String fontStyleNames[] = {"Plain","Bold","Italic","Bold_Italic"};
/**
 * sets the current color names strings
 */
private String[] colorNamesString;// = new String[colorNames.size()];
/**
 * sets the current font size names
 */
private String[] fontSizeNames = {"10","12","14","16","20","25","30","35","45","60","72","80","90","100","120","130","140","160","200"};
/**
 * the current logical font 
 */
  private String logicalFontName;
  /**
   * the current font size
   */
   private int fontSize;
   /**
    * the current font style
    */
    private int fontStyle;
    /**
     * the default no argument constructor for this object
     * initializes all the graphical components required by this object
     */
public SimpletronInterface()
{
               super("Simpletron Editor Interface");
               setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
               setVisible(true);
               setSize(500,500);
               setLocation(100,100);
                 //add a default system icon
               Image img = Toolkit.getDefaultToolkit().getImage(getClass().getResource("logo.jpg"));
               this.setIconImage(img);  
               //add a window listener
               addWindowListener(new WindowHandler());
               //add components to the frame
               add(new JScrollPane(editorPane),BorderLayout.CENTER);
               editorPane.setToolTipText("This is the default workspace for this implementation.");
               setJMenuBar(topMenuBar);
               topMenuBar.setToolTipText("This is the current menu bar for this implementation.");
               add(controlPanel,BorderLayout.NORTH);
               controlPanel.setToolTipText("This is the default control panel");
               add(statusPanel,BorderLayout.SOUTH);
               statusPanel.setToolTipText("status bar");
               //decorate the panels uniformly
               controlPanel.setBackground(defaultThemeColor);
               statusPanel.setBackground(defaultThemeColor);
               //customize the editor panel
               editorPane.setEditable(true);
               //add menus to the menu bar
               topMenuBar.add(fileMenu);
               topMenuBar.add(editMenu);
               topMenuBar.add(viewMenu);               
               topMenuBar.add(helpMenu);
               //add menu items to the file menu
                  fileMenu.add(createSML);
                  fileMenu.add(readSML);
                   fileMenu.add(compileSML);
                   fileMenu.add(quit);
                   //register listeners for the file menu items                 
                  createSML.addActionListener(new MenuItemHandler());
                    readSML.addActionListener(new MenuItemHandler());
                    compileSML.addActionListener(new MenuItemHandler());
                   quit.addActionListener(new MenuItemHandler());
                   //add menu items to the edit menu
                   editMenu.add(saveText);
                   editMenu.add(clear);
                   editMenu.add(readText);
                   //register action listeners for file menu items
                   saveText.addActionListener(new MenuItemHandler());
                   clear.addActionListener(new MenuItemHandler());
                   readText.addActionListener(new MenuItemHandler());
                   //add the control panel components
                    logicalFontBox = new JComboBox(logicalFontNames);
                   fontSizeBox = new JComboBox(fontSizeNames);
                   fontStyleBox = new JComboBox(fontStyleNames);
                   //update the color names to the colorNamesString array
                   colorNames = colors.getColorNames();
                   colorObjects = colors.getColorObject();
                   colorNamesString = new String[colorNames.size()];                   
                   for(int i=0; i<colorNamesString.length; i++)
                   {
                        colorNamesString[i] = colorNames.get(i);
                   }//end for
                   colorBox = new JComboBox(colorNamesString);                   
                   //add the boxes to the controlPanel
                   controlPanel.add(logicalFontBox);
                    controlPanel.add(fontSizeBox);
                     controlPanel.add(fontStyleBox);
                      controlPanel.add(colorBox);                   
                      //add item isteners to the comboBoxes
                      logicalFontBox.addItemListener(new ItemHandler());
                        fontSizeBox.addItemListener(new ItemHandler());
                          fontStyleBox.addItemListener(new ItemHandler());
                            colorBox.addItemListener(new ItemHandler()); 
                            //set the current font
                            this.setLogicalFontName("Monospace");
                            this.setFontSize(12);
                            this.setFontStyle(Font.PLAIN);
                            Font currentFont = new Font(this.getLogicalFontName(),this.getFontStyle(),this.getFontSize());
                            editorPane.setFont(currentFont);                                             
}//end constructor
/**
 * this inner class handles all actions related to components within the control panel
 * @see simpletron.editor.SimpletronInterface#controlPanel
 */
private class ItemHandler implements ItemListener
{
   // @Override
    /**
     * responds to actions related to items that implement the {@link java.awt.event.ItemListener} 
     */
public void itemStateChanged(ItemEvent evt)
{
     if(evt.getStateChange()==ItemEvent.SELECTED)
     {
       if(evt.getSource()==logicalFontBox)
       {
           setLogicalFontName(logicalFontNames[logicalFontBox.getSelectedIndex()]);
          editorPane.setFont(new Font(getLogicalFontName(),getFontStyle(),getFontSize()));         
       }//end if
       else if(evt.getSource()==fontSizeBox)
       {
           setFontSize(Integer.parseInt(fontSizeNames[fontSizeBox.getSelectedIndex()]));
            editorPane.setFont(new Font(getLogicalFontName(),getFontStyle(),getFontSize()));   
       }//end else if
        else if(evt.getSource()==fontStyleBox)
        {
          switch(fontStyleBox.getSelectedIndex())
          {
                 case 0://plain
                setFontStyle(Font.PLAIN);
                  break;
                  case 1://bold
                   setFontStyle(Font.BOLD);
                  break;
                  case 2://italic
                  setFontStyle(Font.ITALIC);
                  break;
                    case 3://bold italic
                   setFontStyle(Font.BOLD + Font.ITALIC);                   
                  break;
          }//end switch
           editorPane.setFont(new Font(getLogicalFontName(),getFontStyle(),getFontSize())); 
       }//end else if
       else if(evt.getSource()==colorBox)
       {
          try
          {
            Color currentColor = colorObjects.get(colorBox.getSelectedIndex());  
            editorPane.setForeground(currentColor);                   
          }
          catch(IndexOutOfBoundsException e)
          {
              //TODO
            JOptionPane.showMessageDialog(controlPanel, "Null color value", "Null color value", JOptionPane.ERROR_MESSAGE);
          }//end catch
       }
     }//end if the item was selected
}//end method
}//end class
/**
 * this class provides action handling for the menu items in this class
 */
private class MenuItemHandler implements ActionListener
{   
//@Override
/**
 * responds to actions elicited by the menu items
 */
public void actionPerformed(ActionEvent evt)
{
               //file menu items 
    /**
     * quit button
     */
  if(evt.getSource()==quit)
  {
      System.exit(0);
  }//end if
  /**
   * if this is the compileSML button
   */
  else if(evt.getSource()==compileSML)
  {
      File  inputFile = new simpletron.records.file.SMLFile().getOpenFile("Input an SML file");
       File outputFile = new simpletron.records.file.SMLFile().getSaveFile("Save output to a text file");
       Processor processor = new Processor(inputFile,outputFile);
       editorPane.setText(processor.getOutputText());
  }
  /**
   * read sml button
   */
  else if(evt.getSource()==readSML)
  {
       File file = new simpletron.records.file.SMLFile().getOpenFile();//open the current file
       ReadSimpletronRecord readRecord = new ReadSimpletronRecord(file);//read the file
      // ReadSimpletronText rs = new ReadSimpletronText(file);//reaad sml text file
       //append the text to the editor
       editorPane.setText(readRecord.getOutputText());
  }//read sml
  /**
   * create SML button
   */
 else if(evt.getSource()==createSML)
  {
      File file = new simpletron.records.file.SMLFile().getSaveFile();     
     CreateSimpletronRecord createRecord = new CreateSimpletronRecord(editorPane.getText(),file);
     // CreateSimpletronText cs = new CreateSimpletronText(editorPane.getText(),file);//write sml text file
      if(createRecord.getDeleteFlag()==true)//if exceptions are thrown, delete the currently saved file
      {
         boolean delete = file.delete();
//         if(delete == true)
//          System.out.printf("\nfile: %s has been deleted\n",file.getName());
      }
      //System.out.println(" "+editorPane.getText());      
  }//end else if
       //edit menu items
 /**
  * save a text file
  */
 else  if(evt.getSource()==saveText)
  {
     File file = new simpletron.records.file.SMLFile().getSaveFile();
     JEditorPaneOutput output = new JEditorPaneOutput(editorPane,file);
  }//end else if
 /**
  * clear the editor text
  */
    else  if(evt.getSource()==clear)
  {
      editorPane.setText("");
  }//end else if
    /**
     * read text file
     */
    else  if(evt.getSource()==readText)
  {
     File file = new simpletron.records.file.SMLFile().getOpenFile();
     JEditorPaneInput input = new JEditorPaneInput(editorPane,file);           
  }//end else if
}//end method
}//end menu item handler

/**
          * handler window events for this object
          */
         private class WindowHandler implements WindowListener
         {
         /**
          * invoked when the user attempts to close the window
          * @param evt the current window event object
          */
         public void windowClosing(WindowEvent evt)
         {
                        
         }//end method
         /**
          * invoked when the window is set to be the active window
          * @param evt the current window event object
          */
           public void windowActivated(WindowEvent evt)
         {
                   
         }//end method
            /**
           * invoked when the system calls the dispose method
           * @param evt the current window event object
           */    
            public void windowClosed(WindowEvent evt)
         {
                     
         }//end method
             /**
            * invoked when the active window is no longer active
            * @param evt the current window event object
            */  
             public void windowDeactivated(WindowEvent evt)
         {
                     
         }//end method
              /**
             * invoked when a window is maximized
             * @param evt the current window event object
             */  
              public void windowDeiconified(WindowEvent evt)
         {
                 
         }//end method
              /**
               * invoked when the window has been minimized
               * @param evt the current window event object
               */
               public void windowIconified(WindowEvent evt)
         {
                  
         }//end method
               /**
                * invoked when the window is made visible for the first time
                *@param evt the current window event object
                */
             public void windowOpened(WindowEvent evt)
         {
           JOptionPane.showMessageDialog(editorPane, "Welcome to the simpletron interface"+"\n"
                   + "Copyright, Gerry Dreamer Ventures, 2016\nAll rights reserved.","Welcome to the simpletron interface",JOptionPane.INFORMATION_MESSAGE);
         }//end method
         }//end class   
         /**
    * update the current name of the logical font
    * @param s the current font name
    */
   public void setLogicalFontName(String s)
   {
                  this.logicalFontName = s;
   }//end method
   /**
    * this is the default font name accessibility method
    * @return the current logical font name
    */
   public String getLogicalFontName()
   {
       return this.logicalFontName;           
   }//end method
   /**
    * updates the current font size
    * @param i the current font size
    */
   public void setFontSize(int i)
   {
                this.fontSize = i <= 0 ? 0:i;//ensure that the font size is > 0  
   }//end method
   /**
    * provides access to the current font size
    * @return the current font size
    */
   public int getFontSize()
   {
        return this.fontSize;          
   }//end method
   /**
    * sets the current font style
    * @param i the current font style
    */
   public void setFontStyle(int i)
   {             
                this.fontStyle =  i;// (i < 0 || i > 2) ? 0:i;//ensure that the font style is in the range of 0 - 2, otherwise set it to PLAIN
   }//end method
   /**
    * 
    * @return the current font style integer 
    */
   public int getFontStyle()
   {
                return this.fontStyle;  
   }//end method
}//end class
