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
import simpletron.exceptions.AccumulatorOverflowException;
import simpletron.exceptions.DivideByZeroException;
import simpletron.hardware.Accumulator;
import simpletron.hardware.InstructionCounter;
import simpletron.hardware.InstructionRegister;
import simpletron.hardware.Memory;
import simpletron.hardware.OperandRegister;
import simpletron.hardware.OperationCodeRegister;
/**
 * This object performs Divide operations within an SML array
 * @author gerry dreamer
 */
public class Divide extends Instructions{
    /**
     * the current constant defining this object
     */
    private final int constant = 32;
     /**
     * The default constructor creates a call to the parent object
     * @param opCode the current opCode
     * @param operand the current operand
     * @param memory the current memory
     * @param accumulator the current accumulator
     * @param instructionRegister the current instruction register
     * @param instructionCounter the current instruction counter
     */
    public Divide(OperationCodeRegister opCode, OperandRegister operand,Memory memory,Accumulator accumulator, InstructionRegister instructionRegister, InstructionCounter instructionCounter)
    {
        super(opCode,operand,memory,accumulator,instructionRegister,instructionCounter);
    }
    /**
     * this method executes the current instruction
     */
    public void executeInstruction()
    {
        /**
         * ensure that the current opCode is equal to the constant
         */
        if(super.getOperationCodeRegister().getOperationCode()==constant)
        {
          //divide a value from the location in memory specified in the operand into the accumulator
           try{
               int memoryValue = getMemory().getElement(getOperandRegister().getOperand());
               /**
                * enforce integrity
                * if the value from memory is zero, throw a new divide by zero exception
                * the program should not even attempt to divide by zero
                */
               if(memoryValue == 0)
               {
                    throw new DivideByZeroException("Cannot divide by zero");
               }//end inner if
               setOutput(getAccumulator().getResult() / memoryValue);//update the output value of this object
               getAccumulator().setResult(getOutput());//append the output value to the accumulator
           }//end try
           catch(AccumulatorOverflowException e)
           {
             getOutputStream().writeToGUI(e);//write the exception message
             setTerminate(true);//terminate the program
           }//end catch
           catch(DivideByZeroException e)
           {
                   getOutputStream().writeToGUI(e);//write the exception message
                    setTerminate(true);//terminate the program
           }//end catch
        }
    }//end method   
    /**
     * this method provides access to the current constant
     * @return constant defining this instruction
     */
    public int getConstant()
    {
        return constant;
    }
}
