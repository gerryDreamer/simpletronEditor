/*
 * Copyright (C) 2017 gerry dreamer
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

package simple.records;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.NoSuchElementException;
import javax.swing.JOptionPane;
import simple.register.SMLArray;
import simpletron.records.SimpletronRecord;

/**
 * This object writes the data produced by the simple compiler into a specified *.sml file
 * the data is actually received from an smlArray object
 * @see simple.register.SMLArray
 * @author gerry dreamer
 */
public class WriteDataToSML {
     /**
    * an instance of the SimpletronRecord object
    */
    private SimpletronRecord record;
     /**
     * enables output of the record object in its original serialized form
     */
    private ObjectOutputStream output;
    /**
     * implemented in the output object
     */
    private FileOutputStream fileOutput;
    /**
     * provides access to the storage location of the current set of records
     */
    private File currFile;
    /**
     * the current object containing the compiled SML data
     * refer to: {@link simple.register.SMLArray}
     */
    private SMLArray smlArray;
    /**
     * the default constructor for this object: <br>
     * this method initializes the object instance variables with the parameters given
     * @param smlArray {@link simple.records.WriteDataToSML#smlArray}
     * @param currFile {@link simple.records.WriteDataToSML#currFile}
     */
    public WriteDataToSML(SMLArray smlArray, File currFile)
     {
        this.smlArray = smlArray;       
         try
     {
        this.currFile = currFile;  
        new com.dreamer.file.FileFunctions().validateFileExtension(currFile, "sml");
     }
   catch(com.dreamer.file.exceptions.InvalidFileNameExtensionException e)
     {
          this.currFile = null;//set the file to null
             new simpletron.hardware.OutputStream().writeToGUI(e);
     }//end catch
    }
    /**
     * opens the file destination
     */
    public void openFile()
    {
        try
        {
            fileOutput = new FileOutputStream(currFile);
            output = new ObjectOutputStream(fileOutput);
        }//end try
        catch(IOException e)
        {
         new simpletron.hardware.OutputStream().writeToGUI(e, "Error in locating the specified file");
        }//end catch 
    }
    /**
     * adds records to the file
     */
    public void addRecords()
    {
       //loop through the sml array
        for(int i=0; i<smlArray.getMemorySize(); i++)
        {
        try
        {
          record = new SimpletronRecord(smlArray.getElement(i)); //update the current smlArray element specified by the index i to the record object
          output.writeObject(record);//write the record object
        }//end try
        catch(NumberFormatException e)
        {
            JOptionPane.showMessageDialog(null, "Error in parsing the SML instructions", "Error in parsing the SML instructions", JOptionPane.ERROR_MESSAGE); 
        }
        catch(IOException e)
        {
            JOptionPane.showMessageDialog(null, "Error in locating the specified file", "Error in locating the specified file", JOptionPane.ERROR_MESSAGE); 
        }
        catch(NoSuchElementException e)
        {
            JOptionPane.showMessageDialog(null, "Error in file format", "Error in file format", JOptionPane.ERROR_MESSAGE); 
        } 
        }//end for loop
    }//end method addRecords
    /**
     * closes the file
     */
    public void closeFile()
    {
       try
        {
            if(output != null)
                output.close();
        }
         catch(IOException e)
        {
             new simpletron.hardware.OutputStream().writeToGUI(e, "Error in closing the specified file");
        } 
    }
}
