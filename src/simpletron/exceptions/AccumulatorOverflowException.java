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
 * This exception is thrown whenever values &gt; +9999 and &lt; -9999 are passed to the accumulator
 * @author gerry dreamer
 */
public class AccumulatorOverflowException extends Exception {

    /**
     * Creates a new instance of <code>AccumulatorOverflow</code> without detail
     * message.
     */
    public AccumulatorOverflowException() {
    }

    /**
     * Constructs an instance of <code>AccumulatorOverflow</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public AccumulatorOverflowException(String msg) {
        super(msg);
    }
}
