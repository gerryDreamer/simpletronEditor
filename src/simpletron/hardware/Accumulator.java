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

import simpletron.exceptions.AccumulatorOverflowException;

/**
 * This method provides access to the accumulator used by the simulator objects
 * @author gerry dreamer
 */
public class Accumulator {
    /**
     * This instance represents the current accumulator variable
     */
   private int result;
   /**
    * This is the default constructor for this object
    */
   public Accumulator()
   {
       result = 0;//initialize result to 0 (default)
   }
   /**
    * This method sets the current result as specified by the client
    * To avoid redundancy, the client is advised to use the following approaches in processing input in the accumulator
    * <ul>
    * <li>If the client wants to increment a value, use <code>setResult(getResult() + value:INTEGER)</code> </li>
    * <li>If the client wants to decrement a value, use <code>setResult(getResult() - value:INTEGER)</code> </li>
    * <li>For multiplication, use <code>setResult(getResult() * value:INTEGER)</code> </li>
    * <li>For division, use <code>setResult(getResult() / value:INTEGER)</code> </li>
    * <li>For direct loading of a value into the accumulator, use <code>setResult(value:INTEGER)</code> do not make a call to the <code>getResult</code>method</li>
    * </ul>
    * @param value the current value to be added by the accumulator
    * @throws AccumulatorOverflowException if the accumulator value is &lt; 9999 or &gt; 9999
    */
   public void setResult(int value)  throws AccumulatorOverflowException
   {
       if(value <-99999999 || value>99999999)
          throw new AccumulatorOverflowException("Accumulator overflow exception\nAn error occurred while appending to the accumulator object result.");
      else
            result = value;     
   }
   /**
    * This method returns the current result in the accumulator
    * @return the current result
    */
   public int getResult()
   {
       return result;
   }//end method   
}
