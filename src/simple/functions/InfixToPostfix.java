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

package simple.functions;
import com.dreamer.arithmetic.InfixFunctions;
import java.util.EmptyStackException;
import java.util.Stack;

/**
 * This class performs an infix to postfix conversion of an expression defined by the simple language
 * It provides a special feature of support for variable names
 * @author gerry dreamer
 */
public class InfixToPostfix {
    /**
 * the default stack object that holds the operators
 * the operators are stored as strings for simplicity
 */
     private Stack<String> stack = new Stack<String>();
     /**
      * the default functions object for this class
      */
     private InfixFunctions infixFunct = new InfixFunctions();
     /**
      * an instance of the simple language functions
      */
     private Functions simpleFunct = new Functions();
  /**
 * this method converts an infix expression to postfix
 * @param exp the current expression
 * @return a postfix expression
 */
     public String convertToPostfix(String exp)
     {
          /**
        * declare the current buffer for the postfix expression
        */
       StringBuilder temp = new StringBuilder();
       //append a right parenthesis to the expression
       exp = exp + " ) ";
       //trim the current string, remove all leading and trailing whitespaces
       String tempString = exp.trim();
       //split the string into tokens
       String[] tokens = tempString.split("\\s+");      
       /**
        * push a left parenthesis onto the stack
        */
       stack.push("(");
            /**
             * convert the current infix expression to postfix
             * handle any errors generated from empty stacks being accessed by the program
             */
        try
        {
            //loop through the tokens array
           int counter = 0;
                 /**
                 * While the stack is not empty, read infix from left to right
                 */
           while(counter < tokens.length  && !(stack.isEmpty()))
           {
                   /**
                    * declare a new instance of the current token
                    */
                 String c = tokens[counter];
                 char varName = c.charAt(0);
                  /**
                  * If the current character in infix is a digit or a valid variable name, append it to postfix.
                  */
                 if(infixFunct.isDigit(c)==true)
                 {                    
                   //  System.out.println("const: "+Integer.parseInt(c));
                     temp.append(c).append(" ");
                 }//end constant check
                 if(simpleFunct.searchAlphabet(varName) != -1 && simpleFunct.searchAlphabet(varName) != 26)
                 {
                    // System.out.println("var " + varName); 
                     temp.append(varName).append(" ");
                 }//end variable check
                  /**
                * If the current character in infix is a left parenthesis, push it onto the stack.
                */
               if(c.equals("("))
               {
                        stack.push("(");
               }//end if
               /**
                * If the current character in infix is an operator:
                */
               if(infixFunct.isOperator(c)==true)
               {
                   /**
                      * Pop operators (if there are any) at the top of the stack while they have equal
                      * or higher precedence than the current operator, and append the popped
                      * operators to postfix.
                      */
                     if(infixFunct.precedence(stack.peek())==false )//&& precedence(c)==true)
          {
                         String rem  = stack.pop();
                        // System.out.println("higher preced: "+rem);
                         /**
                          * ensure that only operators are appended to postfix the popped operator
                          */
                         if(rem != "(" && rem != ")")
                                        /**
                                         * append to postfix
                                         */
                         temp.append(rem+" ");
          }//end if
          /**
           * Push the current character in infix onto the stack
           */
          stack.push(c);
        }//end if operator check
        /**
       * If the current character in infix is a right parenthesis: 
       */
      if(c.equals(")"))
      {
                     /**
                      * Pop operators from the top of the stack and append them to postfix until
                      * a left parenthesis is at the top of the stack
                      */
        while(stack.peek() != "(")
        {
             String rem  = stack.pop();
             temp.append(rem+" ");
        }//end while
      }//end if right parenthesis check        
               counter++;//increment the counter
           }//end while
        }//end try
        catch(EmptyStackException e)
        {
                //   new simpletron.hardware.OutputStream().writeToGUI(e, "Empty stack exception");
            //TODO nothing
        }//end catch
                       return temp.toString();
     }//end string
}//end class
class InfixToPostfixTest
{  
            public static void main(String[] args) {
                  simple.functions.InfixToPostfix i = new  simple.functions.InfixToPostfix();               
               String exp1 = " z * a + ( 100 - 89 ) ";
               String postfix = i.convertToPostfix(exp1) ;
               System.out.println("infix: "+exp1);
               System.out.println("postfix: "+postfix);
    }
}