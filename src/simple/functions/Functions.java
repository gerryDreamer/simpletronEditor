/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2016 Oracle and/or its affiliates. All rights reserved.
 *
 * Oracle and Java are registered trademarks of Oracle and/or its affiliates.
 * Other names may be trademarks of their respective owners.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common
 * Development and Distribution License("CDDL") (collectively, the
 * "License"). You may not use this file except in compliance with the
 * License. You can obtain a copy of the License at
 * http://www.netbeans.org/cddl-gplv2.html
 * or nbbuild/licenses/CDDL-GPL-2-CP. See the License for the
 * specific language governing permissions and limitations under the
 * License.  When distributing the software, include this License Header
 * Notice in each file and include the License file at
 * nbbuild/licenses/CDDL-GPL-2-CP.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the GPL Version 2 section of the License file that
 * accompanied this code. If applicable, add the following below the
 * License Header, with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [2016] [Dreamer Ventures]"
 *
 * If you wish your version of this file to be governed by only the CDDL
 * or only the GPL Version 2, indicate your decision by adding
 * "[Contributor] elects to include this software in this distribution
 * under the [CDDL or GPL Version 2] license." If you do not indicate a
 * single choice of license, a recipient has the option to distribute
 * your version of this file under either the CDDL, the GPL Version 2 or
 * to extend the choice of license to its licensees as provided above.
 * However, if you add GPL Version 2 code and therefore, elected the GPL
 * Version 2 license, then the option applies only if the new code is
 * made subject to such option by the copyright holder.
 *
 * Contributor(s): Dreamer Ventures
 *
 * Portions Copyrighted 2016 Sun Microsystems, Inc.
 */
package simple.functions;

import com.dreamer.data.structures.EmptyListException;
import com.dreamer.data.structures.QueueClass;
import com.dreamer.data.structures.StackFunctions;
import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.Stack;
import java.util.StringTokenizer;
import simple.exceptions.InvalidVariableSeparatorException;
import simple.register.Flags;
import simple.register.SMLArray;
import simple.register.SymbolTable;

/**
 * this class provides essential functions for the simple object
 * @author gerry dreamer
 */
