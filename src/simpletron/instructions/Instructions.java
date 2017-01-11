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

package simpletron.instructions;
import simpletron.hardware.InputStream;
import simpletron.hardware.Accumulator;
import simpletron.hardware.InstructionCounter;
import simpletron.hardware.InstructionRegister;
import simpletron.hardware.Memory;
import simpletron.hardware.OperandRegister;
import simpletron.hardware.OperationCodeRegister;
import simpletron.hardware.OutputStream;

/**
 * This object is the direct ancestor of the SMl Instructions hierarchy <br>
 * It provides the basic framework for implementing SML instructions
 * @author gerry dreamer
 */
public abstract class Instructions 
{
    /**
     * an instance of the memory object
     */
    private Memory memory;
    /**
     * an instance of the accumulator object
     */
    private Accumulator accumulator;   
    /**
     * an instance of the OperationCodeRegister object
     */
    private OperationCodeRegister opCode;
    /**
     * an instance of the OperandRegister object
     */
    private OperandRegister operand;
    /**
     * an instance of the InputStream object
     */
    private InputStream inputStream;
    /**
     * an OutputStream of the InputStream object
     */
    private OutputStream outputStream;
    /**
     * an InstructionRegister of the InputStream object
     */
    private InstructionRegister instructionRegister;
    /**
     * an InstructionCounter of the InputStream object    
     */
    private InstructionCounter instructionCounter;
    /**
     * the current input received from a either a client or the standard input stream
     */
    private int input;
    /**
     *  the current output received from a either a client or the standard input stream
     */
    private int output;
    /**
     * this instance indicates to the processor object that the simulator should not increment the counter
     */
    private boolean terminate;
    /**
     * This constructor initializes the hardware objects associated with the instruction hierarchy
     * @param opCode the current opCode
     * @param operand the current operand
     * @param memory the current memory
     * @param accumulator the current accumulator
     * @param instructionRegister the current instruction register
     * @param instructionCounter the current instruction counter
     */
    public Instructions(OperationCodeRegister opCode, OperandRegister operand,Memory memory,Accumulator accumulator, InstructionRegister instructionRegister, InstructionCounter instructionCounter)//This is the default constructor for this object, //,InputStream inputStream,OutputStream outputStream
    {       
        this.opCode = opCode;
        this.operand = operand;
        this.memory = memory;
        this.accumulator = accumulator;
        this.inputStream = new InputStream();
        this.outputStream = new OutputStream();
        this.instructionRegister = instructionRegister;
        this.instructionCounter = instructionCounter;      
      //  setDone(false);//set the terminate to a default false value for all the subclasses
    }//end constructor
    /**
     * sets the indicator that the client should not increment the counter in the simulator program since a a branch operation occurred
     * @param b a true or a false value, a true value indicates that the program should shut down
     * a false value indicates that the program should not shut down
     */
    public void setTerminate(boolean b)
    {
        terminate = b;
    }//end
    /**
     * @see simpletron.instructions.Instructions#setTerminate(boolean) 
     * @return the current terminating condition for the SML simulator
     */
    public boolean getTerminate()
    {
        return terminate;
    }
    /**
     * represents the input this class receives from a client object
     * @param i the current input integer
     */
    public void setInput(int i)
    {
        input = i;
    }
    /**
     * represents the input this class returns to a client object
     * @return the current input object
     */
    public int getInput()
    {
        return input;
    }
     /**
     * represents the output this class is to output a client object
     * @param o the current output integer
     */
    public void setOutput(int o)
    {
        output = o;
    }
     /**
     * represents the output this class is to output a client object
     * @return the current output object
     */
    public int getOutput()
    {
        return output;
    }
    /**
     * @see simpletron.hardware.InstructionCounter
     * @return an instance of the current InstructionCounter object
     * @deprecated This functionality is passed on to the Processor object
     */
    public InstructionCounter getInstructionCounter()
    {
        return this.instructionCounter;
    }
    /**
     * @see simpletron.hardware.InstructionRegister
     * @return an instance of the current InstructionRegister object
     *  @deprecated This functionality is passed on to the Processor object
     */
    public InstructionRegister getInstructionRegister()
    {
        return this.instructionRegister;
    }
    /**
     * 
     * @return an instance of the current OutputStream object
     */
    public OutputStream getOutputStream()
    {
        return this.outputStream;
    }
    /**
     * @see simpletron.hardware.InputStream
     * @return  an instance of the current InputStream object
     */
    public InputStream getInputStream()
    {
        return this.inputStream;
    }
    /**
     * @see simpletron.hardware.Accumulator
     * @return  an instance of the current Accumulator object
     */
    public Accumulator getAccumulator()
    {
        return this.accumulator;
    }
    /**
     * @see simpletron.hardware.Memory
     * @return  an instance of the current Memory object
     */
    public Memory getMemory()
    {
        return this.memory;
    }
    /**
     * @see simpletron.hardware.OperandRegister
     * @return  an instance of the current OperandRegister object
     */
    public OperandRegister getOperandRegister()
    {
        return this.operand;
    }
    /**
     * @see simpletron.hardware.OperationCodeRegister
     * @return an instance of the current OperationCodeRegister object
     */
    public OperationCodeRegister getOperationCodeRegister()
    {
        return this.opCode;
    }    
    /**
     * this method returns the current value of the constant that this object represents
     * in the case of the <code>Read</code> object, the value 10 is returned
     * @return the operationCode value of an instruction object
     */
    public abstract int getConstant();
    /**
     * This abstract method allows subclasses of this class to uniformly perform modifications on the instructions they receive
     */
    public abstract void executeInstruction();
}
