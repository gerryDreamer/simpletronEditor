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

import simple.exceptions.InvalidVariableSeparatorException;
import simple.functions.Functions;

/**
 *
 * @author gerry dreamer
 */
public class Test1{
     
    
    /**
     * This method is used to validate if tokens within a simple file have a valid separator
     * @param string the current line statement 
     * @param separator a valid character separator
     * @throws InvalidVariableSeparatorException 
     */
    public void validateSeparator(String string, char separator) throws InvalidVariableSeparatorException
    {
     //create a call to the simple compiler utility object   
        Functions fx = new Functions();
     //break the string into tokens
        //trim the string
     String temp = string.trim();
     String[] tokens = temp.split("\\s+");//break the string into tokens
       //join the tokens into a single string
     String finalString = "";
     for(int i=0; i<tokens.length; i++)
     {
        // System.out.println(""+tokens[i]);
         finalString += tokens[i];
     }   
        System.out.println("final: "+finalString);
        //now that we have a final string we need to break the string and ignore all the variableName characters  from within
        //use the function searchAlphabet and ignore characters that
        //first create a character array containing characters from the finalString 
        char[] array = finalString.toCharArray();
        for(int i=0; i <array.length; i++)
        {
           //ensure that only non variables and non digits are checked          
            if(fx.searchAlphabet(array[i]) == -1 || fx.isOperator(array[i])==true || fx.searchAlphabet(array[i]) == 26)
            {
                System.out.println("chars "+array[i]);
                //throw an exception if the separator is not valid
                if(array[i] != separator)
                {
                    throw new InvalidVariableSeparatorException("\nAn invalid character separator was found\n"
                            + "Please ensure you have placed a valid variable separator");
                }//end if, invalid separator check
            }
        }//end for loop
    }//end method
}
class Test1Test
{
    public static void main(String[] args)
    {
    Test1 t = new Test1();     
    String s = "x,y, z, 3 4 5 6     ,a,b, c,12,33";
       System.out.println("original string: "+s);
    try
    {
      t.validateSeparator(s, ',');
    }
    catch(InvalidVariableSeparatorException e)
    {
        System.err.println(""+e); 
    }    
    }
}
