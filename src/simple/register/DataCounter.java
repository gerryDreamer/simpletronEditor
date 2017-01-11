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

package simple.register;

/**
 * This object refers to the current data counter, which represents the current location of the SML array data
 * @author gerry dreamer
 */
public class DataCounter
{
    /**
     * this constant represents the maximum size the data counter begins from
     * this constant equals <code>Memory.getMemorySize() - 1</code>
     * Refer to: {@link simpletron.hardware.Memory#getMemorySize() }
     */
    public final int MAXIMUM_SIZE = new simpletron.hardware.Memory().getMemorySize() - 1;
    /**
     * an instance of the InstructionCounter object
     */
    InstructionCounter instrCounter = new InstructionCounter();
    /**
     * the default no argument constructor, does nothing for now
     */
    public DataCounter(){
        //set the counter to a default value of MAXIMUM_SIZE - 1 
        //if the SMl memory is 100, then the data counter will be equal to 98
        this.setDataCounter(MAXIMUM_SIZE - 1);
    }
    /**
     * Sets the current data counter for this object
     * @param counter the current data counter
     */
    public void setDataCounter(int counter)
    {
        instrCounter.setInstructionCounter(counter);
    }
    /**
     * @see simple.register.DataCounter#setDataCounter(int) 
     * @return the current data counter
     */
    public int getDataCounter()
    {
        return instrCounter.getInstructionCounter();
    }
}
