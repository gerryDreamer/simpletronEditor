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
 * This object represents the current instruction counter which the client uses to 
 * access the memory location within the Simpletron machine
 * @author gerry dreamer
 */
public class InstructionCounter {
    /**
     * an instance of the current instruction counter
     */
    private int instructionCounter;
    /**
     * initializes the current counter to zero
     */
    public InstructionCounter()
    {
        instructionCounter = 0;
    }
    /**
     * This method sets the value of the current instruction counter
     * @param c the current counter value
     */
    public void setInstructionCounter(int c)
    {
        instructionCounter = c;
    }
    /**
     * This method provides an integer representation of the current <code>instructionCounter</code>
     * @return the current <code>instructionCounter</code>
     */
    public int getInstructionCounter()
    {
        return instructionCounter;
    }
}
