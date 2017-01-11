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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;
import javax.swing.JOptionPane;

/**
 * This method provides capabilities for creating a simpletron record object of type *.sml 
 * @author gerry dreamer
 */
public class CreateSimpletronRecord {
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
    private File currentFile;
    /**
     * a string representation of the current instructions 
     */
    private String currentText;
    /**
     * this flag indicates whether the current file should be deleted
     */
    private boolean deleteFlag;
    /**
     * This constructor receives a text representation of the SML instructions
     * and the default file save location
     * @param text the current string of instructions
     * @param file the default file save location
     * @see simpletron.records.CreateSimpletronRecord#addRecords() 
     * @see simpletron.records.CreateSimpletronRecord#openFile() 
     * @see simpletron.records.CreateSimpletronRecord#closeFile() 
     */
    public CreateSimpletronRecord(String text, File file)
    {
        currentFile = file;
        currentText = text;
        //call the default methods for output processing
        openFile();
        addRecords();
        closeFile();
    }//end args constructor
    /**
     * open the default file save operations
     */
    public void openFile()
    {
        try
        {
            fileOutput = new FileOutputStream(currentFile);
            output = new ObjectOutputStream(fileOutput);
        }//end try
        catch(IOException e)
        {
          JOptionPane.showMessageDialog(null, "Error in locating the specified file", "Error in locating the specified file", JOptionPane.ERROR_MESSAGE);
        }//end catch
    }//end method
    /**
     * add the current instructions to the output file
     */
    public void addRecords()
    {
     StringTokenizer token  = new StringTokenizer(this.currentText);//tokenize the instruction string
     int word = 1000;//the current sml word 
     while(token.hasMoreTokens())
     {
        try
        {
            String currentToken = token.nextToken();//an instance of the current string token
            word = Integer.parseInt(currentToken);//an instance of a the <code>currentToken</code> in integer format
            record = new SimpletronRecord(word);   //initialize the record constructor instance with the current word       
            output.writeObject(record);//write the new reord object
           // System.out.println(""+record.getWord());
        }//end try
        catch(NumberFormatException e)
        {
            JOptionPane.showMessageDialog(null, "Error in parsing the SML instructions", "Error in parsing the SML instructions", JOptionPane.ERROR_MESSAGE); 
           // System.exit(1);
            this.setDeleteFlag(true);//indicate to the client that the current file should be deleted   
        }
        catch(IOException e)
        {
            JOptionPane.showMessageDialog(null, "Error in locating the specified file", "Error in locating the specified file", JOptionPane.ERROR_MESSAGE); 
        }
        catch(NoSuchElementException e)
        {
            JOptionPane.showMessageDialog(null, "Error in file format", "Error in file format", JOptionPane.ERROR_MESSAGE); 
        }
     }//end while
    }
    /**
     * close the current file
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
            JOptionPane.showMessageDialog(null, "Error in closing the specified file", "Error in closing the specified file", JOptionPane.ERROR_MESSAGE);
           // System.exit(1);
        }
    }
    /**
     * this method sets the current indicator that the current file should be deleted
     * @param f a true or a false state
     */
    public void setDeleteFlag(boolean f)
    {
        this.deleteFlag = f;
    }
    /**
     * a getter method for the delete flag
     * @return the current delete file flag
     */
    public boolean getDeleteFlag()
    {
        return this.deleteFlag;
    }
}
