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

import com.dreamer.file.exceptions.InvalidFileNameExtensionException;
import java.io.File;
import simple.exceptions.InvalidVariableSeparatorException;
import simple.functions.Functions;

/**
 *
 * @author gerry dreamer
 */
public abstract class Test {
    public static void main(String[] args) {
    Functions f = new Functions();
    try
    {
      String lineStatement = " 10 input a  ,  b,                     c,d ";       
      String temp = lineStatement.trim();
    //split the strings into an array of the respective tokens
    String tokens[] = temp.split("\\s+");
    
    //declare the multiple variable declarations
    String s;
    String s1;
    String[] s2;
    String s3;
    String s4[];
    //end multiple variable declarations
    
    //print
    //ignore tokens at index 1, and 2
    //create a temp string s
    //append the tokens at indices 2 - n into s
     s = "";
    for(int i=2; i<tokens.length; i++)
    {
        s += tokens[i] + " ";
    }
    //ensure that the expression contains valid separators       
     new simple.functions.Functions().validateSeparator(s, ',');
    //create a temp string s1, which contains a trimmed version of s
     s1 = s.trim();  
    //the resulting array will contain the individual variables that are comma separated
     s2 = s1.split("\\,");
    //create String s3, where the tokens of s2 will be appended
     s3 = "";
    //append the tokens at s2[] to s3
    for(String element: s2)
    {
        s3 += element + " ";
    }//end for
    //create an array string s4[] which contains the tokens at s3 without any whitespace
     s4 = s3.split("\\s+");
    /**
     * loop between the variables
     */
    for(int i=0; i<s4.length; i++)
    {
        System.out.println("variables: "+s4[i]);
    }//end for loop, print variables loop    
    }//end try
    catch(InvalidVariableSeparatorException e)
    {
           System.err.println(""+e); 
    }
} 
 
}    
