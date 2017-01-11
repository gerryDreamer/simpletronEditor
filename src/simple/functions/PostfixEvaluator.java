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
import simple.register.DataCounter;
import simple.register.InstructionCounter;
import simple.register.InstructionRegister;
import simple.register.Operand;
import simple.register.OperationCode;
import simple.register.SMLArray;
import simple.register.SymbolTable;

/**
 * This class evaluates an infix expression generated by the simple compiler, and 
 * then generates SML code that corresponds to the operations, in essence it does not 
 * fully evaluate the expression, but it only creates a hook for the SML simulator to do the work
 * @author gerry dreamer
 */
public class PostfixEvaluator {
    /**
    * the default stack object that holds the operators
    */
     private Stack<Integer> stack = new Stack<Integer>();
     /**
      * the current buffer
      */
     private StringBuilder temp = new StringBuilder();
     /**
     * specifies the location of SML data within the SML array
     */
    private DataCounter dataCounter ;
     /**
     * specifies the location of SML instruction within the SML array
     */
    private InstructionCounter instrCounter ;
    /**
     * represents the operand of the final SMl instruction
     */
    private Operand operand ;
    /**
     * represents the opcode of the final SMl instruction
     */
    private OperationCode opCode ;
    /**
     * represents the current array temporarily holding the SML instructions and data before they are actually written to an SML file
     */
    private SMLArray smlArray ;
    /**
     * a reference table for the simple file symbols 
     */
    private SymbolTable symbolTable ;
    /**
     * contains essential functions for compiling a simple file
     */
    private Functions simpleFunct = new Functions();
    /**
     * temporarily holds an SML instruction before it is appended to the SML array
     */
    private InstructionRegister instrRegister;
    /**
      * the default functions object for this class
      */
     private InfixFunctions infixFunct = new InfixFunctions();
    
