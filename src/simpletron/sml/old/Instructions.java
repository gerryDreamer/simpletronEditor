/**
*
 * 
 *
 *
 * (C) Copyright 2013-2016, by Gerry Dreamer Ventures.
 *
 * Project Info:  http://WWW.facebook.com/Dreamer Ventures
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
 * License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 *
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 *
 * (C)copyright 2013-2016, by Gerry Dreamer Ventures.
 *
 * Original Author:  Gerry Dreamer;
 * Contributor(s):   Gerry Dreamer Ventures;
 *
 * 
 *
 * Changes
 * -------
 * 
 * 11-09-2016 : final compilation of the first program.
 *
*
**/
package simpletron.sml.old;

/**
 *
 * @author gerry dreamer
 */
 /**
     * this class provides constants used in the SML language simulator.
     * the constants use Integer representations.
     */
public class Instructions {   
     //------------------------------CONSTANTS-----------------------------------------------------------------
    // IO operations
    /**
     * read a word from the keyboard to a spec memory location
     */
    protected static final int READ = 10;//
    /**
     * writes a word from a spec memory location to the screen
     */
    protected static final int WRITE = 11;//
    // load / store operations
    /**
     * loads a word from a specific memory location into the accumulator 
     */
    protected static final int LOAD = 20;//
    /**
     * stores a word from the accumulator into a specific memory location.
     */
    protected static final int STORE = 21; 
    //arithmetic operations
    //all SML arithmetic instructions leave their results in the accumulator
    /**
     * adds a word form a specific memory location to the accumulator, leave the result in the accumulator
     */
    protected  static final int ADD = 30;
    /**
     * subtracts a word form a specific memory location from the accumulator, leave the result in the accumulator
     */
    protected static final int SUBTRACT = 31;
    /**
     * divides a word form a specific memory location from the accumulator, leave the result in the accumulator
     */
    protected static final int DIVIDE = 32;
    /**
     * multiplies a word form a specific memory location from the accumulator, leave the result in the accumulator
     */
    protected static final int MULTIPLY = 33;
    /**
     * finds the remainder of a word from a specific memory location from the accumulator
     */
    protected static final int MODULUS = 34;
    /**
     * provides SML with capabilities to perform exponential calculations.
     */
    protected static final int EXPONENTIATION = 35;
    /**
     * finds the maximum integer
     */
    protected static final int MAX = 36;
    /**
     *  finds the minimum integer
     */
    protected static final int MIN = 37;
    //transfer of control operations
    /**
     * allows the values currently in the accumulator to be placed in the memory location specified in the operand.
     */
    protected static final int BRANCH = 40;
    /**
     *  allows the negative values currently in the accumulator to be placed in the memory location specified in the operand.
     */
    protected static final int BRANCHNEG = 41;
    /**
     *  allows the zero values currently in the accumulator to be placed in the memory location specified in the operand.
     */
    protected static final int BRANCHZERO = 42;
    /**
     * terminates the  program execution.
     */
    protected static final int HALT = 43;
   /**
    * creates a newline in the SML instruction cycle.
    */
     protected static final int NEW_LINE = 55;//creates a new line in the program
}
