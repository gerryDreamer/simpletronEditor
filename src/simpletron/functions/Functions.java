/**
*
 * 
 *
 *
 * (C) Copyright 2013-2016, by Gerry Dreamer Ventures.
 *
 * Project Info:  http://www.facebook.com/Dreamer Ventures
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
package simpletron.functions;
/**
 *
 * @author gerry dreamer
 */
/**
     * this class provides the functions needed for execution of SML programs.
     */
public class Functions {  
   /**
    * This method breaks a four digit signed integer and returns the rightmost two digits
    * @param word the current SML word
    * @return returns the current SML instruction
    */
   public int breakOperationCode(int word){
       int un,dix,cent,mille;
        un = word%10;
        dix = (word%100 - un)/10;
        cent = (word%1000 -dix)/100;
        mille = (word%10000-cent)/1000;
        String first = String.format("%s%s",mille,cent);
        return Integer.parseInt(first);
   }
   /**
    * This method breaks a four digit signed integer and returns the leftmost two digits
    * @param word  the current SML word
    * @return  returns the current SML memory index.
    */   
   public int breakOperand(int word){
        int un,dix,cent,mille;
        un = word%10;
        dix = (word%100 - un)/10;
        cent = (word%1000 -dix)/100;
        mille = (word%10000-cent)/1000;
        String last = String.format("%s%s",dix,un);
        return Integer.parseInt(last);
   }  
}
