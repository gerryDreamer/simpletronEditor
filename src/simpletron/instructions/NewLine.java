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

import simpletron.hardware.Accumulator;
import simpletron.hardware.InstructionCounter;
import simpletron.hardware.InstructionRegister;
import simpletron.hardware.Memory;
import simpletron.hardware.OperandRegister;
import simpletron.hardware.OperationCodeRegister;

/**
 * This object creates a new line feed in the SML output
 * @author gerry dreamer
 */
public class NewLine extends Instructions{
     /**
     * the current constant defining this object
     */
    private final int constant = 55;
     /**
     * The default constructor creates a call to the parent object
      * @param opCode the current opCode
     * @param operand the current operand
     * @param memory the current memory
     * @param accumulator the current accumulator
     * @param instructionRegister the current instruction register
     * @param instructionCounter the current instruction counter
     */
    public NewLine(OperationCodeRegister opCode, OperandRegister operand,Memory memory,Accumulator accumulator, InstructionRegister instructionRegister, InstructionCounter instructionCounter)
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
         //print a newline character to the output stream
            getOutputStream().writeToGUI("\n");
             //increment the instructionCounter
               super.getInstructionCounter().setInstructionCounter(super.getInstructionCounter().getInstructionCounter() + 1);//increment the counter
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
