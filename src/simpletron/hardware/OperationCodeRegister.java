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
 * This object stores the current operation code as received from the instruction register
 * @author gerry dreamer
 */
public class OperationCodeRegister {
    /**
     * an instance of the current operation code
     */
    private int opCode;
    /**
     * initializes the <code>opCode</code> to zero 
     */
    public OperationCodeRegister()
    {
        opCode = 0;
    }
    /**
     * This method sets the current <code>opCode</code> to the value specified by the client
     * @param code the current operation code received from the client
     */
    public void setOperationCode(int code)
    {
        opCode = code;
    }
    /**
     * This method returns the current <code>opCode</code> to the value specified by the client
     * @return the current operation code received from the client
     */
    public int getOperationCode()
    {
        return opCode;
    }
}
