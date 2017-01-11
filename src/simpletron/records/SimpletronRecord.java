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

package simpletron.records;

import java.io.Serializable;

/**
 * This object provides an implementation of a Simpletron Machine Language object file, which is passed for execution
 * @author gerry dreamer
 */
public class SimpletronRecord implements Serializable{
    /**
     * The default serialVersionUID for this object is -4983453268911443683L
     * this value helps in maintaining compatibility across files created using this object
     */
    private static final long serialVersionUID = -4983453268911443683L;
    /**
     * a signed four digit integer passed into the SML memory and accumulator
     */
    private int word;
    /**
     * this constructor initializes the word value to a default value of 0000
     */
    public SimpletronRecord()
    {
       this(1000); 
    }
    /**
     * this constructor initializes the word value to the parameter value
     * @param word the current four digit signed integer indicating the SML instruction
     */
    public SimpletronRecord(int word)
    {
      setWord(word);
    }
    /**
     * sets the current word value to the parameter specified
     * @param w the current signed four digit signed integer indicating the SML instruction
     */
    public void setWord(int w)
    {
        this.word = w;
    }
    /**
     * this implementation provides access to the current word 
     * @return the current signed four digit integer
     */
    public int getWord()
    {
        return this.word;
    }
}
