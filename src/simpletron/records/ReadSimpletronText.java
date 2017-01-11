/*
 * Copyright (C) 2016 gerry dreamer
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

package simpletron.records;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.NoSuchElementException;
import java.util.Scanner;
import javax.swing.JOptionPane;

/**
 * This class enables the reading of an SML object file
 * @author gerry dreamer
 */
public class ReadSimpletronText {
    /**
    * an instance of the SimpletronRecord object, a template for reading the SML file object
    */
    private SimpletronRecord record;
     /**
     * enables input of the record object in its original serialized form
     */
    private Scanner input;
    /**
     * the current file object specified by the client
     */
    private File currentFile;
    /**
     * a buffer that formats the word representations from the file object and renders them as a string file
     */
    private StringBuilder textOutput = new StringBuilder(); 
     /**
     * this constructor initializes a new object of this type,
     * which reads contents of an *.sml file and provides a string representation
     * of the file contents, which are serialized
     * @param file the current file object
     * @see simpletron.records.ReadSimpletronRecord#getOutputText()
     * @see simpletron.records.ReadSimpletronRecord#openFile()
     * @see simpletron.records.ReadSimpletronRecord#readRecords()
     * @see simpletron.records.ReadSimpletronRecord#closeFile()
     */
    public ReadSimpletronText(File file)
    {
        this.currentFile = file;//initialize the location of the current file
        openFile();
        readRecords();
        closeFile();        
    }
    /**
     * opens the current file specified by the client
     * @see simpletron.records.ReadSimpletronRecord#currentFile
     */
    public void openFile()
    {
        try
        {
           input = new Scanner(currentFile);
        }//end try
        catch(FileNotFoundException e)
        {
             JOptionPane.showMessageDialog(null, "Error in locating the specified file", "Error in locating the specified file", JOptionPane.ERROR_MESSAGE); 
        }//end catch
    }
    /**
     * this method reads the current file contents, only if they are of type *.sml
     */
    public void readRecords()
    {        
        record = new SimpletronRecord();
        //create a custom header for the SML records
        textOutput.append(String.format("%s\n%s\n","SIMPLETRON MACHINE LANGUAGE","OBJECT FILE OUTPUT"));
        //receive input from the file and append the words to the string buffer
        try
        {
          while(input.hasNext())
          {
           //append the record contents
              record.setWord(input.nextInt());
           textOutput.append(String.format("%d\n",record.getWord()));
          }//end while
        }//end try
        catch(NoSuchElementException e)
        {
            JOptionPane.showMessageDialog(null, "File improperly formed", "File improperly formed", JOptionPane.ERROR_MESSAGE); 
        }//end catch
        catch(IllegalStateException e)
        {
             JOptionPane.showMessageDialog(null, "Error in locating the class object", "Error in locating the class object", JOptionPane.ERROR_MESSAGE); 
        }//end catch
    }
    /**
     * this method closes the current file
     */
    public void closeFile()
    {
       if(input != null)
                input.close();
    }
    /**
     * This method provides a string representation of of the SML *.sml object contents
     * @return a string representation of the sml instructions
     */
    public String getOutputText()
    {
        return this.textOutput.toString();
    }
}//end class
