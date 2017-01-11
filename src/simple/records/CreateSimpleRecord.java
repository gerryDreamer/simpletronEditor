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
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Formatter;
import java.util.FormatterClosedException;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * This object creates a simple record, which produces a file *.simple
 * This object formats the file to plain text
 * @author gerry dreamer
 */
public class CreateSimpleRecord {
    /**
     * this object allows for creation of a text file based on the {@link simple.records.SimpleRecord} object
     */
    private Formatter output;
     /**
     * provides access to the storage location of the current set of records
     */
    private File currentFile;
    /**
     * initializes the file received from the client, and the default methods required by this object
     * @param currFile a file received from the user
     */
    public CreateSimpleRecord(File currFile)
    {
        this.currentFile = currFile;
        this.openFile();
        this.addRecords();
        this.closeFile();
    }
   /**
    * opens the current file
    */
    public void openFile()
    {
      try
      {
          output = new Formatter(this.currentFile);
      }
      catch(FileNotFoundException e)
      {
         new simpletron.hardware.OutputStream().writeToGUI(e, "Error in locating the file");
      }
    }
    /**
     * adds records to the file
     */
    public void addRecords()
    {
      simple.records.SimpleRecord record = new simple.records.SimpleRecord();
      Scanner input = new Scanner(System.in);
      int sentinel = 0;
        System.out.printf("%s\n","Please enter a simple file: and a sentinel");
        while(sentinel != -1)
        {
            try
            {
                record.setLineStatement(input.nextLine());
                sentinel = input.nextInt();
                output.format("%s\n", record.getLineStatement());
            }
            catch(FormatterClosedException e)
            {
               new simpletron.hardware.OutputStream().writeToGUI(e, "Error in wrting to the file");  
               return;
            }
            catch(NoSuchElementException e)
            {
                 new simpletron.hardware.OutputStream().writeToGUI(e, "Invalid input, please try again");
                 input.nextLine();
            }
               System.out.printf("%s\n","Please enter a simple file: and a sentinel");//repeat loop
        }//end while
    }
    /**
     * closes the file
     */
    public void closeFile()
    {
        if(output != null)
            output.close();
    }
}
class CreateSimpleRecordTest
{
    public static void main(String[] args) {
        File f = new simpletron.records.file.SMLFile().getSaveFile();
        CreateSimpleRecord c = new CreateSimpleRecord(f);
    }
}