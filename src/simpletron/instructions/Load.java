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
import simpletron.hardware.Accumulator;
import simpletron.hardware.InstructionCounter;
import simpletron.hardware.InstructionRegister;
import simpletron.hardware.Memory;
import simpletron.hardware.OperandRegister;
import simpletron.hardware.OperationCodeRegister;

/**
 * This object performs Load operations within an SML array
 * @author gerry dreamer
 */
public class Load extends Instructions{
     /**
     * the current constant defining this object
     */
    private final int constant = 20;
      /**
     * The default constructor creates a call to the parent object
     * @param opCode the current opCode
     * @param operand the current operand
     * @param memory the current memory
     * @param accumulator the current accumulator
     * @param instructionRegister the current instruction register
     * @param instructionCounter the current instruction counter
     */
    public Load(OperationCodeRegister opCode, OperandRegister operand,Memory memory,Accumulator accumulator, InstructionRegister instructionRegister, InstructionCounter instructionCounter)
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
         //load a value from memory into the accumulator
           try{
           setOutput(getMemory().getElement(getOperandRegister().getOperand()));//retrieve a value from memory using the operand as the index
           getAccumulator().setResult(getOutput());
           }//end try
           catch(AccumulatorOverflowException e)
           {
             getOutputStream().writeToGUI(e);
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
