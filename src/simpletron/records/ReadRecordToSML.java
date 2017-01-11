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

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import simpletron.exceptions.InvalidWordException;
import simpletron.exceptions.OutOfMemoryException;
import simpletron.hardware.InstructionCounter;
import simpletron.hardware.InstructionRegister;
import simpletron.hardware.Memory;
import simpletron.hardware.OutputStream;

/**
 * This object parses an SML file and then passes the values to the memory
 * @author gerry dreamer
 */
public class ReadRecordToSML {
    /**
    * an instance of the SimpletronRecord object, a template for reading the SML file object
    */
    private SimpletronRecord record;
     /**
     * enables input of the record object in its original serialized form
     */
    private ObjectInputStream input;
     /**
     * implemented in the input object
     */
    private FileInputStream fileInput;
    /**
     * the current file object specified by the client
     */
    private File currFile; 
     /**
     *  an instance of the <code>InstructionRegister</code> object
     */
    private InstructionRegister instrRegister = new InstructionRegister();
    /**
     *  an instance of the <code>InstructionCounter</code> object
     */
    private InstructionCounter instrCounter = new InstructionCounter();
     /**
     *  an instance of the <code>Memory</code> object
     */
    private Memory memory = new Memory();
    /**
     * this value indicates to the client method that the input received should stop at this point
     */
    private final int SENTINEL = -99999;
     /**
     *  an instance of the <code>OutputStream</code> object
     */
    private OutputStream outputStream = new OutputStream();
    /**
     * This is the default multiple argument constructor for this object,
     * all the hardware components are initialized here
     * @param currFile the current file received by this object
     * @param instrRegister the current register for this object
     * @param instrCounter the current index redirecting input to specific locations
     * @param memory the current memory object from the Processor
     * @param outputStream an value of the OutputStream object
     */
    public ReadRecordToSML(File currFile, InstructionRegister instrRegister,InstructionCounter instrCounter, Memory memory,OutputStream outputStream)
    {
     this.currFile = currFile;
     this.instrCounter = instrCounter;
     this.instrRegister = instrRegister;
     this.memory = memory;
     this.outputStream = outputStream;
    }//end default constructor
    /**
     * this method opens the current SML file 
     * @see simpletron.records.ReadRecordToSML#currFile
     */
    public void openSMLRecords()
    {
        try
        {
           fileInput = new FileInputStream(currFile);
            input = new ObjectInputStream(fileInput);
        }
        catch(IOException e)
        {
           outputStream.writeToGUI(e, "Error in locating the specified file");
        }//end catch
         catch(NullPointerException e)
        {
             outputStream.writeToGUI(e,"The file received is either invalid or null.");
        }
    }//end method
    /**
     * this method reads contents from the specified file and updates the records to memory
     * @see simpletron.records.ReadRecordToSML#input
     * @see simpletron.records.ReadRecordToSML#record
     * @see simpletron.records.ReadRecordToSML#memory
     * @see simpletron.records.ReadRecordToSML#instrCounter
     * @see simpletron.records.ReadRecordToSML#openSMLRecords() 
     * @see simpletron.records.ReadRecordToSML#SENTINEL
     * @see simpletron.records.ReadRecordToSML#instrRegister
     * @throws OutOfMemoryException if the instructions given surpass the SML simulator memory
     * @throws InvalidWordException if an invalid instruction word is received from the user/file
     */
    public void updateRecordsToMemory() throws OutOfMemoryException,InvalidWordException
    {
        try
        {
            /**
             * read the current file and output the contents to memory
             */
         while(true)
         {
             record = (SimpletronRecord) input.readObject();//cast the SimpletronRecord     
              /**
               * enforce integrity, ensure that the word is valid
              */
             if(instrRegister.getInstructionRegister() <-9999 || instrRegister.getInstructionRegister() > 9999)
             {
                  throw new InvalidWordException("An invalid instruction or a data word has been encountered");
             }//throw an exception if the word is invalid
             else//if the word is valid, append it to the instructions register
             {
                  instrRegister.setInstructionRegister(record.getWord());//set the current word read to the Register
             }//end else
             /**
              * if the register is not equal to the sentinel 
              */
             if(instrRegister.getInstructionRegister() != SENTINEL)//do not add the sentinel to memory append only valid data and instructions
             {
                 memory.add(instrRegister.getInstructionRegister(), instrCounter.getInstructionCounter());
             }//end if
             else//if the sentinel is reached, return to the method caller
             {
                 return;//do not read any further values
             }
             /**
               * if the instructions are too big for the memory, throw an out of memory error
               */
              if(instrCounter.getInstructionCounter() >= memory.getMemorySize())
              {
                  throw new OutOfMemoryException("The instructions provided are too big for the memory.");
              }//end if
             instrCounter.setInstructionCounter(instrCounter.getInstructionCounter() + 1);//increment the counter
         }//end while
        }
          catch(EOFException e)
        {
            return;//end of file
        }//end catch
        catch(ClassNotFoundException e)
        {
            outputStream.writeToGUI(e, "Error in locating the class object");
        }//end catch
        catch(IOException e)
        {
           outputStream.writeToGUI(e, "Error in reading the specified file");
        }//end catch
    }//end method 
    /**
     * closes the currently read SML file
     */
    public void closeSMLFile()
    {
       try
        {
            if(input != null)
                input.close();
        }
        catch(IOException e)
        {
           outputStream.writeToGUI(e, "Error in closing the specified file");
        }//end catch  
       //set the instruction counter and registers to zero values after processing is 
       instrCounter.setInstructionCounter(0);
       instrRegister.setInstructionRegister(0);
    }//end method
}
