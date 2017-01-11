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
import simple.exceptions.InvalidSymbolTypeException;
import simple.exceptions.OutOfMemoryException;
/**
 * This object creates the standard object representation of the Simple Language symbol table
  * @since 20/12/2016
 * @version 1.0.0
 * @author gerry dreamer
 */
public class TableEntry {
    /**
     * contains the UNICODE representation of a variable, lineNumber, or a constant
     */
    int symbol;
    /**
     * a character indicating the symbol's type
     * <ul>
     * <li>
     * A list of the currently supported symbol types
     * <ul>
     * <li> <em>'C'</em>&nbsp; for Constant</li>
     * <li> <em>'L'</em>&nbsp; for line number</li>
     * <li> and,<em>'V'</em>&nbsp; for variable</li>
     * </ul>
     * </li>
     * </ul>
     */
    char type;
    /**
     * Contains the Simpletron's memory location to which the symbol refers<br>
     * the location is in the range of (00 - {@link simpletron.hardware.Memory#getMemorySize() })
     */
    int location; 
    //  * @throws InvalidSymbolTypeException  {@link simple.register.TableEntry#setSymbol(int) }
   /**
    * The default multiple argument constructor for the tableEntry object
     * initializes default values for this object
    * @param symbol {@link simple.register.TableEntry#symbol}
    * @param type   {@link simple.register.TableEntry#type}
    * @param location {@link simple.register.TableEntry#location}  
    * @throws OutOfMemoryException  {@link simple.register.TableEntry#type}
    */
    public TableEntry(int symbol, char type, int location) throws OutOfMemoryException //, InvalidSymbolTypeException
    {
        this.setSymbol(symbol);
        this.setType(type);
        this.setLocation(location);
    }
    /**
     * sets the current location of the symbol received from the *.simple file
     * @param location {@link simple.register.TableEntry#location}
     * @throws OutOfMemoryException if the current memory location is beyond the bounds of the Simpletron memory
     */
    public void setLocation(int location) throws OutOfMemoryException
    {
        /**
         * if the location is larger than the Simpletron memory
         * throw an OutOfMemoryException 
         */
        if(location >= new simpletron.hardware.Memory().getMemorySize())
        {
            throw new OutOfMemoryException("The current memory location is beyond the bounds of the Simpletron memory.");
        }
        /**
         * if the location is valid, modify the value value of location
         */
        else
        {
            this.location = location;
        }
    }
    /**
     * refer to: {@link simple.register.TableEntry#location}
     * @return the current location of the symbol
     */
    public int getLocation()
    {
        return this.location;
    }
    /**
     * sets the value of the current character type
     * @param type  {@link simple.register.TableEntry#type}    
     * character type as specified by the simple language
     * <!-- @see simple.register.TableEntry#type -->
     */
    public void setType(char type) //throws InvalidSymbolTypeException  // * @throws InvalidSymbolTypeException this exception is thrown if the character received is not a valid 
    {       
        char temp = type=='C' || type=='L' || type=='V' ? type:' ';//if this is not a valid character type, set temp to whitespace
        /**
         * if the character is not set to whitespace, add it to the table entry
         */
//        if(temp != ' ')
//        {
            this.type = temp;
//        }//end if
//        else 
//        {
//          throw new InvalidSymbolTypeException("An invalid variable name has been input.");  
//        }//end else
    }
    /**
     * refer to:  {@link simple.register.TableEntry#type}
     * @return the current character type
     */
    public char getType()
    {
        return this.type;
    }
    /**
     * sets the value of the current symbol
     * @param symbol {@link simple.register.TableEntry#symbol}
     */
    public void setSymbol(int symbol)
    {
        this.symbol = symbol;
    }
    /**
     * refer to: {@link simple.register.TableEntry#symbol}
     * @return the current symbol set by this object
     */
    public int getSymbol()
    {
        return this.symbol;
    }
}