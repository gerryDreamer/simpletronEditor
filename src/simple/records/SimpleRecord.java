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

package simple.records;

/**
 * This object provides the basic template for accessing the string values within a simple file
 * @author gerry dreamer
 */
public class SimpleRecord {
 /**
  * an instance of the string literals within a line in a simple file (plain text)
  */
    private String lineStatement;
    /**
     * This is the default no argument constructor of this object
     * It initializes the {@link simple.records.SimpleRecord#lineStatement} to whitespace
     */
    public SimpleRecord()
    {
        this("");
    }
    /**
     * This is the default one argument constructor of this object
     * @param lineStatement the current string literals within a line in a simple file (plain text)
     * @see simple.records.SimpleRecord#setLineStatement(java.lang.String) 
     */
    public SimpleRecord(String lineStatement)
    {
        setLineStatement(lineStatement);
    }
    /**
     * Updates the current string literals from a line in a simple file into a single String 
     * @param lineStatement a value of the {@link simple.records.SimpleRecord#lineStatement} instance variable
     */
    public void setLineStatement(String lineStatement)
    {
        this.lineStatement = lineStatement;
    }
    /**
     * refer to: {@link simple.records.SimpleRecord#lineStatement}
     * @return a string representation of a line within a simple file
     */
    public String getLineStatement()
    {
        return this.lineStatement;
    }
}
