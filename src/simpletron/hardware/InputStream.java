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

package simpletron.hardware; 
import java.io.File;
import java.util.Scanner;
import javax.swing.JOptionPane;
import simpletron.exceptions.InvalidWordException;
import simpletron.exceptions.OutOfMemoryException;
import simpletron.records.ReadRecordToSML;

/**
 * This method attempts to read from a default input stream, as specified by the simulator
 * @author gerry dreamer
 */
public class InputStream {   
     /**
      * This method enables the simulator to read from a console
      * @return an integer value from the input stream
      * @throws NumberFormatException if an invalid number format is provided
      */
    public int readFromConsole() throws NumberFormatException
    {
        return new Scanner(System.in).nextInt();
    }
    /**
     * This method enables the simulator to read from a console (default implementation)
     * @return an integer value from the input stream
     * @throws NumberFormatException if an invalid number format is provided
     */
    public int readFromGUI() throws NumberFormatException
    {
        return Integer.parseInt(JOptionPane.showInputDialog("Please input an integer: "));
       // System.out.println("Please input an integer: ");
        //return new java.util.Scanner(System.in).nextInt();
    }
    /**
      * This method allows an SML client object to read values from a file and append them directly into memory
     * This abstraction is necessary as it helps the client in reducing the effort of creating many declarations
     * it also promotes code reuse
     * @param currFile the current file received by this object
     * @param instrRegister the current register for this object
     * @param instrCounter the current index redirecting input to specific locations
     * @param memory the current memory object from the Processor
     * @param outputStream the current outputStream object
     * @throws OutOfMemoryException {@link simpletron.exceptions.OutOfMemoryException}
     * @throws InvalidWordException {@link simpletron.exceptions.InvalidWordException}
     * @throws NullPointerException {@link java.lang.NullPointerException}
     * @see simpletron.records.ReadRecordToSML
     * @see simpletron.records.ReadRecordToSML#openSMLRecords() 
     * @see simpletron.records.ReadRecordToSML#updateRecordsToMemory() 
     * @see simpletron.records.ReadRecordToSML#closeSMLFile() 
     */
    public void readFromFile(File currFile, InstructionRegister instrRegister,InstructionCounter instrCounter, Memory memory,OutputStream outputStream)
            throws OutOfMemoryException,InvalidWordException,NullPointerException
    {
        //create a call to the ReadRecordToSML object
        ReadRecordToSML readRecord = new ReadRecordToSML(currFile,instrRegister,instrCounter,memory,outputStream);//initialize the ReadRecordToSML constructor
        readRecord.openSMLRecords();//open the file
        readRecord.updateRecordsToMemory();//update the file contents to memory
        readRecord.closeSMLFile();//close the file
    }
}//end class
