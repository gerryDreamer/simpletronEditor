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
 * ------------
 * IOUtils.java
 * ------------
 * (C)copyright 2002-2004, by Gerry Dreamer Ventures.
 *
 * Original Author:  Gerry Dreamer;
 * Contributor(s):   Gerry Dreamer Ventures;
 *
 * $Id: IOUtils.java,v 1.8 2009/01/22 08:34:58 taqua Exp $
 *
 * Changes
 * -------
 * 
 * 11-09-2016 : final compilation of the first program.
 *
*
**/
package simpletron.sml.old;
import java.io.File;
import java.util.Scanner;
import static simpletron.sml.old.FunctionClass.exponential;
import static simpletron.sml.old.FunctionClass.fillArray;
import static simpletron.sml.old.FunctionClass.message;
import static simpletron.sml.old.FunctionClass.printMemoryDump;
import static simpletron.sml.old.Instructions.ADD;
import static simpletron.sml.old.Instructions.BRANCH;
import static simpletron.sml.old.Instructions.BRANCHNEG;
import static simpletron.sml.old.Instructions.BRANCHZERO;
import static simpletron.sml.old.Instructions.DIVIDE;
import static simpletron.sml.old.Instructions.EXPONENTIATION;
import static simpletron.sml.old.Instructions.HALT;
import static simpletron.sml.old.Instructions.LOAD;
import static simpletron.sml.old.Instructions.MAX;
import static simpletron.sml.old.Instructions.MIN;
import static simpletron.sml.old.Instructions.MODULUS;
import static simpletron.sml.old.Instructions.MULTIPLY;
import static simpletron.sml.old.Instructions.NEW_LINE;
import static simpletron.sml.old.Instructions.READ;
import static simpletron.sml.old.Instructions.STORE;
import static simpletron.sml.old.Instructions.SUBTRACT;
import static simpletron.sml.old.Instructions.WRITE;
import static simpletron.sml.old.Messages.exception;
import static simpletron.sml.old.Messages.memoryDump;
import static simpletron.sml.old.Messages.terminationMessage;
import static simpletron.sml.old.Messages.welcomeMessage;

/**
 * this object provides an implementation for the SML simulator
 * @author gerry dreamer
 */
/**
     * this program allows simulation of the SML language using Java.
     */
