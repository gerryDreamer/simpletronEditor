/**
*
 * 
 *
 *
 * (C) Copyright 2013-2016, by Gerry Dreamer Ventures.
 *
 * Project Info:  http://www.facebook.com/Dreamer Ventures
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
 * License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 *
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 *
 * (C)copyright 2013-2016, by Gerry Dreamer Ventures.
 *
 * Original Author:  Gerry Dreamer;
 * Contributor(s):   Gerry Dreamer Ventures;
 *
 * 
 *
 * Changes
 * -------
 * 
 * 11-09-2016 : final compilation of the first program.
 *
*
**/
package simpletron.sml.old;

/**
 *
 * @author gerry dreamer
 */
 /**
     * this class provides access to SML messages, and other necessary notifications
  */
public class Messages {
    //--------------------------messages--------------------------------------------------------------------------------------
    /**
     * an array string representation of the SML welcome message.
     */
    protected static  String[] welcomeMessage = {"Welcome to Simpletron!",
        "Please enter your program one instruction",
        "(or data word) at a time into the input",
        "text field. I will display the location.",
        "number and question mark (?). You then",
        "type the word for that location. Press the",
        "Done button to stop entering your program."};
    /**
     * an array string representation of the SML arithmetic exception message.
     */
    protected static String[] exception = {"Attempt to divide by zero",
    "Simpletron execution abnormally terminated."};
    /**
     * an array string representation of the SML program termination message.
     */
    protected static String[] terminationMessage = {"Program loading completed",
          "Program execution ends.","Simpletron execution terminated."};
    /**
     *  this method prints the current memory in a dump format.
     * @param memory the current SML memory
     * @param accumulator the virtual Arithmetic unit
     * @param instructionCounter the current memory index
     * @param instructionRegister the current memory index
     * @param operationCode the leftmost two digits, the instruction code
     * @param operand the rightmost two digits,memory indices.
     * @return a string representation of the memory dump 
     */
       protected static String memoryDump(int[] memory, int accumulator, int instructionCounter, int instructionRegister, int operationCode, int operand)
       {
         StringBuilder temp = new StringBuilder();
         temp.append(String.format("%2s%20s\n","REGISTERS:",""));
           temp.append(String.format("%-20s%7s%s%04d\n","accumulator","",accumulator < 0 ? "-":"+",Math.abs(accumulator)));
           temp.append(String.format("%-20s%7s%02d\n","instructionCounter","",instructionCounter));
           temp.append(String.format("%-20s%7s%s%04d\n","instructionRegister","",instructionRegister < 0 ? "-":"+",Math.abs(instructionRegister)));
           temp.append(String.format("%-20s%7s%02d\n","operationCode","",operationCode));
           temp.append(String.format("%-20s%7s%02d\n","operand","",operand));
           temp.append(String.format("%-5s","MEMORY:"));
           //print the memory contents
           temp.append(String.format("\n"));
           temp.append(String.format("%-8s",""));  
            for(int i=0; i<10;++i)
            {
            temp.append(String.format("%-11d",i));
            }//end  for
             temp.append(String.format("\n"));//create a newline
               for(int i=0; i<memory.length; ++i){ 
            if(i%10==0)
            {
             //   System.out.printf(" %-5d\n",i);
                temp.append(String.format("\n"));//create a newline
            }
             temp.append(String.format("%-5s%s%04d ","",memory[i] < 0 ? "-":"+",Math.abs(memory[i])));
        }//end for
       temp.append(String.format("\n"));
         return temp.toString();
       }//end method memory dump
}