    /**
     * This is the default constructor for this object, <br>it initializes all
     * the necessary objects required by this object
     * @param dataCounter {@link simple.compiler.Compiler#dataCounter}
     * @param instrCounter {@link simple.compiler.Compiler#instrCounter}
     * @param operand {@link simple.compiler.Compiler#operand}
     * @param opCode {@link simple.compiler.Compiler#opCode}
     * @param smlArray {@link simple.compiler.Compiler#smlArray}
     * @param symbolTable {@link simple.compiler.Compiler#symbolTable}
     * @param instrRegister {@link simple.compiler.Compiler#instrRegister}
     */
    public PostfixEvaluator(DataCounter dataCounter,InstructionCounter instrCounter,Operand operand,OperationCode opCode,SMLArray smlArray,SymbolTable symbolTable,InstructionRegister instrRegister)
    {
        this.dataCounter = dataCounter;
        this.instrCounter = instrCounter;
        this.operand = operand;
        this.opCode = opCode;
        this.smlArray = smlArray;
        this.symbolTable = symbolTable;
        this.instrRegister = instrRegister;
        //ensure all objects are initialized here
    }
    /**
     * This method allows for a customized postfix evaluation algorithm:
     * in the case of:
     * <pre>
     * y 1 +
     * since y is located in the symbol table, its corresponding memory location is pushed into the stack
     * also since 1 is located in the symbol table, its corresponding memory location is pushed onto the stack
     * when the + operator is encountered, the evaluator pops the stack into the right operand of the operator,
     * and pops the stack again into the left operand of the operator, thus:
     * +2099 (load y)
     * +3098 (add 1)
     * +2197 (result)
     * the result is pushed onto the stack
     * after evaluation of the expression, the result is stored in the variable before the assignment operator
     * </pre>
     * @param exp the current postfix expression received from the compiler functions
     */
    public int evaluatePostfixExpression(String exp)
    {
                /**
                 *  Append a right parenthesis ')' to the end of the postfix expression. When the right parenthesis
                 * character is encountered, no further processing is necessary.
                 */
                 exp = exp + " ) ";
                  //trim the current string, remove all leading and trailing whitespaces
                String tempString = exp.trim();
                //split the string into tokens
                String[] tokens = tempString.split("\\s+"); 
                /**
    * When the right-parenthesis character has not been encountered, read the expression
    * from left to right.
    * handle any empty stack exceptions
    */
   try
   {
        /**
         * loop the expression from left to right
         */  
         int counter = 0;
         while(counter < tokens.length)
         {             
                   /**
                    * declare a new instance of the current token
                    */
         String c = tokens[counter];             
         /**
          * if the current token is not a right parenthesis
          * 
          */
         if(!(c.equals(")")))
         {
             /**
              * push the corresponding memory locations of the constants and the variables onto the stack
              * check both first
              * i. if the constants/variables exist within the symbol table:
              * ii. determine their memory locations and push them onto the stack
              * iii. create integer location
              * a temp tableEntry object
              * the location is a 2 digit integer
              * there is no need to increment the indices
              */
                char varName = c.charAt(0);//declare the variable character
                 /**
                  * If the current character in infix is a digit or a valid variable name, append it to postfix.
                  * push the memory location of the constant to the stack
                  */
                 if(infixFunct.isDigit(c)==true)//if this is a constant
                  {
                        /**
                         * if the constant exists within the symbolTable                        
                         */ 
                     if(simpleFunct.confirmIfDuplicate(symbolTable, Integer.parseInt(c), 'C')==true)
                     {
                         //set the opCode based on the location of the variable
                            //first find the location of the symbol
                            int location = simpleFunct.findTableIndexBasedOnSymbol(symbolTable, Integer.parseInt(c),'C');
                            //create a temporary tableEntry object based on the location of the symbol
                            simple.register.TableEntry tableEntry = symbolTable.getTableEntry(location);
                            //update the locations to the stack
                            stack.push(tableEntry.getLocation());//push the memory locations
                             // System.out.println(""+tableEntry.getLocation());
                            //System.out.println("location: "+location+" const: "+c);
                     }
                  }//end if, constant check
                 /**
                  * if this is a variable
                  * push the memory location of the variable to the stack
                  */
                 if(simpleFunct.searchAlphabet(varName) != -1 && simpleFunct.searchAlphabet(varName) != 26)
                  {
                        /**
                         * if the variable exists within the symbolTable                        
                         */ 
                     if(simpleFunct.confirmIfDuplicate(symbolTable, varName, 'V')==true)
                     {
                         //set the opCode based on the location of the variable
                            //first find the location of the symbol
                            int location = simpleFunct.findTableIndexBasedOnSymbol(symbolTable, varName,'V');
                            //create a temporary tableEntry object based on the location of the symbol
                            simple.register.TableEntry tableEntry = symbolTable.getTableEntry(location);
                            //update the locations to the stack
                            stack.push(tableEntry.getLocation());//push the memory locations
                          //  System.out.println(""+tableEntry.getLocation());
                            //System.out.println("location: "+location+" var: "+varName);
                     } 
                  }//end if, variable check
           /**
            * Calculation stage:
            * if the current token is an operator
            * calculate y operator x
             * push the result onto the stack
             * if the operator
             * is '/', the top of the stack is 2and the next element in the stack is 8,
             * then pop 2 into x, pop 8 into
             * y, evaluate 8/2 and push the result,4, back on the stack.
             * This note also applies to operator '-'.
            */
             if(infixFunct.isOperator(c)==true)
             {
                int op1 = 0;
                int op2 = 0;            
                 //System.out.println("op1: "+op1+" op2: "+op2);
              int x = 0, y = 0;
                int value = 0;
                switch(c)
                {
                    case "+":
                       x = stack.pop();
                       y = stack.pop();
                       //load x into the accumulator
                      opCode.setOperationCode(20);//set the opCode to load
                      operand.setOperand(x);//set the operand to reference the location of x
                      instrRegister.setInstructionRegister((opCode.getOperationCode() * 100)+operand.getOperand());//update the instrRegister
                      smlArray.add(instrRegister.getInstructionRegister(), instrCounter.getInstructionCounter());//add the instruction to the smlArray 
                      instrCounter.setInstructionCounter(instrCounter.getInstructionCounter() + 1);//increment the SML array indexay index
                      //add y into the accumulator
                       opCode.setOperationCode(30);//set the opCode to perform addition
                      operand.setOperand(y);//set the operand to reference the location of y
                      instrRegister.setInstructionRegister((opCode.getOperationCode() * 100) + operand.getOperand());//update the instrRegister
                       smlArray.add(instrRegister.getInstructionRegister(), instrCounter.getInstructionCounter());//add the instruction to the smlArray 
                      instrCounter.setInstructionCounter(instrCounter.getInstructionCounter() + 1);//increment the SML array index
                      //store the result in string value
                      //use the data counter to acquire the location of the temp storage area
                      //update the location in the operand
                      //decrement the data counter
                      //update the memory location of the result
                        opCode.setOperationCode(21);//set the opCode to store
                         operand.setOperand(dataCounter.getDataCounter());
                         instrRegister.setInstructionRegister((opCode.getOperationCode() * 100)+operand.getOperand());//update the instrRegister
                          smlArray.add(instrRegister.getInstructionRegister(), instrCounter.getInstructionCounter());//add the instruction to the smlArray 
                        instrCounter.setInstructionCounter(instrCounter.getInstructionCounter() + 1);//increment the SML array index
                         dataCounter.setDataCounter(dataCounter.getDataCounter() - 1);     
                         value = operand.getOperand();//update the location of the last operand as the temp result
                        break;
                    case "*":
                          x = stack.pop();
                       y = stack.pop();
                       //load x into the accumulator
                      opCode.setOperationCode(20);//set the opCode to load
                      operand.setOperand(x);//set the operand to reference the location of x
                      instrRegister.setInstructionRegister((opCode.getOperationCode() * 100)+operand.getOperand());//update the instrRegister
                       smlArray.add(instrRegister.getInstructionRegister(), instrCounter.getInstructionCounter());//add the instruction to the smlArray 
                       instrCounter.setInstructionCounter(instrCounter.getInstructionCounter() + 1);//increment the SML array index
                      //add y into the accumulator
                       opCode.setOperationCode(33);//set the opCode to multiply
                      operand.setOperand(y);//set the operand to reference the location of y
                      instrRegister.setInstructionRegister((opCode.getOperationCode() * 100) + operand.getOperand());//update the instrRegister
                        smlArray.add(instrRegister.getInstructionRegister(), instrCounter.getInstructionCounter());//add the instruction to the smlArray 
                       instrCounter.setInstructionCounter(instrCounter.getInstructionCounter() + 1);//increment the SML array index
                      //store the result in string value
                      //use the data counter to acquire the location of the temp storage area
                      //update the location in the operand
                      //decrement the data counter
                      //update the memory location of the result
                        opCode.setOperationCode(21);//set the opCode to store
                         operand.setOperand(dataCounter.getDataCounter());
                         instrRegister.setInstructionRegister((opCode.getOperationCode() * 100)+operand.getOperand());//update the instrRegister
                             smlArray.add(instrRegister.getInstructionRegister(), instrCounter.getInstructionCounter());//add the instruction to the smlArray 
                       instrCounter.setInstructionCounter(instrCounter.getInstructionCounter() + 1);//increment the SML array index
                         dataCounter.setDataCounter(dataCounter.getDataCounter() - 1);     
                         value = operand.getOperand();//update the location of the last operand as the temp result
                        break;
                     case "%":
                       x = stack.pop();
                       y = stack.pop();
                       //load x into the accumulator
                      opCode.setOperationCode(20);//set the opCode to load
                      operand.setOperand(x);//set the operand to reference the location of x
                      instrRegister.setInstructionRegister((opCode.getOperationCode() * 100)+operand.getOperand());//update the instrRegister
                       smlArray.add(instrRegister.getInstructionRegister(), instrCounter.getInstructionCounter());//add the instruction to the smlArray 
                                      instrCounter.setInstructionCounter(instrCounter.getInstructionCounter() + 1);//increment the SML array index
                      //add y into the accumulator
                       opCode.setOperationCode(34);//set the opCode to perform remainder operations
                      operand.setOperand(y);//set the operand to reference the location of y
                      instrRegister.setInstructionRegister((opCode.getOperationCode() * 100) + operand.getOperand());//update the instrRegister
                        smlArray.add(instrRegister.getInstructionRegister(), instrCounter.getInstructionCounter());//add the instruction to the smlArray 
                                      instrCounter.setInstructionCounter(instrCounter.getInstructionCounter() + 1);//increment the SML array index
                      //store the result in string value
                      //use the data counter to acquire the location of the temp storage area
                      //update the location in the operand
                      //decrement the data counter
                      //update the memory location of the result
                        opCode.setOperationCode(21);//set the opCode to store
                         operand.setOperand(dataCounter.getDataCounter());
                         instrRegister.setInstructionRegister((opCode.getOperationCode() * 100)+operand.getOperand());//update the instrRegister
                           smlArray.add(instrRegister.getInstructionRegister(), instrCounter.getInstructionCounter());//add the instruction to the smlArray 
                         instrCounter.setInstructionCounter(instrCounter.getInstructionCounter() + 1);//increment the SML array index
                         dataCounter.setDataCounter(dataCounter.getDataCounter() - 1);     
                         value = operand.getOperand();//update the location of the last operand as the temp result         
                        break;
                    case "/":
                        op1 = stack.pop();
                        op2 = stack.pop();
                          x = op2;
                          y = op1;
                       //load x into the accumulator
                      opCode.setOperationCode(20);//set the opCode to load
                      operand.setOperand(x);//set the operand to reference the location of x
                      instrRegister.setInstructionRegister((opCode.getOperationCode() * 100)+operand.getOperand());//update the instrRegister
                       smlArray.add(instrRegister.getInstructionRegister(), instrCounter.getInstructionCounter());//add the instruction to the smlArray 
                      instrCounter.setInstructionCounter(instrCounter.getInstructionCounter() + 1);//increment the SML array index
                      //add y into the accumulator
                       opCode.setOperationCode(32);//set the opCode to load
                      operand.setOperand(y);//set the operand to reference the location of y
                      instrRegister.setInstructionRegister((opCode.getOperationCode() * 100) + operand.getOperand());//update the instrRegister
                      smlArray.add(instrRegister.getInstructionRegister(), instrCounter.getInstructionCounter());//add the instruction to the smlArray 
                     instrCounter.setInstructionCounter(instrCounter.getInstructionCounter() + 1);//increment the SML array index
                      //store the result in string value
                      //use the data counter to acquire the location of the temp storage area
                      //update the location in the operand
                      //decrement the data counter
                      //update the memory location of the result
                        opCode.setOperationCode(21);//set the opCode to store
                         operand.setOperand(dataCounter.getDataCounter());
                         instrRegister.setInstructionRegister((opCode.getOperationCode() * 100)+operand.getOperand());//update the instrRegister
                          smlArray.add(instrRegister.getInstructionRegister(), instrCounter.getInstructionCounter());//add the instruction to the smlArray 
                          instrCounter.setInstructionCounter(instrCounter.getInstructionCounter() + 1);//increment the SML array index
                         dataCounter.setDataCounter(dataCounter.getDataCounter() - 1);     
                         value = operand.getOperand();//update the location of the last operand as the temp result
                        break;
                    case "-":
                    op1 = stack.pop();
                        op2 = stack.pop();
                          x = op2;
                          y = op1;
                       //load x into the accumulator
                      opCode.setOperationCode(20);//set the opCode to load
                      operand.setOperand(x);//set the operand to reference the location of x
                      instrRegister.setInstructionRegister((opCode.getOperationCode() * 100)+operand.getOperand());//update the instrRegister
                        smlArray.add(instrRegister.getInstructionRegister(), instrCounter.getInstructionCounter());//add the instruction to the smlArray 
                       instrCounter.setInstructionCounter(instrCounter.getInstructionCounter() + 1);//increment the SML array index
                      //add y into the accumulator
                       opCode.setOperationCode(31);//set the opCode to load
                      operand.setOperand(y);//set the operand to reference the location of y
                      instrRegister.setInstructionRegister((opCode.getOperationCode() * 100) + operand.getOperand());//update the instrRegister
                         smlArray.add(instrRegister.getInstructionRegister(), instrCounter.getInstructionCounter());//add the instruction to the smlArray 
                       instrCounter.setInstructionCounter(instrCounter.getInstructionCounter() + 1);//increment the SML array index
                      //store the result in string value
                      //use the data counter to acquire the location of the temp storage area
                      //update the location in the operand
                      //decrement the data counter
                      //update the memory location of the result
                        opCode.setOperationCode(21);//set the opCode to store
                         operand.setOperand(dataCounter.getDataCounter());
                         instrRegister.setInstructionRegister((opCode.getOperationCode() * 100)+operand.getOperand());//update the instrRegister
                           smlArray.add(instrRegister.getInstructionRegister(), instrCounter.getInstructionCounter());//add the instruction to the smlArray 
                          instrCounter.setInstructionCounter(instrCounter.getInstructionCounter() + 1);//increment the SML array index
                         dataCounter.setDataCounter(dataCounter.getDataCounter() - 1);     
                         value = operand.getOperand();//update the location of the last operand as the temp result
                         break;
                    case "^":
                         x = stack.pop();
                       y = stack.pop();
                       //load x into the accumulator
                      opCode.setOperationCode(20);//set the opCode to load
                      operand.setOperand(x);//set the operand to reference the location of x
                      instrRegister.setInstructionRegister((opCode.getOperationCode() * 100)+operand.getOperand());//update the instrRegister
                        smlArray.add(instrRegister.getInstructionRegister(), instrCounter.getInstructionCounter());//add the instruction to the smlArray 
                     instrCounter.setInstructionCounter(instrCounter.getInstructionCounter() + 1);//increment the SML array index
                      //add y into the accumulator
                       opCode.setOperationCode(35);//set the opCode to perform exponentiation
                      operand.setOperand(y);//set the operand to reference the location of y
                      instrRegister.setInstructionRegister((opCode.getOperationCode() * 100) + operand.getOperand());//update the instrRegister
                         smlArray.add(instrRegister.getInstructionRegister(), instrCounter.getInstructionCounter());//add the instruction to the smlArray 
                       instrCounter.setInstructionCounter(instrCounter.getInstructionCounter() + 1);//increment the SML array index
                      //store the result in string value
                      //use the data counter to acquire the location of the temp storage area
                      //update the location in the operand
                      //decrement the data counter
                      //update the memory location of the result
                        opCode.setOperationCode(21);//set the opCode to store
                         operand.setOperand(dataCounter.getDataCounter());
                         instrRegister.setInstructionRegister((opCode.getOperationCode() * 100)+operand.getOperand());//update the instrRegister
                           smlArray.add(instrRegister.getInstructionRegister(), instrCounter.getInstructionCounter());//add the instruction to the smlArray 
                           instrCounter.setInstructionCounter(instrCounter.getInstructionCounter() + 1);//increment the SML array index
                          dataCounter.setDataCounter(dataCounter.getDataCounter() - 1);     
                         value = operand.getOperand();//update the location of the last operand as the temp result
                        break;    
                }//end switch
                //push the result (temp memory location) onto the stack
                stack.push(value);
             }//end if, operator check
             
         }//end if
         /**
          * append the stack top value once the ")" is reached
          * if the current token is not a right parenthesis
          * pop the result on the stack 
          * assign the left operand the location of the result
          */
         else
         {
            //return the top of the stack
             int result = stack.pop();
             return result;
         }//end else
             counter++;
         }//end while 
         //print the stack
         simpleFunct.printStack(stack);
   }//end try
   catch(EmptyStackException e)
   {
       //TODO nothing
   }//end catch
   return 0;
}//end method
}
class PostfixEvaluatorTest
{
    public static void main(String[] args) {
       // PostfixEvaluator p = new PostfixEvaluator();
    }
}