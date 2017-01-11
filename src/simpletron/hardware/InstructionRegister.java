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

/**
 * This object receives the current SML instruction from the memory as specified by the <code>instructionCounter</code>
 * @author gerry dreamer
 */
public class InstructionRegister {
    /**
     * represents the current instruction passed from memory
     */
    private int instructionRegister;
    /**
     * this default constructor initializes the register with a value of zero
     */
    public InstructionRegister()
    {
        instructionRegister = 0;
    }
    /**
     * sets the instruction register to the current instruction received from the memory
     * @param word the current SML word
     */
    public void setInstructionRegister(int word)
    {
        this.instructionRegister = word;
    }
    /**
     * this method returns an integer representation of the current instruction in the register
     * @return the current word in the instruction register
     */
    public int getInstructionRegister()
    {
      return this.instructionRegister;   
    }//end method 
}