public class Functions {
/**
 * this character array represents the current implementation of characters 
 * supported by the simple language
 */
private char[] alphabet = {'a','b','c','d','e','f','g','h','i','j','k','l','m','n'
         ,'o','p','q','r','s','t','u','v','w','x','y','z'};
/**
 * this method is used in validation of a character as a lowercase letter
 * @param x the current character to be compared 
 * @return returns true if the current character is lowercase
 * @see simple.functions.Functions#searchAlphabet(char) 
 */
 public boolean isLowercase(char x)
 {
  int pos = searchAlphabet(x);//initialize and declare the current position of the character
  //if it is strictly a letter
  //this integrity is enforced by the searchAlphabet method
  if(pos == -1)
    return false;             
  return true;              
 }
 /**
   * this method checks if a character is a lowercase Latin character
   * based on the ASCII character set values (97 - 122)
 * @param x the current character to be compared
  * @return returns the position of the character, or -1 if the character is not lowercase
  * a default value of 26 is assigned to operators and digits
  * @see com.dreamer.data.structures.StackFunctions#isOperator(java.lang.Character)  
  * @see com.dreamer.data.structures.StackFunctions#isDigit(char) 
  */       
public int searchAlphabet(char x) 
{
      /**
       * call the default digit check class
       */        
   StackFunctions funct = new StackFunctions();
    int pos = 0;//initialize the current position of the character
    /**
     * ensures that the current character is a letter within the alphabet
     * this step reduces unnecessary array traversal and references
     * hence it economizes on system time and memory
     */
   if(funct.isDigit(x)==false && isOperator(x)==false)
   {
      pos = searchAlphabetHelper(x,0); //assign the current position value of the character               
   }//end integrity check  
   /**
    * ensures that the current character is a either an operator or a digit 
    */
   else
   {
      pos = 26; //assign the current position value of the operator or digit           
   }
   return pos; //return the current position of the character         
}//end method
/**
 * this method checks if a character is a lowercase Latin letter
 * @param x the current character to be compared
 * @param index the current index, in the range, 0 - 25
 * @return returns the position of the character, or -1 if the character is not lowercase
 * @see simple.functions.Functions#searchAlphabet(char) 
 */
private int searchAlphabetHelper(char x, int index)
{
     /**
      * once the array length is reached
      * return a default value of -1
      * this means that the value was not found
      */          
     if(index == alphabet.length)
     {
                     return -1;
     }//end if
     /**
      * terminate the routine once the character matches 
      * those within the alphabet array
      * return the current index
      */
     if(x == alphabet[index])
     {
         return index; 
     }//end if          
    return searchAlphabetHelper(x,index + 1);
}//end method
 /**
 * this is a customized routine that determines if a character is an operator
 * @param x the current character
 * @return returns true if the current character is an operator
 */
public boolean isOperator(char x)
{
    char[] ops = {'%','^','/','*','+','-'
    ,'=','<','>','!'};//the current char array of operators
    for(int i=0; i<ops.length; i++)
    {
       if(x == ops[i])
       {
          return true;
       }//end if
    }//end for
  return false;//default value             
}//end method
   /**
     * this method determines whether a string literal contains 
     * its first token value as a digit, this is useful when checking for line numbers
     * @param x the current string literal value
     * @return returns true if x is a digit, and false if x is otherwise
     */
    public boolean isDigit(String x){  
        //acquire the first token
       StringTokenizer token = new StringTokenizer(x);
       String s = "";
       for(int i =0; i<1; i++)
       {
           s = token.nextToken();
       }
      try
      {
            int intValue = Integer.parseInt(String.valueOf(s));
            if(intValue > 0 || intValue < 0)
                return true;
      }
       catch(NumberFormatException e)
       {
         return false;  
       }
        return false;
    }//end method isDigit
    /**
  * this method checks if this string is a binary operator
  * currently supported by the simple language
  * @param x the current string
  * @return returns true if x is a binary operator
  */
        public boolean isBinaryOperator(String x)
        {
           String operators[] = {"+","-","*","/"};
           int i = 0;
           while( i < operators.length)
           {
             if(x.equals(operators[i]))
             {
                return true;
             }//end if
             ++i;//increment the loop counter
           }//end while
                       return false;//default value
        }//end method
         /**
         * this method attempts at creating a string representation of a queue
         * @param queue the current queue
         * @return a string representation of the current queue
         */
  public String addQueueItemsToString(QueueClass queue)
  {
     String output = " ";
     try
     {
      Object removedItem = null;//dequeue objects
     while(!(queue.isEmpty()))
     {
     removedItem = queue.dequeue();
     output += (removedItem.toString() + " ");
     }//end while loop
     }//end try
     catch(EmptyListException e)
     {
        //TODO nothing            
     }//end catch
     return output;
  }//end method
  /**
   * prints an array list
   * @param e the current array list
   */
  public void printList(ArrayList e)
  {
      for(int i=0; i<e.size(); i++)
      {
                     System.out.println(" "+e.get(i));         
      }
  }//end method print list
   /**
   *<pre> 
   * this method recursively validates a string expression as being a relational 
   * operator
   * the method linearly walks through the relational operator array
   * since, the array is small, this method works at its best case in the O(n) linear time
   * a helper method is used to encapsulate details of the operator array and the recurrent array index  </pre>
   * @param op the current relational operator
   * @return returns true if the current string is a relational operator
   * and false if otherwise
   */
public boolean isRelationalOperator(String op)
{
  String[] ops = {"<",">",">=","==","!=","<="};
  return isRelationalHelper(0,ops,op);             
}//end method
/**
 * this method assists in validating a relational operator string
 * @param index the current array index
 * @param ops the current relational operator array
 * @param op the current relational operator
 * @return returns true if the current string is a relational operator
 * and false if otherwise
 */
private boolean isRelationalHelper(int index, String[] ops,String op)
{
 if(index == ops.length)
    return false;  
 else if(ops[index].equals(op))
 {
    return true;            
 }//end else if
      return isRelationalHelper(index + 1,ops,op);
}//end method
 /**
    * prints a symbolTable object
    * @param symbolTable {@link simple.register.SymbolTable}
    */
   public void printSymbolTable(SymbolTable symbolTable)
   {
         System.out.printf("%-10s%-10s%-10s%10s\n","Symbol","Type","Location","SymbolIndex");//print the headers
         for(int i=0; i<symbolTable.getSymbolTableSize(); i++)
     {
         /**
          * if the table has no whitespace, print its contents
          */
         if(symbolTable.getTableEntry(i).getType() != ' ')
          System.out.printf("%-10d%-10s%-10d%10d\n",symbolTable.getTableEntry(i).getSymbol(),symbolTable.getTableEntry(i).getType(),symbolTable.getTableEntry(i).getLocation(),symbolTable.getSymbolIndex());
       }
   }//end method
   /**
    * Prints an SML array 
    * @param smlArray  {@link simple.register.SMLArray}
    */
   public void printSMLArray(SMLArray smlArray)
   {
       System.out.println("\nSML Array");
       System.out.println(""+smlArray.toString());
   }
   /**
    * Prints a Flags array
    * @param flags {@link simple.register.Flags}
    */
   public void printFlagsArray(Flags flags)
   {
          System.out.println("\nFlags Array");
       System.out.println(""+flags.toString());
   }
    /**
     * This method confirms if a symbolTable object has a duplicate within the table
     * @param table the current symbolTable object {@link simple.register.SymbolTable}
     * @param symbol  {@link simple.register.TableEntry#symbol}
     * @return true if the symbol has a duplicate, and false if no duplicate is encountered
     * @param type  {@link simple.register.TableEntry#type}
     */
    public boolean confirmIfDuplicate(SymbolTable table, int symbol, char type)
    {
       for(int i=0; i<table.getSymbolTableSize(); i++)
       {
         if(table.getTableEntry(i).getSymbol() == symbol && table.getTableEntry(i).getType()==type)
             return true;
       }//end for loop 
        return false;
    }
    /**
     * This method allows the client to search for a tableEntry object within the symbolTable <br> 
     * containing a specified symbol
     * @param table {@link simple.register.SymbolTable}
     * @param symbol {@link simple.register.TableEntry#symbol}
     * @param type {@link simple.register.TableEntry#type}
     * @return the index location of the table containing the specified symbol
     * otherwise, -1 is returned if the tables don't contain the symbol
     */
    public int findTableIndexBasedOnSymbol(SymbolTable table,int symbol,char type)
    {
     //loop thru the symbol table
       for(int i=0; i<table.getSymbolTableSize(); i++)
       {
        if(table.getTableEntry(i).getSymbol() == symbol && table.getTableEntry(i).getType()==type)
            return i;//return the current index
       }//end for loop 
        return -1;
    }//end search
    /**
     * This method allows the client to verify if the tokens within the sml line are valid simple characters
     * @param s the current string token
     * @return true if this is an unwanted simple character, and false if otherwise
     */
    public boolean isUnwantedCharacter(String s)
    {
      String[] illegals = {"`","~","!","#","$","%","^","&","*","(",")","{","}","[","]","|","?","\\","//",".",","};
      for(int i=0; i<illegals.length; i++)
      {
          if(s.equals(illegals[i]))
              return true;
      }
        return false;
    }
    /**
     * prints a stack (for debugging only)
     * @param stack the current stack
     */
    public void printStack(Stack stack)
    {
        try
        {
            System.out.println("\nstack contents");
           for(int i=0; i<stack.size(); i++)
           {
               System.out.println(""+stack.get(i));
           }//end for
        }
        catch(EmptyStackException e)
        {
            //TODO
        }
    }
    /**
     * This method is used to validate if tokens within a simple file have a valid separator
     * @param string the current string containing comma separated expressions 
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
     String[] tokens = temp.split("\\s+");//break the string into tokens based on the whitespace character as the delimiter
       //join the tokens into a single string
     String finalString = "";
     for(int i=0; i<tokens.length; i++)
     {
        // System.out.println(""+tokens[i]);
         finalString += tokens[i];
     }   
       // System.out.println("final: "+finalString);
        //now that we have a final string we need to break the string and ignore all the variableName characters  from within
        //use the function searchAlphabet and ignore characters that
        //first create a character array containing characters from the finalString 
        char[] array = finalString.toCharArray();
        for(int i=0; i <array.length; i++)
        {
           //ensure that only non variables and non digits are checked
            //use the logical operator or (||) to ensure that both operators, non variableNames, and digits generate an error if they are encountered
           if(fx.searchAlphabet(array[i]) == -1 || fx.isOperator(array[i])==true || fx.searchAlphabet(array[i]) == 26)
            {
              //  System.out.println("chars "+array[i]);
                //throw an exception if the separator is not valid
                if(array[i] != separator)
                {
                    throw new InvalidVariableSeparatorException("\nAn invalid character separator was found\n"
                            + "Please ensure you have placed a valid variable separator");
                }//end if, invalid separator check
            }
        }//end for loop
    }//end method
}//end class
