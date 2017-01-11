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
import javax.swing.JOptionPane;
import simpletron.records.RecordText;

/**
 * This method allows the simulator to display output to the standard output stream
 * @author gerry dreamer
 */
public class OutputStream {
    /**
     * This method prints to the console
     * @param i the current integer to be printed 
     */
     public void writeToConsole(int i)
    {
        System.out.println("Integer: "+i);
    } 
    /**
     * This method prints to the default GUI system
     * @param i the current integer to be printed 
     */
     public void writeToGUI(int i)
    {
        JOptionPane.showMessageDialog(null, "Integer: "+i, "Simpletron Simulator >> Integer Output", JOptionPane.INFORMATION_MESSAGE);
      //  System.out.println("Integer: "+i);
    } 
     /**
      * This method allows a client to print an exception message on the standard SMl simulator output stream
      * this occurs if an anomaly happens during program execution
      * @param e the current exception
      * @see simpletron.exceptions.AccumulatorOverflowException
      * @see simpletron.exceptions.AddressabilityException
      * @see simpletron.exceptions.DivideByZeroException
      * @see simpletron.exceptions.IllegalInputException
      * @see simpletron.exceptions.InvalidOperationCodeException
      * @see simpletron.exceptions.InvalidWordException
      * @see simpletron.exceptions.OutOfMemoryException
      */
   public void writeToGUI(Exception e)
   {
       JOptionPane.showMessageDialog(null, ""+e.getMessage(), "Simpletron Simulator >> Exception Error Output", JOptionPane.ERROR_MESSAGE); 
      // System.out.println("Exception: "+e.getMessage());
   }
    /**
      * This method allows a client to print an exception message on the standard SMl simulator output stream
      * this occurs if an anomaly happens during program execution
      * @param e the current exception
      * @param msg an additional message for the exception
      * @see simpletron.exceptions.AccumulatorOverflowException
      * @see simpletron.exceptions.AddressabilityException
      * @see simpletron.exceptions.DivideByZeroException
      * @see simpletron.exceptions.IllegalInputException
      * @see simpletron.exceptions.InvalidOperationCodeException
      * @see simpletron.exceptions.InvalidWordException
      * @see simpletron.exceptions.OutOfMemoryException
      */
   public void writeToGUI(Exception e, String msg)
   {
      JOptionPane.showMessageDialog(null, ""+e.getMessage()+"\n"+msg, "Simpletron Simulator >> Exception Error Output", JOptionPane.ERROR_MESSAGE); 
       // System.out.println("Exception: "+e.getMessage()+"\n"+msg);
   }
   /**
    * This method allows a client to provide the SMl simulator a string literal, which is to be output in the 
    * standard output stream
    * @param s the current string literal to be output
    */
   public void writeToGUI(String s)
   {
      JOptionPane.showMessageDialog(null, ""+s, "Simpletron Simulator >> String Literal Output", JOptionPane.INFORMATION_MESSAGE);  
     //  System.out.println(""+s);
   }
     /**
      * This method allows a client to print an exception message on the standard SMl simulator output stream
      * this occurs if an anomaly happens during program execution
      * @param e the current exception
      * @see simpletron.exceptions.AccumulatorOverflowException
      * @see simpletron.exceptions.AddressabilityException
      * @see simpletron.exceptions.DivideByZeroException
      * @see simpletron.exceptions.IllegalInputException
      * @see simpletron.exceptions.InvalidOperationCodeException
      * @see simpletron.exceptions.InvalidWordException
      * @see simpletron.exceptions.OutOfMemoryException
      */
   public void writeToConsole(Exception e)
   {
       System.out.println("Simpletron Simulator >> Exception Error Output"+"\n"+e.getMessage()); 
   }
    /**
      * This method allows a client to print an exception message on the standard SMl simulator output stream
      * this occurs if an anomaly happens during program execution
      * @param e the current exception
      * @param msg an additional message for the exception
      * @see simpletron.exceptions.AccumulatorOverflowException
      * @see simpletron.exceptions.AddressabilityException
      * @see simpletron.exceptions.DivideByZeroException
      * @see simpletron.exceptions.IllegalInputException
      * @see simpletron.exceptions.InvalidOperationCodeException
      * @see simpletron.exceptions.InvalidWordException
      * @see simpletron.exceptions.OutOfMemoryException
      */
   public void writeToConsole(Exception e, String msg)
   {
       System.out.println("Simpletron Simulator >> Exception Error Output"+"\n"+e.getMessage()+"\n"+msg); 
   }
   /**
    * This method allows a client to provide the SMl simulator a string literal, which is to be output in the 
    * standard output stream
    * @param s the current string literal to be output
    */
   public void writeToConsole(String s)
   {
      System.out.println("Simpletron Simulator >> Console Output"+"\n"+s); 
   }
   /**
    * This method writes a text file based on a buffered string literal passed on from the client object
    * @param file the current file
    * @param s the current string literal
    * @see simpletron.records.RecordText
    */
   public void writeTextFile(File file, String s)
   {
       RecordText record = new RecordText(file,s);
   }
}
