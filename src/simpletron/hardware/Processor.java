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
import simpletron.exceptions.AddressabilityException;
import simpletron.exceptions.InvalidOperationCodeException;
import simpletron.exceptions.InvalidWordException;
import simpletron.exceptions.OutOfMemoryException;
import simpletron.instructions.Add;
import simpletron.instructions.Branch;
import simpletron.instructions.BranchNeg;
import simpletron.instructions.BranchZero;
import simpletron.instructions.Divide;
import simpletron.instructions.Exponentiation;
import simpletron.instructions.Halt;
import simpletron.instructions.Instructions;
import simpletron.instructions.Load;
import simpletron.instructions.Max;
import simpletron.instructions.Min;
import simpletron.instructions.Modulus;
import simpletron.instructions.Multiply;
import simpletron.instructions.NewLine;
import simpletron.instructions.Read;
import simpletron.instructions.Store;
import simpletron.instructions.Subtract;
import simpletron.instructions.Write;
import simpletron.records.SimpletronRecord;
/**
 * This object performs the central processing services for the SML  
 * All the hardware services are received and instantiated
 * @author gerry dreamer
 */
public class Processor {
    /**
     * an instance of the <code>OperandRegister</code> object
     */
    private OperandRegister operandRegister = new OperandRegister();
    /**
     *  an instance of the <code>OperationCodeRegister</code> object
     */
    private OperationCodeRegister operationCodeRegister = new OperationCodeRegister();
    /**
     *  an instance of the <code>Memory</code> object
     */
    private Memory memory = new Memory();
    /**
     *  an instance of the <code>Accumulator</code> object
     */
    private Accumulator accumulator = new Accumulator();
    /**
     *  an instance of the <code>InputStream</code> object
     */
    private InputStream inputStream = new InputStream();
    /**
     *  an instance of the <code>OutputStream</code> object
     */
    private OutputStream outputStream = new OutputStream();
    /**
     *  an instance of the <code>InstructionRegister</code> object
     */
    private InstructionRegister instructionRegister = new InstructionRegister();
    /**
     *  an instance of the <code>InstructionCounter</code> object
     */
    private InstructionCounter instructionCounter = new InstructionCounter();
    /**
     *  an instance of the <code>Instructions</code> object
     * initialize 17 instructions
     */
    private Instructions[] instructions = new Instructions[17];
    /**
     *  an instance of the <code>Scanner</code> object
     *  receives instructions from a keyboard
     */
    private Scanner scanner = new Scanner(System.in);
    /**
     * The current instruction before it is loaded to the instruction register
     */
    private int currentWord;
    /**
     * an instance of the string buffer for the default SML dump
     */
    private StringBuilder outputText = new StringBuilder();
    /**
     * indicates to the processor methods whether they should terminate the program 
     */
    private boolean terminate;
    /**
     * an instance of the branch object, to be used in the instructions hierarchy
     */
    private Branch branch = null;
    /**
     *  an instance of the branchZero object, to be used in the instructions hierarchy
     */
    private BranchZero branchZero = null;
    /**
     *  an instance of the branchNeg object, to be used in the instructions hierarchy
     */
    private BranchNeg branchNeg = null;
    /**
     * an instance of the <code>SimpletronRecord</code> object
     * this instance is initialized by the reader object of serialized SML files
     * In this format:
     * <code> record = (SimpletronRecord) input.readObject() </code>
     * @see java.io.ObjectInput
     * @see java.io.ObjectInputStream
     * @see java.io.ObjectInputStream#readObject() 
     */
    private SimpletronRecord record;
    /**
     * represents the current serialized *.sml file received from the user
     */
    private File inputFile;
    /**
     * represents the current *.txt destination file for the SML output
     */
    private File outputFile;
    /**
     * initializes all the methods fundamental to this object 
     * @see simpletron.hardware.Processor#initialization(java.io.File, java.io.File) 
     * @see simpletron.hardware.Processor#load() 
     * @see simpletron.hardware.Processor#execute() 
     * @param inputFile {@link simpletron.hardware.Processor#inputFile}
     * @param outputFile {@link simpletron.hardware.Processor#outputFile}
     */
    public Processor(File inputFile, File outputFile)
    {
      initialization(inputFile,outputFile);//initialize all the hardware  
     // load();//load the instructions to memory
      loadFromFile();//read from a file
      execute();
     // if(getTerminate()==true)
       dump();//this method is always executed lastly       
    }
    /**
     * this method executes the instructions loaded into the memory<br>
     * this approach makes several trade offs, since there is an inherent problem with incrementing the instruction counter <br>
     * within an <code>instructions[int ]</code> loop, branching operations are handled directly by this object, to facilitate<br>
     * smooth transfers of control to specific memory locations, and minimizing bugs, or runtime errors. This method manages to decode the <br>
     * instructions given to it, and performing necessary alterations to them.<br>
     * The implementation of this object method makes it fully independent from declarations without its scope<br>
     * A method calling it only receives information it can provide, and not otherwise.<br>
     * @see simpletron.hardware.Processor#load()
     * @see simpletron.hardware.Processor#setTerminate(boolean) 
     * @see simpletron.hardware.Processor#validateOperationCode(int) 
     * @see simpletron.hardware.InputStream#readFromGUI() 
     * @see simpletron.hardware.OutputStream#writeToGUI(java.lang.Exception) 
     * @see simpletron.hardware.OutputStream#writeToGUI(java.lang.String) 
     * @see simpletron.hardware.OutputStream#writeToGUI(int) 
     * @see simpletron.hardware.OutputStream#writeToGUI(java.lang.Exception, java.lang.String) 
     */
    public void execute()
    {
        //print msg
        outputStream.writeToGUI("begin execution"); 
       // outputStream.writeToGUI("instruction counter: "+instructionCounter.getInstructionCounter()); //write the initial counter
        /**
         * in a branching construct:
         * this is the initial value of the instruction counter plus one
         * before it is modified by the operand
         */
        int currCounter = 0;
        /**
       * in a branching construct:
         * this is the initial value of the instruction counter
         * before it is modified by the operand
         */
        int initialCounter = 0;
        /**
         * in a branching construct:
         * this value determines whether the instruction counter should be incremented or not
         * true indicates no incrementation
         * false indicates that there should be incrementation
         */
        boolean done = false;
       //fetch the instruction from memory            
             //set the end index for ending the fetch
        int endIndex = new com.dreamer.search.SearchArray().linearSearch(memory.getMemory(), +4300);//search for the terminating instruction +4300
        //begin the fetch:
                 //use the instruction counter to walk through the start and the ending indices of the array
                 //ensure the instructionCounter contains a valid memory location
                 //this is in the range 00 - memory.getSize() - 1
                try
                {
                 do              
                 {
                   //fetch an instruction from memory, the current instructionCounter is set to zero
                     //ensure that the instructionCounter references a valid memory location
                     if(instructionCounter.getInstructionCounter() >= memory.getMemorySize())
                     {
                       throw new AddressabilityException("An invalid memory address was encountered.");
                     }//end if
                      //populate the instructionRegister with the current instruction
                     //use the instructionCounter to navigate through the memory object
                     //using the instructionCounter as the index
                     instructionRegister.setInstructionRegister(memory.getElement(instructionCounter.getInstructionCounter()));
                        //calculate the operationCodes and the operands
                     // operationCode = instructionRegister / 100
                     // operand = instructionRegister % 100
                     operationCodeRegister.setOperationCode(instructionRegister.getInstructionRegister() / 100);
                     operandRegister.setOperand(instructionRegister.getInstructionRegister() % 100);
                     //validate the operationCode if it is a valid instruction
                     //this is done exclusively by calling the validate validateOperationCode method
                    validateOperationCode(operationCodeRegister.getOperationCode());
                     //polymorphically call the instructions hierarchy                    
                  for(int i=0; i<instructions.length; i++)
                  {                      
                       instructions[i].executeInstruction();//call the executeInstruction method 
                         setTerminate(instructions[i].getTerminate());
                       /**
                        * if this is an instance of branch, create a separate instance of the branch object
                        */
                       if(instructions[i] instanceof Branch)
                       {
                          branch = (Branch) instructions[i]; 
                       }//end if
                       /**
                        * if this is an instance of branchNeg, create a separate instance of the branchNeg object
                        */
                       if(instructions[i] instanceof BranchNeg)
                       {
                          branchNeg = (BranchNeg) instructions[i]; 
                       }//end if
                       /**
                        * if this is an instance of branch zero, create a separate instance of the branchZero object
                        */
                       if(instructions[i] instanceof BranchZero)
                       {
                          branchZero = (BranchZero) instructions[i]; 
                       }//end if
                  }//run the instructions within this body
                  /**
                   * if this is a branch instruction
                   */
                  if(operationCodeRegister.getOperationCode() == branch.getConstant())
                  {
                    //  outputStream.writeToGUI("Current counter: "+instructionCounter.getInstructionCounter());
                      initialCounter = instructionCounter.getInstructionCounter();
                      currCounter = instructionCounter.getInstructionCounter() + 1;
                      instructionCounter.setInstructionCounter(operandRegister.getOperand());//set the counter to the value of the operand
                      outputStream.writeToGUI("branching...");
                       // outputStream.writeToGUI("final counter: "+instructionCounter.getInstructionCounter());
                        done = true;
                  } //end if branch
                  /**
                   * if this is branchNeg
                   */
                   if(operationCodeRegister.getOperationCode() == branchNeg.getConstant())
                   {
                     // outputStream.writeToGUI("Current counter: "+instructionCounter.getInstructionCounter());
                      initialCounter = instructionCounter.getInstructionCounter();
                      currCounter = instructionCounter.getInstructionCounter() + 1;
                      if(accumulator.getResult() < 0)
                      {
                         instructionCounter.setInstructionCounter(operandRegister.getOperand());//set the counter to the value of the operand
                       outputStream.writeToGUI("branching negatives...");
                      //  outputStream.writeToGUI("final counter: "+instructionCounter.getInstructionCounter());
                        done = true;  
                      }//end if
                   }//end branchNeg
                   /**
                    * if this is a branchZero
                    */                   
                    if(operationCodeRegister.getOperationCode() == branchZero.getConstant())
                    {
                       // outputStream.writeToGUI("Current counter: "+instructionCounter.getInstructionCounter());
                      initialCounter = instructionCounter.getInstructionCounter();
                      currCounter = instructionCounter.getInstructionCounter() + 1;
                       if(accumulator.getResult() == 0)
                      {
                         instructionCounter.setInstructionCounter(operandRegister.getOperand());//set the counter to the value of the operand
                      outputStream.writeToGUI("branching zeros...");
                      //  outputStream.writeToGUI("final counter: "+instructionCounter.getInstructionCounter());
                        done = true;
                    }//end branchZero
                    }//end if branch zero   
                  /**
                   * increment the counter
                   */
                  if(operationCodeRegister.getOperationCode() != branch.getConstant() && operationCodeRegister.getOperationCode()!= branchNeg.getConstant() && operationCodeRegister.getOperationCode() != branchZero.getConstant())
                  {
                      if(done == false)
                      {
                        instructionCounter.setInstructionCounter(instructionCounter.getInstructionCounter() + 1);//increment the counter  
                      }//end if done is false
                      else
                      {
                          if(currCounter == endIndex)
                          {
                              instructionCounter.setInstructionCounter(currCounter);
                          }//end if
                          else
                          {
                               instructionCounter.setInstructionCounter(initialCounter + 1);
                               done = false;
                          }
                      }//if done is true
                  }//end if, loop increment
                 }//end do              
                 while(instructionCounter.getInstructionCounter() != endIndex);//instructionRegister.getInstructionRegister() != +4300 && 
                }//end                 
                catch(AddressabilityException e)
                {
                     outputStream.writeToGUI(e);//write to the standard error stream
                     setTerminate(true);//terminate the program if we have an invalid memory location reference call 
                }//end catch
                catch(InvalidOperationCodeException e)
                {
                     outputStream.writeToGUI(e);//write to the standard error stream
                     setTerminate(true);//terminate the program if we have an invalid memory location reference call 
                }//end catch
                catch(NumberFormatException e)//handle errors resulting from input
                {
                     outputStream.writeToGUI(e,"\nAn invalid number format has been detected");//write to the standard error stream
                     setTerminate(true);//terminate the program if we have an invalid memory location reference call 
                }
     //  setTerminate(true);//terminate once the execute method is complete
    }//end execute method
    /**
     * This method loads the program instructions from a file into memory
     * @see simpletron.hardware.InputStream#readFromFile(java.io.File, simpletron.hardware.InstructionRegister, simpletron.hardware.InstructionCounter, simpletron.hardware.Memory, simpletron.hardware.OutputStream) 
     */
    public void loadFromFile()
    {
        try
        {
          inputStream.readFromFile(inputFile, instructionRegister, instructionCounter, memory, outputStream);
        }
         catch(InvalidWordException e)
        {
           //print the exception using the standard SML error stream
          outputStream.writeToGUI(e);
            //call the dump method
           setTerminate(true);
        }
        catch(OutOfMemoryException e)
        {
             //print the exception using the standard SML error stream
           outputStream.writeToGUI(e);
            //call the dump method
            setTerminate(true);
        }
        catch(NullPointerException e)
    {
         outputStream.writeToGUI(e,"The file received is either invalid or null.");
    }
          outputStream.writeToGUI("load successful");
    }//end method
    /**
     * loads an sml program into memory using input from the keyboard
     * @deprecated this functionality has been replaced by the <code>loadFromFile</code>method
     * @see simpletron.hardware.ProcessorConsole#loadFromFile() 
     */
    public void load()
    {
        try
        {   
            int sentinel = -9999;
           do
           {
             //  System.out.println("Please enter an SML word instruction: ");             
               setCurrentWord(inputStream.readFromGUI());
               //sequantially load the instructions into memory
               if(getCurrentWord() != sentinel)
              memory.add(getCurrentWord(), instructionCounter.getInstructionCounter());
              /**
               * if the instructions are too big for the memory, throw an out of memory error
               */
              if(instructionCounter.getInstructionCounter() >= memory.getMemorySize())
              {
                  throw new OutOfMemoryException("The instructions provided are too big for the memory.");
              }//end if
               instructionCounter.setInstructionCounter(instructionCounter.getInstructionCounter() + 1);//increment the counter
           }
           while(getCurrentWord() != sentinel);//load until the current word is equal to -9999
        }
        catch(InvalidWordException e)
        {
           //print the exception using the standard SML error stream
          outputStream.writeToGUI(e);
            //call the dump method
           setTerminate(true);
        }
        catch(OutOfMemoryException e)
        {
             //print the exception using the standard SML error stream
           outputStream.writeToGUI(e);
            //call the dump method
            setTerminate(true);
        }
        //print msg
        outputStream.writeToGUI("load successful");
         //before any fetch operation, set the instruction counter to a default value of zero
        instructionCounter.setInstructionCounter(0);//clears the previous counter value set by the load method
    }
    /**
     * initializes all the hardware components for the simulator
     * @param inputFile {@link simpletron.hardware.Processor#inputFile}
     * @param outputFile {@link simpletron.hardware.Processor#outputFile}
     */
    public void initialization(File inputFile, File outputFile)
    {
        //all the hardware components are initialized by default
        //initialize the instruction hierarchy
       instructions[0] = new Subtract(operationCodeRegister,operandRegister,memory,accumulator,instructionRegister,instructionCounter);
        instructions[1] = new Multiply(operationCodeRegister,operandRegister,memory,accumulator,instructionRegister,instructionCounter);
        instructions[2] = new Modulus(operationCodeRegister,operandRegister,memory,accumulator,instructionRegister,instructionCounter);
        instructions[3] = new Halt(operationCodeRegister,operandRegister,memory,accumulator,instructionRegister,instructionCounter);
         instructions[4] = new Read(operationCodeRegister,operandRegister,memory,accumulator,instructionRegister,instructionCounter);
         instructions[5] = new Write(operationCodeRegister,operandRegister,memory,accumulator,instructionRegister,instructionCounter);
         instructions[6] = new Load(operationCodeRegister,operandRegister,memory,accumulator,instructionRegister,instructionCounter);
         instructions[7] = new Exponentiation(operationCodeRegister,operandRegister,memory,accumulator,instructionRegister,instructionCounter);
         instructions[8] = new Min(operationCodeRegister,operandRegister,memory,accumulator,instructionRegister,instructionCounter);
         instructions[9] = new Max(operationCodeRegister,operandRegister,memory,accumulator,instructionRegister,instructionCounter);
        instructions[10] = new NewLine(operationCodeRegister,operandRegister,memory,accumulator,instructionRegister,instructionCounter);
         instructions[11] = new Store(operationCodeRegister,operandRegister,memory,accumulator,instructionRegister,instructionCounter);
         instructions[12] = new Add(operationCodeRegister,operandRegister,memory,accumulator,instructionRegister,instructionCounter);
         instructions[13] = new Branch(operationCodeRegister,operandRegister,memory,accumulator,instructionRegister,instructionCounter);
         instructions[14] = new BranchNeg(operationCodeRegister,operandRegister,memory,accumulator,instructionRegister,instructionCounter);
          instructions[15] = new BranchZero(operationCodeRegister,operandRegister,memory,accumulator,instructionRegister,instructionCounter);
         instructions[16] = new Divide(operationCodeRegister,operandRegister,memory,accumulator,instructionRegister,instructionCounter);
          //initialize other important objects
          setTerminate(false);//set the terminate method to false
          //initialize the file objects
          //validate the file extensions        
          try
            {
                 this.inputFile = inputFile;
                 new com.dreamer.file.FileFunctions().validateFileExtension(inputFile, "sml","SML");
            }
           catch(com.dreamer.file.exceptions.InvalidFileNameExtensionException e)
             {
                  this.inputFile = null; 
                   new simpletron.hardware.OutputStream().writeToGUI(e,"\nEnsure the input file name extension ends with *.sml or *.SML");
             }
           try
            {
                 this.outputFile = outputFile;
                 new com.dreamer.file.FileFunctions().validateFileExtension(outputFile, "txt");
            }
           catch(com.dreamer.file.exceptions.InvalidFileNameExtensionException e)
             {
                  this.outputFile = null;    
                   new simpletron.hardware.OutputStream().writeToGUI(e,"\nEnsure the output file name extension ends with *.txt");
             }
    }
    /**
     * this method dumps the contents of Simpletron after every program execution, this includes:
     * <ul>
     * <li> Normal SML termination </li>
     * <li>SML program load error</li>
     * <li>Or an SML execution error</li>
     * <li>The following contents are printed
     * <ul>
     *   <li> All the hardware registers </li>
     *   <li> And all the words in memory </li>
     * </ul>
     * </li>
     * <li>  
     * Output is based on the following format <br>
     * <table border = 1 width="50%" style="text-align:left;">
     * <caption> SML memory &amp; Instructions Dump  </caption>
     * <thead>
     * <tr>
     * <th colspan=10>REGISTERS </th>
     * </tr>
     * <tr>
     *  <th colspan=10>accumulator +0000 </th> </tr>
     * <tr>
     *  <th colspan=10>instructionCounter 06</th> </tr>
     * <tr>
     *  <th colspan=10>instructionRegister +1001 </th> </tr>
     * <tr>
     *  <th colspan=10>operationCode 43 </th> </tr>
     * <tr>
     *  <th colspan=10>Operand 00</th> </tr>
     * <tr>
     *  <th colspan=10>validInstructions 7</th>
     * </tr>
     * </thead>
     * <tbody>
     * <tr>
     * <td>MEMORY: </td>
     * </tr>
     * <tr>
     * <td colspan=1>0</td> <td colspan=1>1</td> <td colspan=1>2</td> <td colspan=1>3</td> <td colspan=1>4</td> <td colspan=1>5</td> <td colspan=1>6</td> <td colspan=1>7</td> <td colspan=1>8</td> <td colspan=1>9</td>
     * </tr>
     * <tr>
     * <td colspan=1>+1009</td> <td colspan=1>+1008</td> <td colspan=1>+2009</td> <td colspan=1>+3008</td>
     * <td colspan=1>+2107</td> <td colspan=1>+4300</td> <td colspan=1>-9999</td> <td colspan=1>180</td> <td colspan=1>90</td> <td colspan=1>90</td>
     * </tr>
     * </tbody>
     * <tfoot>
     *<tr>
     * <td colspan=10>The results are possible if:</td>
     * </tr>
     * <tr>
     * <td colspan=2>input a = 90 </td>
     * <td colspan=2> and input b = 90 </td>
     * <td colspan=2> the result c is stored in location 07 </td>
     * <td colspan=2> c = 180 </td>
     * <td colspan=2> &copy; Dreamer Ventures, 2016 </td>
     * </tr>
     * </tfoot>
     * </table>
     * </li>
     * </ul>
     */
    public void dump()
    {
        //set the output text with a default message
        String memoryDump = new simpletron.messages.Messages().memoryDump(memory.getMemory(), accumulator.getResult(),instructionCounter.getInstructionCounter(), instructionRegister.getInstructionRegister(), operationCodeRegister.getOperationCode(), operandRegister.getOperand());
        setOutputText(memoryDump);//append the memory dump message to the output text buffer
       //determine the output locations for the current output
      //  System.out.println(""+getOutputText());//print to the console first
        //print to a text file
        outputStream.writeTextFile(outputFile, memoryDump);
        //print to the standard display
        outputStream.writeToGUI(getOutputText());
        //Note: the getOutputText() sets the data for the standard SML display
    }
    /**
     * provides access to the current word received from the user or a file
     * @param word a signed four digit integer
     * @deprecated functionality has been passed to the <code>loadFromFile</code> method
     * @see simpletron.hardware.Processor#loadFromFile() 
     * @throws InvalidWordException {@link simpletron.exceptions.InvalidWordException}
     */
    public void setCurrentWord(int word) throws InvalidWordException
    {
        /**
         * enforce integrity, ensure that the word is valid
         */
         if(word <-9999 || word > 9999)
         {
             throw new InvalidWordException("An invalid instruction or a data word has been encountered");
         } 
       else
         {
              currentWord = word;//reset the value of the word to this value
         }
    }
    /**
     * @see simpletron.hardware.Processor#setCurrentWord(int) 
     * @return the current SML word instruction
     * @deprecated refer to:: {@link simpletron.hardware.Processor#setCurrentWord(int)  }
     */
    public int getCurrentWord()
    {
        return currentWord;
    }
    /**
     * provides an abstract modification of the standard SML output 
     * @param  s the current string literal to be appended in the output buffer
     * @see simpletron.hardware.Processor#outputText
     */
    public void setOutputText(String s)
    {
        outputText.append(s);
    }
    /**
     * @see simpletron.hardware.Processor#setOutputText(java.lang.String) 
     * @see simpletron.hardware.Processor#load() 
     * @return a string representation of the current SML_PROCESSOR output_text
     */
    public String getOutputText()
    {
        return outputText.toString();
    }
   /**
    * @see simpletron.hardware.Processor#terminate
    * @param b the current terminating conditions 
    * true indicates that the program should continue
    * false indicates that the program should terminate
    * @see simpletron.hardware.Processor#load() 
    */ 
    public void setTerminate(boolean b)
    {
        terminate = b;
    }
    /**
     * provides an indicator for SML to terminate the program 
     * @see simpletron.hardware.Processor#setTerminate(boolean) 
     * @return the current program terminating condition
     */
    public boolean getTerminate()
    {
        return terminate;
    }
    /**
     * This method is used to validate an operation code if it belongs to the standard SML operationCode specification
     * @param operationCode the current operationCode received from the client
     * @throws InvalidOperationCodeException thrown when the operationCode is not recognized
     * @see simpletron.hardware.OperationCodeRegister
     * @see simpletron.exceptions.InvalidOperationCodeException
     * @see simpletron.instructions.Instructions
     */
    public void validateOperationCode(int operationCode) throws InvalidOperationCodeException
    {
        //use a linear search 
              //update the constants to a temp array
              int[] temp = new int[instructions.length];
              for(int i=0; i<temp.length; i++)
              {
                  temp[i] = instructions[i].getConstant();
              }//end for loop
               int pos = new com.dreamer.search.SearchArray().linearSearch(temp, operationCode);
              //throw an exception if the curr position is -1
              if(pos == -1)
                  throw new InvalidOperationCodeException("An invalid operation code has been encountered.");//throw an exception if an invalid opCode is encountered              
    }//end method
}
class ProcessorTest
{
    public static void main(String[] args) {
          File smlFile = new File("C:\\Users\\gerry dreamer\\Desktop\\compileOutput.sml");
         File output = new File("C:\\Users\\gerry dreamer\\Desktop\\prototype.txt");
        Processor p = new Processor(smlFile,output);
        System.out.println(""+p.getOutputText());
    }
}