public class SimpletronConsole {
      //------------------------------------instance variables--------------------------------------------------------
    private int operand;//the last 2 digits of an SML word, designate the desired memory location
    private int operationCode;//1st 2 digits, contain the instructions.
    private int[] memory = new int[100];//represents a one dimensional array with 100 elements
    // to rep. the memory
   //-------------------------------accumulator registers---------------------------------------------------------------------
    private int accumulator = 0;//rep. the accumulator register
    private int instructionRegister = 0;//executes instructions before passing them to the memory.
    private int instructionCounter = 0;//tells the loc of the next instruction to be performed
    //----------------------------------main program execution-----------------------------------------------------------------------------
    public SimpletronConsole(){
      message(welcomeMessage); //begin the execution with a message
       fillArray(memory,0);//initialize all the registers to zero
      try{
           instructionCycle();  //the current flow of instructions in the memory. 
            printMemoryDump(memory, accumulator, instructionCounter, instructionRegister, getOperationCode(), getOperand());
        }
      catch(ArithmeticException e){
       message(exception);
       printMemoryDump(memory, accumulator, instructionCounter, instructionRegister, getOperationCode(), getOperand());
      }
    }//end default constructor
    /**
     * this method performs execution of the program from the first to the last instruction.
     */
    public void instructionCycle() throws ArithmeticException{
          Scanner input = new Scanner(System.in);//input variable.
        int i = 0;
        int word = 0;
        int currCounter = 0;
        int initialCounter = 0;
        int sentinel = -9999;
        boolean done = false;
        // System.out.printf("%02d %2s",instructionCounter," ? ");
        do
        {
              System.out.printf("%02d %2s",i," ? ");
             word = input.nextInt(); 
             if(word != sentinel)//do not add the sentinel to the loop
                memory[i] = word;
            i++;
        }
        while(word != sentinel);
     //execute     
        //find end index 
        int endIndex = new com.dreamer.search.SearchArray().linearSearch(memory, +4300);
       do{           
              instructionRegister = memory[instructionCounter];            
             setOperationCode(instructionRegister/100);//leftmost 2 digits
             setOperand(instructionRegister%100);//rightmost 2 digits 
           switch(getOperationCode()){
           case READ:              
               double readVal;
               do{
                   System.out.print("Enter an integer: ");
                   readVal  = input.nextDouble();
               }while(readVal < -9999 || readVal> 9999);
               memory[getOperand()] =(int) readVal;//cast an Integer to the floating point val
               // instructionCounter++;
               break;
           case WRITE:
                   System.out.printf("%s%4d\n",memory[getOperand()] < 0 ? " ":"+" , memory[getOperand()]);
                   if(done==false)
//               instructionCounter++;
//                   else 
//                 instructionCounter = currCounter;      
               break;
             case ADD:
               accumulator += (int) memory[getOperand()];
               // instructionCounter++;
               break;
             case MAX:
                int temp = 0;
                int a = accumulator;
                int b = memory[getOperand()];
              //temp = ( (a + b) + (Math.abs( (a - b ) )) ) / 2;
                temp = Math.max(a, b);
                accumulator = temp;
               //  instructionCounter++;
                 break;
             case MIN:
                  int temp1 = 0;
                int a1 = accumulator;
                int b1 = memory[getOperand()];
              // temp1 = ( (a1 - b1) + (Math.abs( (a1 - b1 ) )) ) / 2;
                temp1 = Math.min(a1, b1);
                accumulator = temp1;
                // instructionCounter++;
                 break;
             case SUBTRACT:
                 accumulator -= (int) memory[getOperand()];
                //  instructionCounter++;
                 break;
             case DIVIDE:
                 accumulator /= (int) memory[getOperand()];
                 // instructionCounter++;
                 break;
             case MULTIPLY:
                 accumulator *=(int)  memory[getOperand()];
                //  instructionCounter++;
                 break;
             case MODULUS:
                  accumulator %= (int) memory[getOperand()];
                 //  instructionCounter++;
              break;  
             case EXPONENTIATION:
            accumulator = (int) exponential(accumulator,memory[getOperand()]);
           //  instructionCounter++;
                 break;
                 //conditional operators
           case LOAD:
               accumulator = (int) memory[getOperand()];
              //  instructionCounter++;
               break;  
           case STORE:
           memory[getOperand()] = accumulator; 
          //  instructionCounter++;
               break;
              //transfer of control operations
           case BRANCH: //similar to the goto function in C/C++
               System.out.println("curr counter: "+instructionCounter);
               initialCounter = instructionCounter;
               currCounter = instructionCounter + 1;
              instructionCounter = getOperand();
               System.out.println("branching...");
                 System.out.println("final counter: "+instructionCounter);
                 //set the conter to the initial counter
                 //instructionCounter = currCounter + 1;
                 done = true;
           //   instructionRegister = memory[getOperand()+1];             
            //  instructionCounter++;
               break;
           case BRANCHNEG:
                initialCounter = instructionCounter;
               currCounter = instructionCounter + 1;
                if(accumulator < 0)
                {
                     instructionCounter = getOperand(); 
                      System.out.println("branching negatives...");
                 System.out.println("final counter: "+instructionCounter);
                     done = true;
                }
               break;
           case BRANCHZERO:
                initialCounter = instructionCounter;
               currCounter = instructionCounter + 1;
               if(accumulator == 0)
               {
                    instructionCounter = getOperand();
                     System.out.println("branching zeros...");
                 System.out.println("final counter: "+instructionCounter);
                    done = true;
                }
               break;
           case NEW_LINE:
               System.out.println("");
            //   instructionCounter++;
               break;
           case HALT:
               message(terminationMessage);
              // System.out.println(""+memoryDump(memory, accumulator, instructionCounter, instructionRegister, getOperationCode(), getOperand()));
               File file = new File("console.txt");
              new simpletron.hardware.OutputStream().writeTextFile(file, memoryDump(memory, accumulator, instructionCounter, instructionRegister, getOperationCode(), getOperand())); ;
               break;
       }//end switch 
      if(getOperationCode() != BRANCH && getOperationCode() != BRANCHNEG && getOperationCode() != BRANCHZERO)//end 
      {
          if(done == false)
          {
               instructionCounter++;
               //set done to false
              // done = false;
          }              
          else
          {
               
               if(currCounter == endIndex)
               {
                 //do nothing if the next instruction after branch is HALT 
               instructionCounter = currCounter;
               }
               else
               {
                 //increment the counter 
                  //  instructionCounter++;
                   instructionCounter = initialCounter + 1;
                   done = false;
               }
          }
      }
//      if(instructionCounter == endIndex)
//          return;
       }//end do
       while(instructionCounter != endIndex);//instructionRegister != 4300  //getOperationCode() != HALT
    }   
   /**
    * sets the leftmost two digits of a word
    * @param x the current operation code
    */
   public void setOperationCode(int x){
       operationCode = x;
   }
   /**
    * 
    * @return the current operation code
    */
   public int getOperationCode(){
       return operationCode;
   }
   /**
    * sets the rightmost two digits of a word
    * @param x the current operand value
    */
   public void setOperand(int x){
       operand  = x;
   }
   /**
    * returns the current operand
    * @return the current operand value
    */
   public int getOperand(){
       return operand;
   }
}
class SimpletronConsoleTest
{
    public static void main(String[] args) {
        SimpletronConsole s = new SimpletronConsole();
    }
}
