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
import java.util.Formatter;
import java.util.FormatterClosedException;
import java.util.IllegalFormatException;
import simpletron.hardware.OutputStream;

/**
 * This object enables a client object to actually write contents of a buffered string literal to a text file 
 * of type <em>*.txt</em>
 * @author gerry dreamer
 */
public class RecordText {
    /**
     * writes the string contents to a file
     */
    private Formatter output;
    /**
     * the current string received from the client
     */
    private String currText;
    /**
     * the destination file for the <code>output</code> format
     */
    private File currFile;
    /**
     * an OutputStream of the InputStream object
     */
    private OutputStream outputStream;
    /**
     * initializes the <code>currFile</code> and the <code>currText</code> instance variables
     * @param file the destination file for the text output
     * @param s the text to be output
     * @see simpletron.records.RecordText#currFile
     * @see simpletron.records.RecordText#currText
     * @see simpletron.records.RecordText#openFile() 
     * @see simpletron.records.RecordText#addRecords() 
     * @see simpletron.records.RecordText#close() 
     */
    public RecordText(File file, String s)
    {
       this.currFile = file;
       this.currText = s;
       outputStream = new OutputStream();
       openFile();
       addRecords();
       close();
    }
    /**
     * open the destination file
     */
    public void openFile()
    {
          try
        {
          output = new Formatter(currFile);
        }//end try
        catch(FileNotFoundException e)
        {
         getOutputStream().writeToGUI(e, "Error in locating the file");
        }//end catch
        catch(SecurityException e)
        {
        getOutputStream().writeToGUI(e, "Cannot access write permissions for the given file.");
        }//end catch
         catch(NullPointerException e)
    {
         outputStream.writeToGUI(e,"The file received is either invalid or null.");
    }
    }
    /**
     * write the string literal into the given file
     */
    public void addRecords()
    {
        try
        {
           output.format("%s", currText);//output the text to a file
        }//end try
        catch(NumberFormatException e)
        {
          getOutputStream().writeToGUI(e, "rror in parsing the SML instructions");
        }
        catch(FormatterClosedException e)
        {
             getOutputStream().writeToGUI(e, "The formatter has been closed temporarily");
        }
        catch(IllegalFormatException e)
        {
            getOutputStream().writeToGUI(e, "Illegal String lietral format error"); 
        }
         catch(NullPointerException e)
        {
             outputStream.writeToGUI(e,"The file received is either invalid or null.");
        }
    }
    /**
     * close the output stream
     */
    public void close()
    {
          if(output != null)
                output.close();
    }
     /**
     * 
     * @return an instance of the current OutputStream object
     */
    public OutputStream getOutputStream()
    {
        return this.outputStream;
    }
}
