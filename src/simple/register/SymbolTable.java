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

import simple.exceptions.OutOfMemoryException;

/**
 * This object contains an array of tableEntry objects, which are used to map symbols received from a simple file
 * @see simple.register.TableEntry
 * @since 20/12/2016
 * @version 1.0.0
 * @author gerry dreamer
 */
public class SymbolTable {
    /**
     * represents the index of the symbolTable
     * allows for hovering / adding values to the table
     */
    private int symbolIndex;
    /**
     * create a TableEntry array of elements based on the size of the SML memory
     */
    TableEntry[] tableEntry = new TableEntry[new simpletron.hardware.Memory().getMemorySize()];
    /**
     * the default no argument constructor for this object
     * initializes an array of tableEntry objects
     * refer to: {@link simple.register.SymbolTable#initializeTables() }
     * @see simple.register.TableEntry
     */
    public SymbolTable()
    {        
       try
       {
           this.initializeTables();//initialize the tables
       }
       catch(OutOfMemoryException e)
       {
           new simpletron.hardware.OutputStream().writeToGUI(e, "The given memory location is out of bounds within the SML array");
       }
       //initialize the symbol index to a default value of zero
       this.setSymbolIndex(0);
    }
    /**
     * refer to: {@link simple.register.SymbolTable#symbolIndex}
     * @param index the current symbolIndex entr
     */
    public void setSymbolIndex(int index)
    {
        this.symbolIndex = index;
    }
    /**
     * refer to: {@link simple.register.SymbolTable#symbolIndex}
     * @return returns the current symbolIndex 
     */
    public int getSymbolIndex()
    {
        return this.symbolIndex;
    }
    /**
     * This method enables a client object to access the size of the symbol table object
     * refer to: {@link simple.register.SymbolTable#tableEntry}
     * @return the size of the tableEntry array
     */
    public int getSymbolTableSize()
    {
        return tableEntry.length;
    }
    /**
     * This method provides access to the symbolTable array
     * @return an array representation of tableEntry objects
     * @see simple.register.TableEntry
     */
    public TableEntry[] getSymbolTable()
    {
        return tableEntry;
    }
    /**
     * this method initializes the symbolTable to its defaults
     * <pre>
     * In every tableEntry object:
     * symbol = 0;
     * type = ' ';
     * location = 00;
     * </pre>
     *  Refer to: {@link simple.register.TableEntry}
     */
    private void initializeTables() throws OutOfMemoryException
    {
        /**
         * lterate over the symbolTable array
         */
        for(int i=0; i<tableEntry.length; i++)
        {
            tableEntry[i] = new TableEntry(0,' ',00);
        }//end for loop
    }
    /**
     * Refer to: {@link simple.register.TableEntry}
     * @param index the current index indicating the location of the table entry
     * @return a tableEntry object within the specified location
     */
    public TableEntry getTableEntry(int index)
    {
        return tableEntry[index];
    }
    /**
     * This method adds values to the symbol table
     * @param symbol {@link simple.register.TableEntry#symbol}
     * @param type    {@link simple.register.TableEntry#type}
     * @param location {@link simple.register.TableEntry#location}
     * @param index the current index of the symbol table
     * @throws simple.exceptions.OutOfMemoryException refer to: {@link simple.exceptions.OutOfMemoryException}
     */
    public void addTableEntry(int symbol, char type, int location, int index) throws OutOfMemoryException
    {
        tableEntry[index] = new TableEntry(symbol,type,location);
    }
}
