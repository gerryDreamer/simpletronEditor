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
     * this class provides the functions needed for execution of SML programs.
     */
public class FunctionClass {    
    /**
     * this method prints the SML welcome message.
     * @param instruct the current sequence of messages to be printed across the screen.
     */
    public static void message(String[] instruct){
        StringBuilder temp = new StringBuilder();       
        String asterisk = "***";
        for(String element: instruct)//loop thru the string array
        temp.append(String.format("%s %s %s\n",asterisk,element,asterisk));
        //print the message
        System.out.println(temp.toString());   
    }//end method
    /**
     * this method fills an array with the value specified.
     * @param array a copy of the current array
     * @param value a copy of the value to be used.
     */
    public static void fillArray(int[] array, int value){
       for(int element: array)
           element = value;
      }
    /**
     *  this method prints the current memory in a dump format.
     * @param memory the current SML memory
     * @param accumulator the virtual Arithmetic unit
     * @param instructionCounter the current memory index
     * @param instructionRegister the current memory index
     * @param operationCode the leftmost two digits, the instruction code
     * @param operand the rightmost two digits,memory indices.
     */
   public static void printMemoryDump(int[] memory, int accumulator, int instructionCounter, int instructionRegister, int operationCode, int operand){
        System.out.printf("%2s%20s\n","REGISTERS:","");
        System.out.printf("%-20s%7s+%04d\n","accumulator","",accumulator);
        System.out.printf("%-20s%7s%02d\n","instructionCounter","",instructionCounter);
        System.out.printf("%-20s%7s+%04d\n","instructionRegister","",instructionRegister);
        System.out.printf("%-20s%7s%02d\n","operationCode","",operationCode);
        System.out.printf("%-20s%7s%02d\n","operand","",operand);
        System.out.printf("%-5s","MEMORY:");
        //print the memory contents
        System.out.println("");
        System.out.printf("%-8s","");
        for(int i=0; i<10;++i){
            System.out.printf("%-11d",i);
        }//end  for
       System.out.println("");//create a newline 
        for(int i=0; i<memory.length; ++i){ 
            if(i%10==0)
            {
             //   System.out.printf(" %-5d\n",i);
                System.out.println("");
            }
       System.out.printf("%-5s%s%04d ","",memory[i] < 0 ? " ":"+",memory[i]);           
        }//end for
        System.out.println("");
   }
    /**
    * a recursive method that performs exponential calculations
    * @param base base value
    * @param exp exponent
    * @return the exponential value of the <code>base</code> and <code>exp</code>
    * in the form <pre> <code>base<sup>exp</sup></code></pre>
    */
   public static int exponential(int base, int exp){
        if(exp == 1)
            return base;
        else
        {
            return base * exponential(base, exp - 1);
        }
    }
   /**
    * 
    * @param word the current SML word
    * @return returns the current SML instruction
    */
   public static int breakOperationCode(int word){
       int un,dix,cent,mille;
        un = word%10;
        dix = (word%100 - un)/10;
        cent = (word%1000 -dix)/100;
        mille = (word%10000-cent)/1000;
        String first = String.format("%s%s",mille,cent);
        return Integer.parseInt(first);
   }
   /**
    * 
    * @param word  the current SML word
    * @return  returns the current SML memory index.
    */   
   public static int breakOperand(int word){
        int un,dix,cent,mille;
        un = word%10;
        dix = (word%100 - un)/10;
        cent = (word%1000 -dix)/100;
        mille = (word%10000-cent)/1000;
        String last = String.format("%s%s",dix,un);
        return Integer.parseInt(last);
   }
   
}
