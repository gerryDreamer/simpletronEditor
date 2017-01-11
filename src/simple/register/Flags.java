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

import simpletron.hardware.Memory;

/**
 * This object represents the flags array used by the simple compiler
 * @author gerry dreamer
 */
public class Flags {
    /**
     * the current instance of a memory object
     * @see simpletron.hardware.Memory
     */
    Memory memory = new Memory();
    /**
     * this constructor initializes all elements of the flags array to -1 by default
     * @see simpletron.hardware.Memory#initializeMemory(int) 
     */
    public Flags()
    {
     memory.initializeMemory(-1);//initialize all the elements to -1
    }
    /**
     * returns a given flag element based on the given index
     * @param index the current index
     * @return the flag element based on the given index 
     */
    public int getElement(int index)
    {
      return  memory.getElement(index);
    }
    /**
     * @see simpletron.hardware.Memory#getMemory() 
     * @return the array representation of this object
     */
    public int[] getFlagsArray()
    {
        return memory.getMemory();
    }
    /**
     * @see simpletron.hardware.Memory#getMemorySize() 
     * @return the size of the flags array
     */
    public int getFlagSize()
    {
        return memory.getMemorySize();
    }
    /**
     * @see simpletron.hardware.Memory#add(int, int) 
     * @param value the current value to be added
     * @param index the location for the element
     */
    public void addFlag(int value, int index)
    {
        memory.add(value, index);
    }
    /**
     * refer to: {@link simpletron.hardware.Memory#toString() }
     * @return a string representation of this object
     */
    public String toString()
    {
        return memory.toString();
    }
}
