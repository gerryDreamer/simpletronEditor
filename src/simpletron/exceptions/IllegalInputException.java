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

package simpletron.exceptions;

/**
 *This exception is thrown when an attempt is made to read a value 
 * &lt; -9999 and &gt; +9999 
 * @author gerry dreamer
 */
public class IllegalInputException extends Exception {

    /**
     * Creates a new instance of <code>IllegalInputException</code> without
     * detail message.
     */
    public IllegalInputException() {
    }

    /**
     * Constructs an instance of <code>IllegalInputException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public IllegalInputException(String msg) {
        super(msg);
    }
}
