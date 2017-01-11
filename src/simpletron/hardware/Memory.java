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
 * This object provides access to the memory hardware part of the simulator
 * @author gerry dreamer
 */
public class Memory {
    /**
     * initialize the memory to 100 elements
     */
    private int[] memory = new int[100];
    /**
     * this is the default constructor of this object
     * it fills the array to a default of zero values at the onset
     * @see com.dreamer.array.ArrayFunctions#fillArray(int[], int) 
     */
    public Memory()
    {
      //  new com.dreamer.array.ArrayFunctions().fillArray(memory, 0);//fill the array with zero values
        initializeMemory(0);
    }
    /**
     * This method initializes the memory array with the specified value
     * @param value the value which initializes all the elements of this array
     * @see com.dreamer.array.ArrayFunctions#fillArray(int[], int) 
     */
    public void initializeMemory(int value)
    {
         new com.dreamer.array.ArrayFunctions().fillArray(memory, value);//fill the array with zero values
    }
    /**
     * This method adds a value to a specified location within the memory
     * @param value the current value to be added
     * @param index the location in memory
     */
    public void add(int value, int index)
    {
        memory[index] = value;//add the value to the specified location
    }
    /**
     * This method returns a given element based on its index location
     * @param index the index to be searched
     * @return the array element with the given index
     */
    public int getElement(int index)
    {
        return memory[index];
    }
    /**
     * This method returns the memory as it is
     * @return an array representation of the memory
     */
    public int[] getMemory()
    {
        return memory;
    }   
    /**
     * This method provides the client with access to the actual memory size of the SML Simulator
     * @return the size of the SML memory array
     */
    public int getMemorySize()
   {
    return memory.length;
   }
    /**
     * Provides a string representation of this object
     * This method uses a <code>stringBuilder</code> to create a buffer of the string contents
     * before they are finally output to an immutable string
     * @return a string representation of this object and its contents
     */
    @Override
   public String toString()
   {
       StringBuilder stringBuilder = new StringBuilder();
       for(int i = 0; i<memory.length; i++)
       {
           if(i%10==0)
               stringBuilder.append("\n");
           stringBuilder.append(String.format("%s%04d%s",memory[i] < 0 ? "-":"+", Math.abs(memory[i])," "));
       }
          stringBuilder.append("\n");//append a newline
          return stringBuilder.toString();
   }//end string
}//end class
