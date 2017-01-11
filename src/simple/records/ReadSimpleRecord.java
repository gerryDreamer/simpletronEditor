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

package simple.records;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.NoSuchElementException;
/**
 * This object reads a simple file located in the local storage
 * @author gerry dreamer
 */
public class ReadSimpleRecord {
    /**
     * reads input from a file
     */
    private BufferedReader input;
    private FileReader reader;
    /**
     * the current file object specified by the client
     */
    private File currFile;
    /**
     * The default constructor for this object, it initializes the file received from the client
     * @param currFile {@link simple.records.ReadSimpleRecord#currentFile}
     */
    public ReadSimpleRecord(File currFile)
    {
        this.currFile = currFile;
        this.openFile();
        this.readRecords();
        this.closeFile();
    }
    /**
     * opens the current file
     */
    public void openFile()
    {
        try
        {
            reader = new FileReader(currFile);
            input = new BufferedReader(reader);
        }
        catch(IOException e)
        {
            new simpletron.hardware.OutputStream().writeToGUI(e,"Error in locating file");
        }
    }
    /**
     * reads contents of a text file
     */
    public void readRecords()
    {
       simple.records.SimpleRecord record = new simple.records.SimpleRecord();
        System.out.println("Line number statement");
        try
        {
            String lineText = "";
            while(lineText != null)
            {
             lineText = input.readLine();          
              record.setLineStatement(lineText);
              //display record contents
                 if(lineText != null)
                System.out.printf("%s\n",record.getLineStatement());
            }//end while
        }
        catch(IOException e)
        {
              new simpletron.hardware.OutputStream().writeToGUI(e,"error reading from file");
        }
        catch(NoSuchElementException e)
        {
              new simpletron.hardware.OutputStream().writeToGUI(e,"File improperly formed");
        }
    }
    /**
     * close the current file
     */
    public void closeFile()
    {
      try
      {
            if(input != null)
            input.close();
      }
      catch(IOException e)
      {
          
      }
    }
}
class ReadSimpleRecordTest
{
    public static void main(String[] args) {
          File f = new File("E:\\project de java\\simple.txt");
        //File f = new simpletron.records.file.SMLFile().getOpenFile();        
        ReadSimpleRecord r = new ReadSimpleRecord(f);
    }
}