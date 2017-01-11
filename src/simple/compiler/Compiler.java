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

package simple.compiler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.StringTokenizer;
import simple.exceptions.EmptyLineNumberException;
import simple.exceptions.InvalidAssignmentOperatorException;
import simple.exceptions.InvalidVariableNameException;
import simple.exceptions.InvalidVariableSeparatorException;
import simple.exceptions.OutOfMemoryException;
import simple.exceptions.SimpleSyntaxErrorException;
import simple.functions.Functions;
import simple.functions.InfixToPostfix;
import simple.functions.PostfixEvaluator;
import simple.records.SimpleRecord;
import static simple.register.CommandStatements.END;
import static simple.register.CommandStatements.GOTO;
import static simple.register.CommandStatements.IF;
import static simple.register.CommandStatements.INPUT;
import static simple.register.CommandStatements.LET;
import static simple.register.CommandStatements.PRINT;
import static simple.register.CommandStatements.REM;
import simple.register.DataCounter;
import simple.register.Flags;
import simple.register.InstructionCounter;
import simple.register.InstructionRegister;
import simple.register.Operand;
import simple.register.OperationCode;
import simple.register.SMLArray;
import simple.register.SymbolTable;
import simpletron.hardware.OutputStream;

/**
 * This class receives a simple file (plain text) and parses it to produce SML machine language code
 * @author gerry dreamer
 */
public class Compiler {
    /**
     * allows the reading of a simple file line by line
     */
    private BufferedReader bufferedReader;
    /**
     * wrapped inside a bufferedReader object to allow efficient processing of text based streams
     */
    private FileReader fileReader;
    /**
     * this object instance will be used in future implementations, possibly
     * allows for splitting of string tokens within a line in a simple file
     * the use of this object has been replaced by method split of type String
     * @see java.lang.String#split(java.lang.String) 
     */
    private StringTokenizer stringTokenizer;
    /**
     * wraps a specified simple file to allow for use within this object 
     */
    private File currFile;
    /**
     * the current placeholder for string tokens within a line
     */
    private SimpleRecord simpleRecord = new SimpleRecord();
    /**
     * specifies the location of SML data within the SML array
     */
    private DataCounter dataCounter = new DataCounter();
    /**
     * an array based object representing unresolved references that occur in the first pass
     */
    private Flags flags = new Flags();
    /**
     * specifies the location of SML instruction within the SML array
     */
    private InstructionCounter instrCounter = new InstructionCounter();
    /**
     * represents the operand of the final SMl instruction
     */
    private Operand operand = new Operand();
    /**
     * represents the opcode of the final SMl instruction
     */
    private OperationCode opCode = new OperationCode();
    /**
     * represents the current array temporarily holding the SML instructions and data before they are actually written to an SML file
     */
    private SMLArray smlArray = new SMLArray();
    /**
     * a reference table for the simple file symbols 
     */
    private SymbolTable symbolTable = new SymbolTable();
    /**
     * contains essential functions for compiling a simple file
     */
    private Functions functions = new Functions();
    /**
     * converts a simple expression into postfix notation
     */
    private InfixToPostfix infixToPostfix;
    /**
     * evaluates a postfix expression, and provides a result
     */
    private PostfixEvaluator postfixEvaluator;
    /**
     * temporarily holds an SML instruction before it is appended to the SML array
     */
    private InstructionRegister instrRegister = new InstructionRegister();
    /**
     * the default output stream for this object
     */
    private OutputStream outputStream = new simpletron.hardware.OutputStream();
    /**
     * a temporary buffer for the current expression in a let statement
     */
    private StringBuilder expressionBuilder = new StringBuilder();
    /**
     * the file destination for the SML output
     */
    private File smlFile;
    /**
     * utilities for manipulating files
     */
    private com.dreamer.file.FileFunctions fileFunct = new com.dreamer.file.FileFunctions();
    /**
     * The default constructor for this object
     * @param currFile {@link simple.compiler.Compiler#currFile}
     * @param smlFile {@link simple.compiler.Compiler#smlFile}
     */
    public Compiler(File currFile, File smlFile)
    {
        //------------------preliminaries------------------------ 
     try
     {
         this.currFile = currFile;//initialize the current file (simple file)
         fileFunct.validateFileExtension(currFile, "sim","simple");//validate the file name extension
     }//end try
     catch(com.dreamer.file.exceptions.InvalidFileNameExtensionException e)
     {
         this.currFile = null;//set the file name to null
           new simpletron.hardware.OutputStream().writeToGUI(e);
     }//end catch    
    try
     {
        this.smlFile = smlFile;//initialize the SML output file (SML output)  
       fileFunct.validateFileExtension(smlFile, "sml","SML");//validate the file name extension
     }//end try
    catch(com.dreamer.file.exceptions.InvalidFileNameExtensionException e)
     {
          this.smlFile = null;
            new simpletron.hardware.OutputStream().writeToGUI(e);
     }//end catch
	 //------------initialization------------------------------------
	 //create a call to the initialization method
     this.initialization();//involves the whole compilation process     
     //close the simple file
     this.closeFile();//ensures that the buffered reader is through with reading the file
     //---------------finalizations---------------------------------------------
     //record the SML instructions produced to an external .*sml file
     //this functionality is called directly from the writeSMLFile method
     //an external object handles the writing of the SML array from the smlArray object, and direclty to the file destination
     this.finalization();//finalize methods
	 //-----------print to command line---------------- (for debugging only)------------------------
     //print the symbolTable
     functions.printSymbolTable(symbolTable);
     //print the SML array
     functions.printSMLArray(smlArray);
     //print the flags array
     functions.printFlagsArray(flags);    
    }
    /**
     * initializes the instance variables of this class
     */
    public void initialization()
    {
        //initialize the infixToPostfix object
        infixToPostfix = new simple.functions.InfixToPostfix();        
        this.openFile();//initialize the fileReader and BufferedReader
        //initialize the postfix evaluator
      this.postfixEvaluator = new simple.functions.PostfixEvaluator(dataCounter, instrCounter, operand, opCode, smlArray, symbolTable, instrRegister);
        //convert the lines in the simple file into a string 
      try
      {
          simpleRecord.setLineStatement("");//initialize the record to a default white space character
          while(simpleRecord.getLineStatement() != null)
          {
             simpleRecord.setLineStatement(bufferedReader.readLine());//read the current line of the text file
             if(simpleRecord.getLineStatement() != null)
             {
              firstPass(simpleRecord.getLineStatement());//update the first pass with the line string   
             }//end if             
          }//end while
          //call the second pass of the compiler to resolve unresolved references in the program
		  //remember, the second pass occurs once parsing of the whole simple file has been completed
		  //this ensures efficient identification of unresolved references, and their replacement with the identified references
         secondPass();
      }
      catch(IOException e)//throw an exception if the buffered reader fails to read the current file (possibly a non-text file)
      {
          outputStream.writeToGUI(e, "Error in reading the file");
      }//end catch 
    catch(NullPointerException e)//occurs if a null file location is given, possibly due to the file name extension validation mechanism
    {
         outputStream.writeToGUI(e,"The file received is either invalid or null.");
    }
    }
    /**
     * performs the first pass of the simple file compilation
     * @param lineStatement the current line string within a simple file 
     */
    public void firstPass(String lineStatement)
    {  
     /**
     * an character value of the variable name
     */
    char varName = ' ';    
        //initialize the stringTokenizer
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
      try
      {         
          /**
           * check if the first token in the string is an integer
           * append the lineno to the smlArray index where the instructions for a 
           * particular line begin
           */
          if(functions.isDigit(tokens[0]))
          {
              //locate the symbol first in the symbol table, if it is already present, do not add it, and do not increment the symbol table
              if(functions.confirmIfDuplicate(symbolTable, Integer.parseInt(tokens[0]),'L')==false)
              {
                    symbolTable.addTableEntry(Integer.parseInt(tokens[0]), 'L', instrCounter.getInstructionCounter(), symbolTable.getSymbolIndex());
                  //increment the symbol table index
                  symbolTable.setSymbolIndex(symbolTable.getSymbolIndex() + 1);
              }//end if
              else//if the symbol has a duplicate, do nothing
              {
                  //TODO nothing
              }
          }
          /**
           * if the first token is not a valid line number, throw an error
           */
             else
            {
               throw new EmptyLineNumberException("Error in locating the line number (null line number error).");            
            }//if the first token is not a line number throw an exception
             switch(tokens[1])//switch between the second tokens within a simple file
            {
                case REM:
                    //includes comments that are ignored by the compiler
                    //do nothing per se            
                break;
                case INPUT:                                  
    //input
    //ignore tokens at index 1, and 2
    //create a temp string s
    //append the tokens at indices 2 - n into s
     s = "";
    for(int i=2; i<tokens.length; i++)
    {
        s += tokens[i] + " ";
    }
    //ensure that the expression contains valid separators       
     functions.validateSeparator(s, ',');
    //create a temp string s1, which contains a trimmed version of s
     s1 = s.trim();
    //create String[] s2, which contains the individual tokens of string s1
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
                  /**
                   * assign a reference character to the variable name the user has input
                   * ensure that the variable is a single character
                   */
                    varName = s4[i].charAt(0); //next validate if it's a valid var name
                    if(functions.searchAlphabet(varName) != -1 && functions.searchAlphabet(varName) != 26)
                    {
                        //update the value to the symbolTable if the values do not already exist there
                        //eliminate duplicates
                       if(functions.confirmIfDuplicate(symbolTable, varName,'V')==false)
                       {
                            symbolTable.addTableEntry(varName, 'V', dataCounter.getDataCounter(), symbolTable.getSymbolIndex()); 
                            // System.out.println("var "+varName+" index: "+symbolTable.getSymbolIndex());
                            opCode.setOperationCode(10);//this command produces a read instruction
                            operand.setOperand(dataCounter.getDataCounter());//the operand equals the location of the variable
                            //instr = opCode*100 + operand
                            instrRegister.setInstructionRegister((opCode.getOperationCode()*100)+operand.getOperand());//update the instructions register
                            //append the instruction to the virtual SML array
                            smlArray.add(instrRegister.getInstructionRegister(), instrCounter.getInstructionCounter());
                            //increment the counters if the symbol is not a duplicate
                            symbolTable.setSymbolIndex(symbolTable.getSymbolIndex() + 1);//increment the symbol index
                            dataCounter.setDataCounter(dataCounter.getDataCounter() - 1);//decrement the data counter
                            instrCounter.setInstructionCounter(instrCounter.getInstructionCounter() + 1);//increment the SML array index
                       }//end if                        
                    }
                    else
                    {
                        //throw an invalid variable name exception
                        throw new InvalidVariableNameException("An invalid Variable Name has been encountered.");
                    }                              
    }//end for loop, variable loop, loop between the variables after the input statement                   
                break;                
                case PRINT:
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
     functions.validateSeparator(s, ',');
    //create a temp string s1, which contains a trimmed version of s
     s1 = s.trim();
    //create String[] s2, which contains the individual tokens of string s1
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
             //set the next token as a var                 
                    /**
                    * assign a reference character to the variable name the user has input
                    * ensure that the variable is a single character
                    */
                    varName = s4[i].charAt(0); //next validate if it's a valid var name
                       if(functions.searchAlphabet(varName) != -1 && functions.searchAlphabet(varName) != 26)
                    {
                        //check if the value exists in the symbolTable
                        //use the confirmIfDuplicateMethod if it returns true
                        /**
                         * if the variable exists within the symbolTable
                         * produce the corresponding SML code
                         */
                        if(functions.confirmIfDuplicate(symbolTable, varName,'V')== true)
                        {
                            //set the opCode based on the location of the variable
                            //first find the location of the symbol
                            int location = functions.findTableIndexBasedOnSymbol(symbolTable, varName,'V');
                            //create a temporary tableEntry object based on the location of the symbol
                            simple.register.TableEntry tableEntry = symbolTable.getTableEntry(location);
                            //now we have access to the tableEntry containing the varName
                            opCode.setOperationCode(11);//this command produces a write instruction
                            operand.setOperand(tableEntry.getLocation());//the operand is set to the location of the symbol
                             //instr = opCode*100 + operand
                            instrRegister.setInstructionRegister((opCode.getOperationCode()*100)+operand.getOperand());//update the instructions register
                            //append the instruction to the virtual SML array
                            smlArray.add(instrRegister.getInstructionRegister(), instrCounter.getInstructionCounter());
                            //increment the smlArray
                              instrCounter.setInstructionCounter(instrCounter.getInstructionCounter() + 1);//increment the SML array index
                        }//end if
                        /**
                         * if the variable doesn't exist within the symbolTable 
                         * throw an invaidVariable name exception
                         */
                        else
                        {
                            //TODO nothing for now
                            //future implementations shall use this space to provide additional functions
                             throw new InvalidVariableNameException("A reference to an undeclared variable has been encountered.\n"
                                     + "Please specify a correct reference to a declared variable name.");
                        }
                    }//end if
                      else
                    {
                        //throw an invalid variable name exception
                        throw new InvalidVariableNameException("An invalid Variable Name has been encountered.");
                    }    
    }//end for loop, print variables loop    
                break;
                case END:
                    /**
                     * most of the implementations here are fixed
                     * since, this command signals the end of the program
                     * the opCode and operand are fixed
                     * +4300
                     */
                     opCode.setOperationCode(43);//this command produces a Halt instruction
                            operand.setOperand(0);//the operand is set to a default value of 0 (fixed value)
                             //instr = opCode*100 + operand
                             instrRegister.setInstructionRegister((opCode.getOperationCode()*100)+operand.getOperand());//update the instructions register
                            //append the instruction to the virtual SML array
                            smlArray.add(instrRegister.getInstructionRegister(), instrCounter.getInstructionCounter());
                            //increment the smlArray
                             instrCounter.setInstructionCounter(instrCounter.getInstructionCounter() + 1);//increment the SML array index
                break;
                case LET:
                    //set the next token as a var                 
                    /**
                    * assign a reference character to the variable name the user has input
                    * ensure that the variable is a single character
                    */
                    varName = tokens[2].charAt(0); //next validate if it's a valid var name
                       if(functions.searchAlphabet(varName) != -1 && functions.searchAlphabet(varName) != 26)
                       {
                           /**
                            * there are two cases involved here:
                            * either the variable declared is in the symbol table or not
                            * if it is present in the symbol table:
                            * that means it has already been appended to the smlArray
                            * if it is not present in the symbol table:
                            * create new declarations for it
                            */
                           /**
                            * if the symbol is already contained in the symbolTable
                            * acquire its location and load it in the accumulator
                            */
                           if(functions.confirmIfDuplicate(symbolTable, varName,'V') == true)
                           {      
                               //the commented blocks of code indicate a deprecated concept that may be considered for future revisions
                               //should the left operand be loaded first in the let statements? , despite its redundancy,
                               //we can use this block of code to further implement the optimization process
                               //this process applies to both previously declared variables, or newly declared ones
//                             //set the opCode based on the location of the variable
//                            //first find the location of the symbol
//                            int location = functions.findTableIndexBasedOnSymbol(symbolTable, varName,'V');
//                            //create a temporary tableEntry object based on the location of the symbol
//                            simple.register.TableEntry tableEntry = symbolTable.getTableEntry(location);
//                            //now we have access to the tableEntry containing the varName
//                            opCode.setOperationCode(20);//this command produces a load instruction
//                            operand.setOperand(tableEntry.getLocation());//the operand is set to the location of the symbol
//                             //instr = opCode*100 + operand
//                            instrRegister.setInstructionRegister((opCode.getOperationCode()*100)+operand.getOperand());//update the instructions register
//                            //append the instruction to the virtual SML array
//                            smlArray.add(instrRegister.getInstructionRegister(), smlArray.getArrayIndex());
//                            //increment the smlArray
//                              smlArray.setArrayIndex(smlArray.getArrayIndex() + 1);//increment the SML array
                           }//end inner if
                           /**
                            * if the symbol is not contained in the symbolTable:
                            * let indicates an assignment operation, 
                            * append the variable to the symbolTable
                            * increment the symbolIndex 
                            * decrement the dataCounter
                            */
                           else
                           {
                              symbolTable.addTableEntry(varName, 'V', dataCounter.getDataCounter(), symbolTable.getSymbolIndex());//append the symbol to the table
                              //the commented blocks of code indicate a deprecated concept that may be considered for future revisions
                               //should the left operand be loaded first in the let statements? , despite its redundancy,
                               //we can use this block of code to further implement the optimization process
                               //this process applies to both previously declared variables, or newly declared ones
//                             //load this variable to the accumulator
//                             opCode.setOperationCode(20);//indicates that the varName has been loaded to the accumulator
//                             operand.setOperand(dataCounter.getDataCounter());//set the current data counter as the operand
//                              //instr = opCode*100 + operand
//                             instrRegister.setInstructionRegister((opCode.getOperationCode()*100)+operand.getOperand());//update the instructions register
//                             //append the instruction to the sml array
//                             smlArray.add(instrRegister.getInstructionRegister(), smlArray.getArrayIndex());                             
//                             //increment or decrement the indices affected
//                             smlArray.setArrayIndex(smlArray.getArrayIndex() + 1);
                             symbolTable.setSymbolIndex(symbolTable.getSymbolIndex() + 1);
                             dataCounter.setDataCounter(dataCounter.getDataCounter() - 1);
                          }//end inner else
                           /**
                            * once the origin of the varName has been established
                            * work on the tokens after the varName declarations
                            */
                            /**
                              * if the token after the variable is an assignment operator
                              * append the remaining tokens into the symbolTable if they are not already there
                              * loop through the rest of the tokens array
                              */
                             if(tokens[3].equals("="))
                             {
                                for(int i=4; i<tokens.length; i++)//loop thru the rest of the array
                                {
                                    //if the tokens are not operators 
                                    //separate the tokens as either digits(constants) and variables
                                    //ignore operators for now
                                    /**
                                     * if the current tokens are not operators
                                     */
                                    if(functions.isBinaryOperator(tokens[i])==false && functions.isRelationalOperator(tokens[i])==false && functions.isUnwantedCharacter(tokens[i])==false)
                                    {
                                      //separate the tokens into two groups: the constants and the variables
                                        /**
                                         * if the current token is a constant
                                         * ensure there are no duplicates
                                         * append the constants to the symbol table using the data counter
                                         * increment the symbolIndex and decrement the data counter
                                         * place the constants into the smlArray based on the dataCounter as the smlArray index
                                         * directly place the constants into memory
                                         * only the instructionRegister is used here:
                                         * 
                                         */
                                        if(functions.isDigit(tokens[i])==true)
                                        {//eliminate duplicates first
                                          if(functions.confirmIfDuplicate(symbolTable, Integer.parseInt(tokens[i]),'C')==false)
                                          {
                                         symbolTable.addTableEntry(Integer.parseInt(tokens[i]), 'C', dataCounter.getDataCounter(), symbolTable.getSymbolIndex());
                                         instrRegister.setInstructionRegister(Integer.parseInt(tokens[i]));
                                         smlArray.add(instrRegister.getInstructionRegister(), dataCounter.getDataCounter());
                                         dataCounter.setDataCounter(dataCounter.getDataCounter() - 1);//decrement the data counter
                                         symbolTable.setSymbolIndex(symbolTable.getSymbolIndex() + 1);//increment the symbol index
                                          }//end inner if
                                        }//end inner if
                                        /**
                                         * if  the current token is a variable
                                         * make sure this is a valid variable name
                                         * ensure that the variable is not a duplicate
                                         * append the variables to the symbolTable, with type 'V'
                                         * generate the location of the symbols
                                         * increment the symbolIndex
                                         * decrement the dataCounter
                                         * the simple language specification assumes that all variable declarations and assignments are made in the input statement
                                         * or before the assignment operator in the let statement, 
                                         * so any undeclared variable is added to the symbolTable only
                                         * This step appears to be redundant
                                         */
                                        else
                                        {
                                             //set the next token as a var                 
                                                /**
                                               * assign a reference character to the variable name the user has input
                                               * ensure that the variable is a single character
                                               */
                                                varName = tokens[i].charAt(0); //next validate if it's a valid var name
                                                 if(functions.searchAlphabet(varName) != -1 && functions.searchAlphabet(varName) != 26)
                                                 {
                                                     //ensure that this variable is not a duplicate
                                                     if(functions.confirmIfDuplicate(symbolTable, varName,'V')==false)
                                                     {
                                                         symbolTable.addTableEntry(varName, 'V', dataCounter.getDataCounter(), symbolTable.getSymbolIndex());                                                        
                                                         //increment indices
                                                       symbolTable.setSymbolIndex(symbolTable.getSymbolIndex() + 1);
                                                       dataCounter.setDataCounter(dataCounter.getDataCounter() - 1);
                                                     }//end if
                                                 }//end inner if
                                                 else
                                                 {
                                                  throw new simple.exceptions.InvalidVariableNameException("An invalid variable name has been encountered");
                                                 }//end inner else
                                        }//end inner else, variable check
                                    }//end if, constant and variable check
                                    //acquire the string expression in a buffer
                                     expressionBuilder.append(tokens[i]).append(" ");
                                }//end for loop, loop of tokens after the "=" operator
                                /**
                                 * once a string representation of the expression has been acquired
                                 * convert the expression to infix notation
                                 * use the customized simple language infix to postfix converter
                                 * create a temp variable to hold the postfix expression
                                 * evaluate the postfix expression
                                 */                                
                              //   System.out.println("infix: "+expressionBuilder.toString());
                                 String postfix = this.infixToPostfix.convertToPostfix(expressionBuilder.toString());//convert the expression to a string
                                // System.out.println("postfix: "+postfix);
                                 //evaluate the postfix expression
                                 //load the result into the accumulator                                 
                              int result = postfixEvaluator.evaluatePostfixExpression(postfix);//evaluate the postfix expression
                              opCode.setOperationCode(20);//create the load instruction
                              operand.setOperand(result);//set the result as the operand (temporary storage for the result of the expression)
                               instrRegister.setInstructionRegister((opCode.getOperationCode() * 100)+operand.getOperand());//update the instrRegister
                               smlArray.add(instrRegister.getInstructionRegister(), instrCounter.getInstructionCounter());//add the instruction to the smlArray 
                            instrCounter.setInstructionCounter(instrCounter.getInstructionCounter() + 1);//increment the SML array index
                              //store the result in the location of the left operand
                              //validate the varName 
                              //ensure it really exists within the table
                              //find the location of this variable
                              //set the opCode for store 
                              //set the operand to the location of the left operand
                              //update the instructionRegister
                              //add the values to the smlArray
                              //increment the smlArrayIndex
                              varName = tokens[2].charAt(0);
                                 if(functions.searchAlphabet(varName) != -1 && functions.searchAlphabet(varName) != 26)
                                 {
                                     if(functions.confirmIfDuplicate(symbolTable, varName, 'V')==true)
                                     {
                                        //first find the location of the symbol
                                        int location = functions.findTableIndexBasedOnSymbol(symbolTable, varName,'V');
                                        //create a temporary tableEntry object based on the location of the symbol
                                        simple.register.TableEntry tableEntry = symbolTable.getTableEntry(location);
                                        opCode.setOperationCode(21);//indicate that the program is about to store
                                        operand.setOperand(tableEntry.getLocation());
                                        instrRegister.setInstructionRegister((opCode.getOperationCode() * 100)+operand.getOperand());//update the instrRegister
                                        smlArray.add(instrRegister.getInstructionRegister(), instrCounter.getInstructionCounter());//add the instruction to the smlArray 
                                        instrCounter.setInstructionCounter(instrCounter.getInstructionCounter() + 1);//increment the SML array index
                                     }//end duplicate check
                                 }//end valid variable name check
                                 else
                                 {
                                     throw new simple.exceptions.InvalidVariableNameException("An invalid variable name has been encountered.");
                                 }
                                 expressionBuilder.delete(0, expressionBuilder.length());//delete the current strings in the expressionBuilder
                                 //rebuild the expressions again
                             }//end inner if
                             /**
                              * if the token after the variable is not an assignment operator
                              */
                             else
                             {
                                 //TODO
                                 //optionally throw an exception here invalidAssignmentOperator
                                 throw new InvalidAssignmentOperatorException("An invalid assignment operator has been used to assign values");
                             }//end inner else
                       }//end if
                         else
                        {
                            //throw an invalid variable name exception
                            throw new InvalidVariableNameException("An invalid Variable Name has been encountered.");
                        }//end outer else    
                break;               
                case IF:
                    /**
                     * This array (tempLocation) of four elements is used to store locations of variables or constants,
                     * temporarily, before they are used within the relational operators
                     * as a default the left operand's location is stored at tempLocation[0] 
                     * this allows for manipulation of both the left and right operands during comparisons
                     */
                    int[] tempLocation = new int[4];
                   //perform conditional branching 
                    //check if the variable declaration exists within the symbolTable
                    //if not append it to the symbol table and increment the instrCounter
                      /**
                   * assign a reference character to the variable name the user has input
                   * ensure that the variable is a single character
                   */
                      varName = tokens[2].charAt(0); //next validate if it's a valid var name
                       if(functions.searchAlphabet(varName) != -1 && functions.searchAlphabet(varName) != 26)
                       {
                           //check if the varName exists
                           /**
                            * if the varName exists in the symbolTable
                            */
                           if(functions.confirmIfDuplicate(symbolTable, varName, 'V')==true)
                           {
                              //TODO
                              //load the variable to the accumulator
                               //set the opCode to 20
                               //set the operand to the location of the varName in the symbolTable
                               //set tempLoation[0] to reflect the location of the left operand
                               //update the instruction register
                               //append the val to the instrRegister using the instrCounter
                               //increment the instrCounter
                               opCode.setOperationCode(20);//load the left operand to the accumulator
                               //first find the location of the symbol
                                int location = functions.findTableIndexBasedOnSymbol(symbolTable, varName,'V');
                                //create a temporary tableEntry object based on the location of the symbol
                                simple.register.TableEntry tableEntry = symbolTable.getTableEntry(location);
                                operand.setOperand(tableEntry.getLocation());
                                tempLocation[0] = tableEntry.getLocation();
                                instrRegister.setInstructionRegister((opCode.getOperationCode() * 100)+operand.getOperand());//update the instrRegister
                                smlArray.add(instrRegister.getInstructionRegister(), instrCounter.getInstructionCounter());
                                instrCounter.setInstructionCounter(instrCounter.getInstructionCounter() + 1);
                           }//end if, duplicate var check
                           /**
                            * if the varName does not exist in the symbolTable
                            * append the symbol to the symbolTable and increment the symbolTable index
                            */
                           else
                           {
                              symbolTable.addTableEntry(varName, 'V', dataCounter.getDataCounter(), symbolTable.getSymbolIndex());
                              //load this variable to the accumulator
                              //set the opCode to 20
                              //set the operand to the current data counter
                              //set tempLoation[0] to reflect the location of the left operand (data counter)
                              //update the instrRegister
                              //append to the SMl array based on the instrCounter
                              //increment the instrCounter
                              //decrement the dataCounter
                              //increment the symbolIndex
                               opCode.setOperationCode(20);//load the left operand to the accumulator
                               operand.setOperand(dataCounter.getDataCounter());
                               tempLocation[0] = dataCounter.getDataCounter();
                               instrRegister.setInstructionRegister((opCode.getOperationCode() * 100)+operand.getOperand());
                                smlArray.add(instrRegister.getInstructionRegister(), instrCounter.getInstructionCounter());
                              //increment the indices affected
                                instrCounter.setInstructionCounter(instrCounter.getInstructionCounter() + 1);
                              symbolTable.setSymbolIndex(symbolTable.getSymbolIndex() + 1);
                              dataCounter.setDataCounter(dataCounter.getDataCounter() - 1);
                           }//end else, non duplicate var check
                        /**
                         * once the locations of the variables have been set
                         * create a temporary constant if tokens[4] is an integer
                         * proceed with the conditional branching
                         * ensure that the token at index 3 is a relational operator
                         * declare the location of the varName in the symbolTable
                         * create global variables for this effect
                         */
                        if(functions.isRelationalOperator(tokens[3])==true)
                        {
                               int constant = 0;//create a temporary constant
                               //create variable location 
                               int variableLocation = -1;  
                               /**
                                * if the fourth token is a variable name
                                * modify variableLocation to the exact location of the variable in the symbol table
                                */
                               if(functions.isDigit(tokens[4])==false)
                               {
                                   varName = tokens[4].charAt(0); //assign the token at index 4 the variable name
                                   /**
                                    * validate that the varName declaration is a valid one
                                    */
                                        if(functions.searchAlphabet(varName) != -1 && functions.searchAlphabet(varName) != 26)
                                         {
                                        /**
                                         * check if the varName exists within the symbol table
                                         * this is confirmed if the confirmIfDuplicate method returns true
                                         * 1. if the varName is present in the symbolTable
                                         * - update the variable location
                                         * 2. if the varName is absent in the symbolTable
                                         * - append the varName to the symbol table
                                         * - set the variable location to reflect the current value in the data counter
                                         * - increment the symbol index
                                         * - decrement the data counter
                                         */
                                   if(functions.confirmIfDuplicate(symbolTable, varName, 'V')==true)
                                   {
                                       //TODO find the exact location of this variable
                                       //first find the location of the symbol
                                        int location = functions.findTableIndexBasedOnSymbol(symbolTable, varName,'V');
                                        //create a temporary tableEntry object based on the location of the symbol
                                        simple.register.TableEntry tableEntry = symbolTable.getTableEntry(location);
                                        variableLocation = tableEntry.getLocation();
                                   }//end if , duplicate var check
                                   /**
                                    * if the varName is not present in the symbolTable append it there
                                    * update the variableLocation
                                    * increment the symbolTableIndex
                                    * decrement the data counter
                                    */
                                   else
                                   {
                                       //append the varName to the symbolTable
                                        symbolTable.addTableEntry(varName, 'V', dataCounter.getDataCounter(), symbolTable.getSymbolIndex());
                                        //set the variable location to the value of the data counter
                                        variableLocation = dataCounter.getDataCounter();
                                       //increment the indices affected
                                        symbolTable.setSymbolIndex(symbolTable.getSymbolIndex() + 1);
                                        dataCounter.setDataCounter(dataCounter.getDataCounter() - 1);
                                   }//end else , duplicate var check 
                               }//end variable name check   
                               else//if the varName is invalid throw an exception
                             {
                                throw new simple.exceptions.InvalidVariableNameException("An invalid variable name declaration has been encountered.");  
                             }//end else, var at index 4 check          
                            }//end if, var at index 4 check                            
                 /**
                 * if the token at index 4 is a constant
                 * validate if the constant exists in the symbolTable
                 * if the constant exists within the symbol table
                 * update the variable location
                 * if the constant is not within the symbol table
                 * append the constant to the symbol table
                 * set the variable location to reflect the data counter
                 * decrement the data counter
                 */
    else if(functions.isDigit(tokens[4])==true)
       {
          constant = Integer.parseInt(tokens[4]);//assign the token at index 4 the constant value
          /**
           * if the constant exists within the symbol table
           * create a temp location of type int to represent the index of the tableEntry where the constant is located
           * also create a temp tableEntry object to update the variable location
           * let  <code>variableLocation = tableEntry.getLocation()</code>
           */
          if(functions.confirmIfDuplicate(symbolTable, constant, 'C')==true)
          {
                //TODO find the exact location of this variable
                //first find the location of the symbol
                 int location = functions.findTableIndexBasedOnSymbol(symbolTable, constant,'C');
                 //create a temporary tableEntry object based on the location of the symbol
                 simple.register.TableEntry tableEntry = symbolTable.getTableEntry(location);
                 variableLocation = tableEntry.getLocation();
          }//end if, duplicate check
          /**
           * if the constant does not exist within the symbolTable:
           * append the constant to the symbolTable
           * update the variable location
           * increment the symbolTable index 
           * decrement the data counter
           */
          else
          {
               //append the varName to the symbolTable
                symbolTable.addTableEntry(constant, 'C', dataCounter.getDataCounter(), symbolTable.getSymbolIndex());
                //set the variable location to the value of the data counter
                variableLocation = dataCounter.getDataCounter();
               //increment the indices affected
                symbolTable.setSymbolIndex(symbolTable.getSymbolIndex() + 1);
                dataCounter.setDataCounter(dataCounter.getDataCounter() - 1);
          }//end else, non duplicate check
       }//end constant check                                
               // System.out.println("location of "+tokens[4]+" is "+variableLocation);  
                               /**
                              * perform processing based on the relational operator                              
                              * switch between the token at index 3
                              * the current varName character rep the fourth token in the line
                              */                            
                             //switch between the operators
                            if(variableLocation != -1)
                            {
                                 switch(tokens[3])//begin switch
                             {
                                 case "==":
                                     //set the opCode to 31 (subtraction)
                                     //set the operand to correspond to the variableLocation
                                     //update the instrRegister
                                     //append the instruction to the SMl array based on the instruction counter
                                     //increment the instrCounter
                                     opCode.setOperationCode(31);
                                     operand.setOperand(variableLocation);
                                     instrRegister.setInstructionRegister((opCode.getOperationCode() * 100) + operand.getOperand());
                                     smlArray.add(instrRegister.getInstructionRegister(), instrCounter.getInstructionCounter());
                                     instrCounter.setInstructionCounter(instrCounter.getInstructionCounter() + 1);
                                     //set the opCode to 42 (branch zero)
                                     opCode.setOperationCode(42);                                       
                                     break;
                                 case "<":
                                     //set the opCode to 31 (subtraction)
                                     //set the operand to correspond to the variableLocation
                                     //update the instrRegister
                                     //append the instruction to the SMl array based on the instruction counter
                                     //increment the instrCounter
                                     opCode.setOperationCode(31);
                                     operand.setOperand(variableLocation);
                                     instrRegister.setInstructionRegister((opCode.getOperationCode() * 100) + operand.getOperand());
                                     smlArray.add(instrRegister.getInstructionRegister(), instrCounter.getInstructionCounter());
                                     instrCounter.setInstructionCounter(instrCounter.getInstructionCounter() + 1);
                                     //set the opCode to 41 (branch negative)
                                     opCode.setOperationCode(41);                                    
                                     break;
                                  case "<=":
                                     //since the left operand is already loaded into the accumulator:
                                      //set opCode to 37 (MINIMUM)
                                      //set operand to variableLocation
                                      //update the instrRegister (+37...)
                                      //append the instruction to the SML array
                                      //increment the instructionCounter
                                     opCode.setOperationCode(37);
                                     operand.setOperand(variableLocation);
                                     instrRegister.setInstructionRegister((opCode.getOperationCode() * 100) + operand.getOperand());
                                     smlArray.add(instrRegister.getInstructionRegister(), instrCounter.getInstructionCounter());
                                     instrCounter.setInstructionCounter(instrCounter.getInstructionCounter() + 1);
                                     //store the result in a temporary memory location
                                     //update the location to tempLocation[0] before incrementing the data counter
                                     //set opCode to 21 (Store)
                                     //set operand to the current data counter
                                     //the data counter helps identify the temporary storage area
                                     //update the location in the operand
                                     //set tempLocation[1] to the current value of the data counter
                                     //increment the instructionCounter
                                     //decrement the dataCounter
                                     opCode.setOperationCode(21);//set the opCode to store
                                     operand.setOperand(dataCounter.getDataCounter());
                                     tempLocation[1] = dataCounter.getDataCounter();
                                     instrRegister.setInstructionRegister((opCode.getOperationCode() * 100)+operand.getOperand());//update the instrRegister
                                     smlArray.add(instrRegister.getInstructionRegister(), instrCounter.getInstructionCounter());//add the instruction to the smlArray 
                                     instrCounter.setInstructionCounter(instrCounter.getInstructionCounter() + 1);//increment the SML array index
                                     dataCounter.setDataCounter(dataCounter.getDataCounter() - 1);
                                     //load tempLocation[0]
                                     //update the instrCounter 
                                     //append to sml array
                                     //increment the instrCounter
                                      opCode.setOperationCode(20);
                                      operand.setOperand(tempLocation[0]);
                                      instrRegister.setInstructionRegister((opCode.getOperationCode() * 100) + operand.getOperand());
                                      smlArray.add(instrRegister.getInstructionRegister(), instrCounter.getInstructionCounter());
                                      instrCounter.setInstructionCounter(instrCounter.getInstructionCounter() + 1);
                                     //subtract tempLocation[1]
                                       //update the instrCounter 
                                     //append to sml array
                                     //increment the instrCounter
                                     opCode.setOperationCode(31);
                                     operand.setOperand(tempLocation[1]);
                                     instrRegister.setInstructionRegister((opCode.getOperationCode() * 100) + operand.getOperand());
                                     smlArray.add(instrRegister.getInstructionRegister(), instrCounter.getInstructionCounter());
                                     instrCounter.setInstructionCounter(instrCounter.getInstructionCounter() + 1);
                                     //create opCode for branching zero 
                                     opCode.setOperationCode(42);
                                     break;     
                                 case ">":
                                     //a quick overview of the > function:
                                     //since the left operand has already been loaded
                                     //create a constant of a value equal to 1
                                     //check if the symbol [1] of type INT is present in the symbol table
                                     //if it is present
                                     //receive its location and append it to tempLocation[1]
                                     //if it is absent 
                                     //append it to the symbol table and update it to the SML array
                                     //receive its location and append it to tempLocation[1] (this is preferably the data counter)
                                     //add the constant to the accumulator, where the operand = tempLocation[1]
                                     //keep in mind tempLocation[0] = the left operand
                                     //store the result and set tempLocation[2] to the location of the result (store)
                                     //load into the accumulator tempLocation[2] referencing the value of leftOperand + 1
                                     //create the necessary SML instructions
                                     //create the SML instruction MAX (+36...) using the variableLocation as the operand
                                     //store the result and set tempLocation[3] to reference the location of the result
                                     //load tempLocation[2] into the accumulator (leftOperand + 1)
                                     //subtract tempLocation[3] (the current maxima value)
                                     //set the opCode to branch zeros to the unresolved/resolved location
                                     //manipulation of the GOTO is handled by the next function
                                     //-----------------------begin execution---------------------------
                                     //create a temporary constant of type INT with a value equal to 1
                                     int temporaryConstant = 1;
                                     //check if the temp constant exists within the symbol table
                                      //if the constant does exist within the symbol table
                                     //determine its location and update the value of tempLocation[1]
                                     if(functions.confirmIfDuplicate(symbolTable, temporaryConstant, 'C')==true)
                                     {
                                       //first find the location of the symbol
                                        int location = functions.findTableIndexBasedOnSymbol(symbolTable, temporaryConstant,'C');
                                        //create a temporary tableEntry object based on the location of the symbol
                                        simple.register.TableEntry tableEntry = symbolTable.getTableEntry(location);
                                        tempLocation[1] = tableEntry.getLocation();
                                     }//end if, duplicate constant check
                                     //if the constant does not exist within the symbol table
                                     //update the constant to the symbol table
                                     //set tempLocation[1] to equal the value of the current data counter
                                     //increment the symbol table index
                                     //decrement the instructionCounter
                                     else
                                     {
                                         //append the temporaryConstant to the symbolTable
                                         symbolTable.addTableEntry(temporaryConstant, 'C', dataCounter.getDataCounter(), symbolTable.getSymbolIndex());
                                         tempLocation[1] = dataCounter.getDataCounter();
                                         //write temporaryConstant to the SML array using the data counter as the index
                                          smlArray.add(temporaryConstant, dataCounter.getDataCounter());                                           
                                        //increment the indices affected                                        
                                        symbolTable.setSymbolIndex(symbolTable.getSymbolIndex() + 1);
                                        dataCounter.setDataCounter(dataCounter.getDataCounter() - 1);    
                                     }//end if, non duplicate constant check
                                    //add the constant to the accumulator, where the operand = tempLocation[1]
                                     //set opCode to 30
                                     //set operand to tempLocation[1]
                                     //update the instrRegister
                                     //append the instruction to the sml array based on the instruction counter
                                     opCode.setOperationCode(30);
                                     operand.setOperand(tempLocation[1]);
                                     instrRegister.setInstructionRegister((opCode.getOperationCode() * 100) + operand.getOperand());//update the instrRegister
                                     smlArray.add(instrRegister.getInstructionRegister(), instrCounter.getInstructionCounter());
                                     instrCounter.setInstructionCounter(instrCounter.getInstructionCounter() + 1);
                                       //store the result and set tempLocation[2] to the location of the result (store)
                                     //set opCode to 21
                                    // use the data counter to acquire the location of the temp storage area
                                     //set tempLocation[2] to equal the current data counter
                                     //update the instrCounter
                                     //append the result to the sml array
                                     //increment the instrCounter
                                     //decrement  the data instrCounter
                                     opCode.setOperationCode(21);
                                     operand.setOperand(dataCounter.getDataCounter());
                                     tempLocation[2] = dataCounter.getDataCounter();
                                     instrRegister.setInstructionRegister((opCode.getOperationCode() * 100)+operand.getOperand());//update the instrRegister
                                     smlArray.add(instrRegister.getInstructionRegister(), instrCounter.getInstructionCounter());//add the instruction to the smlArray
                                      instrCounter.setInstructionCounter(instrCounter.getInstructionCounter() + 1);
                                      dataCounter.setDataCounter(dataCounter.getDataCounter() - 1);
                                     //load into the accumulator tempLocation[2] referencing the value of leftOperand + 1
                                      //set opCode to 20
                                      //set operand to tempLocation[2]
                                      //append the result to the sml array
                                     //increment the instrCounter
                                     opCode.setOperationCode(20);
                                     operand.setOperand(tempLocation[2]);
                                     instrRegister.setInstructionRegister((opCode.getOperationCode() * 100) + operand.getOperand());//update the instrRegister
                                     smlArray.add(instrRegister.getInstructionRegister(), instrCounter.getInstructionCounter());
                                     instrCounter.setInstructionCounter(instrCounter.getInstructionCounter() + 1);  
                                     //set opCode to 36 (MAXIMUM)
                                      //set operand to variableLocation
                                      //update the instrRegister (+36...)
                                      //append the instruction to the SML array
                                      //increment the instructionCounter
                                     opCode.setOperationCode(36);
                                     operand.setOperand(variableLocation);
                                     instrRegister.setInstructionRegister((opCode.getOperationCode() * 100) + operand.getOperand());
                                     smlArray.add(instrRegister.getInstructionRegister(), instrCounter.getInstructionCounter());
                                     instrCounter.setInstructionCounter(instrCounter.getInstructionCounter() + 1);
                                     //store the result and set tempLocation[3] to reference the location of the result                                      
                                     //set opCode to 21
                                     // use the data counter to acquire the location of the temp storage area
                                     //set tempLocation[3] to equal the current data counter
                                     //update the instrCounter
                                     //append the result to the sml array
                                     //increment the instrCounter
                                     //decrement  the data instrCounter
                                     opCode.setOperationCode(21);
                                     operand.setOperand(dataCounter.getDataCounter());
                                     tempLocation[3] = dataCounter.getDataCounter();
                                     instrRegister.setInstructionRegister((opCode.getOperationCode() * 100)+operand.getOperand());//update the instrRegister
                                     smlArray.add(instrRegister.getInstructionRegister(), instrCounter.getInstructionCounter());//add the instruction to the smlArray
                                      instrCounter.setInstructionCounter(instrCounter.getInstructionCounter() + 1);
                                      dataCounter.setDataCounter(dataCounter.getDataCounter() - 1);
                                       //load into the accumulator tempLocation[2] referencing the value of leftOperand + 1
                                      //set opCode to 20
                                      //set operand to tempLocation[2]
                                      //append the result to the sml array
                                     //increment the instrCounter
                                     opCode.setOperationCode(20);
                                     operand.setOperand(tempLocation[2]);
                                     instrRegister.setInstructionRegister((opCode.getOperationCode() * 100) + operand.getOperand());//update the instrRegister
                                     smlArray.add(instrRegister.getInstructionRegister(), instrCounter.getInstructionCounter());
                                     instrCounter.setInstructionCounter(instrCounter.getInstructionCounter() + 1);  
                                     //subtract tempLocation[3] (the current maxima value) from the accumulator
                                     //set opCode to 31
                                     //set operand to tempLocation[3]
                                       //update the instrCounter 
                                     //append to sml array
                                     //increment the instrCounter
                                     opCode.setOperationCode(31);
                                     operand.setOperand(tempLocation[3]);
                                     instrRegister.setInstructionRegister((opCode.getOperationCode() * 100) + operand.getOperand());
                                     smlArray.add(instrRegister.getInstructionRegister(), instrCounter.getInstructionCounter());
                                     instrCounter.setInstructionCounter(instrCounter.getInstructionCounter() + 1);
                                     //branch zeros to unresolved/resolved location
                                     opCode.setOperationCode(42);
                                     break;
                                 case ">=":
                                      //since the left operand is already loaded into the accumulator:
                                      //set opCode to 36 (MAXIMUM)
                                      //set operand to variableLocation
                                      //update the instrRegister (+36...)
                                      //append the instruction to the SML array
                                      //increment the instructionCounter
                                     opCode.setOperationCode(36);
                                     operand.setOperand(variableLocation);
                                     instrRegister.setInstructionRegister((opCode.getOperationCode() * 100) + operand.getOperand());
                                     smlArray.add(instrRegister.getInstructionRegister(), instrCounter.getInstructionCounter());
                                     instrCounter.setInstructionCounter(instrCounter.getInstructionCounter() + 1);
                                     //store the result in a temporary memory location
                                     //update the location to tempLocation[0] before incrementing the data counter
                                     //set opCode to 21 (Store)
                                     //set operand to the current data counter
                                     //the data counter helps identify the temporary storage area
                                     //update the location in the operand
                                     //set tempLocation[1] to the current value of the data counter
                                     //increment the instructionCounter
                                     //decrement the dataCounter
                                     opCode.setOperationCode(21);//set the opCode to store
                                     operand.setOperand(dataCounter.getDataCounter());
                                     tempLocation[1] = dataCounter.getDataCounter();
                                     instrRegister.setInstructionRegister((opCode.getOperationCode() * 100)+operand.getOperand());//update the instrRegister
                                     smlArray.add(instrRegister.getInstructionRegister(), instrCounter.getInstructionCounter());//add the instruction to the smlArray 
                                     instrCounter.setInstructionCounter(instrCounter.getInstructionCounter() + 1);//increment the SML array index
                                     dataCounter.setDataCounter(dataCounter.getDataCounter() - 1);
                                     //load tempLocation[0]
                                     //update the instrCounter 
                                     //append to sml array
                                     //increment the instrCounter
                                      opCode.setOperationCode(20);
                                      operand.setOperand(tempLocation[0]);
                                      instrRegister.setInstructionRegister((opCode.getOperationCode() * 100) + operand.getOperand());
                                      smlArray.add(instrRegister.getInstructionRegister(), instrCounter.getInstructionCounter());
                                      instrCounter.setInstructionCounter(instrCounter.getInstructionCounter() + 1);
                                     //subtract tempLocation[1]
                                       //update the instrCounter 
                                     //append to sml array
                                     //increment the instrCounter
                                     opCode.setOperationCode(31);
                                     operand.setOperand(tempLocation[1]);
                                     instrRegister.setInstructionRegister((opCode.getOperationCode() * 100) + operand.getOperand());
                                     smlArray.add(instrRegister.getInstructionRegister(), instrCounter.getInstructionCounter());
                                     instrCounter.setInstructionCounter(instrCounter.getInstructionCounter() + 1);
                                     //create opCode for branching zero 
                                     opCode.setOperationCode(42);
                                     break;
                                 case "!=":
                                     //A quick overview of the not equal to relational operator evaluation algorithm
                                     //It seems plausible that if we have variables y and x 
                                     //to evaluate y != x
                                     //if both variables have different values
                                     //then ( y - x ) * ( x - y ) yields a negative value
                                     //if both variables have equal values
                                      //then ( y - x ) * ( x - y ) yields a zero value
                                     //thus the SML simulator should branch negatives to indicate non-equal values
                                     //if the accumulator contains a zero value, then the branch to negatives will stand useless, indicating the
                                     //values were equal                                    
                                     //--------------------preliminaries------------------------------
                                     // if the location of y = 99, x = 98
                                     // - then, since the left operand (y) has already been loaded, +2099
                                     // 1. subtract the right operand (x) from the accumulator
                                     // - the location of x is based on the variableLocation : INT
                                     // - this will yield an instruction similar to +3198
                                     //2. store the result and let tempLocation[1] reference the location of the result
                                     // - +2197
                                     // - tempLocation[1] = 97
                                     //3. Load the right operand (x) into the accumulator
                                     // - the location of x is based on the variableLocation : INT
                                     //- +2098
                                     //4. subtract the left operand (y) from the accumulator
                                     // - the location of y is referenced by tempLocation[0]
                                     // - +3199
                                     //5. store the result and let tempLocation[2] reference the location of the result
                                     // - +2196
                                     // - tempLocation[2] = 96
                                     //6. load tempLocation[1] into the accumulator
                                     // - +2097
                                     //7. Multiply tempLocation[2] into the accumulator
                                     //- +3396
                                     //8. only branch negatives to an (un)resolved location
                                     // - +41...
                                     //----------------begin execution----------------------------
                                       // 1. subtract the right operand (x) from the accumulator
                                      //set opCode = 31
                                     //operand = variableLocation
                                     //update the instrRegister
                                     //append the instr to the SML array
                                     //increment the instrCounter
                                     opCode.setOperationCode(31);
                                     operand.setOperand(variableLocation);
                                     instrRegister.setInstructionRegister((opCode.getOperationCode() * 100) + operand.getOperand());//update the instrRegister
                                     smlArray.add(instrRegister.getInstructionRegister(), instrCounter.getInstructionCounter());//add the instruction to the smlArray 
                                     instrCounter.setInstructionCounter(instrCounter.getInstructionCounter() + 1);//increment the SML array index
                                     //2. store the result and let tempLocation[1] reference the location of the result
                                     //set opCode = 21
                                     //use the data counter to acquire the location of the temp storage area
                                     //let tempLocation[1] = dataCounter (current)
                                     //update the instrRegister
                                     //append to the sml array based on the instr counter as the index
                                     //increment the instrCounter
                                     //decrement the data counter
                                        opCode.setOperationCode(21);
                                        operand.setOperand(dataCounter.getDataCounter());
                                        tempLocation[1] = dataCounter.getDataCounter();
                                        instrRegister.setInstructionRegister((opCode.getOperationCode() * 100) + operand.getOperand());
                                        smlArray.add(instrRegister.getInstructionRegister(), instrCounter.getInstructionCounter());
                                       instrCounter.setInstructionCounter(instrCounter.getInstructionCounter() + 1);//increment the SML array index
                                       dataCounter.setDataCounter(dataCounter.getDataCounter() - 1);  
        //3. Load the right operand (x) into the accumulator
       // - the location of x is based on the variableLocation : int
        opCode.setOperationCode(20);//set the opCode to load
        operand.setOperand(variableLocation);//set the operand to reference the location of x
        instrRegister.setInstructionRegister((opCode.getOperationCode() * 100)+operand.getOperand());//update the instrRegister
        smlArray.add(instrRegister.getInstructionRegister(), instrCounter.getInstructionCounter());//add the instruction to the smlArray 
        instrCounter.setInstructionCounter(instrCounter.getInstructionCounter() + 1);//increment the SML array index
                //4. subtract the left operand (y) from the accumulator
                // - the location of y is referenced by tempLocation[0]
                opCode.setOperationCode(31);
               operand.setOperand(tempLocation[0]);
               instrRegister.setInstructionRegister((opCode.getOperationCode() * 100) + operand.getOperand());//update the instrRegister
               smlArray.add(instrRegister.getInstructionRegister(), instrCounter.getInstructionCounter());//add the instruction to the smlArray 
               instrCounter.setInstructionCounter(instrCounter.getInstructionCounter() + 1);//increment the SML array index
                            //5. store the result and let tempLocation[2] reference the location of the result
                            opCode.setOperationCode(21);
                            operand.setOperand(dataCounter.getDataCounter());
                            tempLocation[2] = dataCounter.getDataCounter();
                            instrRegister.setInstructionRegister((opCode.getOperationCode() * 100) + operand.getOperand());
                            smlArray.add(instrRegister.getInstructionRegister(), instrCounter.getInstructionCounter());
                            instrCounter.setInstructionCounter(instrCounter.getInstructionCounter() + 1);//increment the SML array index
                            dataCounter.setDataCounter(dataCounter.getDataCounter() - 1); 
                                         //6. load tempLocation[1] into the accumulator
                                         opCode.setOperationCode(20);//set the opCode to load
                                        operand.setOperand(tempLocation[1]);//set the operand to reference the location of x
                                        instrRegister.setInstructionRegister((opCode.getOperationCode() * 100)+operand.getOperand());//update the instrRegister
                                        smlArray.add(instrRegister.getInstructionRegister(), instrCounter.getInstructionCounter());//add the instruction to the smlArray 
                                        instrCounter.setInstructionCounter(instrCounter.getInstructionCounter() + 1);//increment the SML array index
        // Multiply tempLocation[2] into the accumulator
          opCode.setOperationCode(33);
        operand.setOperand(tempLocation[2]);
        instrRegister.setInstructionRegister((opCode.getOperationCode() * 100) + operand.getOperand());//update the instrRegister
        smlArray.add(instrRegister.getInstructionRegister(), instrCounter.getInstructionCounter());//add the instruction to the smlArray 
        instrCounter.setInstructionCounter(instrCounter.getInstructionCounter() + 1);//increment the SML array index                               
                                     //------------finish execution-------------------------------
                                     //create opCode for branching to negatives
                                     opCode.setOperationCode(41);
                                     break;                               
                             }//end switch, relational operators 
                            }//end if, positive variableLocation check                          
                             /**
                              * first check if the goto reference indicates a forward or a backward reference
                              * first determine the location of the line number in the symbol table (token at index 6)
                              * a positive value indicates a backward reference
                              * a negative value indicates a forward reference
                              * the opCode has already been defined by the relational operator
                              */
                               int location = functions.findTableIndexBasedOnSymbol(symbolTable, Integer.parseInt(tokens[6]),'L');//locate the line number that has been referenced
                               /**
                                * if this is a backward reference:
                                * create a temporary tableEntry object based on the location of the symbol
                                */
                                if(location >= 0)
                                {                                      
                                    simple.register.TableEntry tableEntry = symbolTable.getTableEntry(location);
                                    operand.setOperand(tableEntry.getLocation());
                                   // System.out.println("current operand: "+operand.getOperand());
                                   //   System.out.println("current opCode: "+opCode.getOperationCode());
                                    instrRegister.setInstructionRegister((opCode.getOperationCode()*100)+operand.getOperand());
                                    smlArray.add(instrRegister.getInstructionRegister(), instrCounter.getInstructionCounter());
                                    instrCounter.setInstructionCounter(instrCounter.getInstructionCounter() + 1);
                                }//end if, backward reference check
                                /**
                                 * if this is a forward reference:
                                 * create the necessary SML instructions
                                 */
                                 else if(location == -1)
                                 {                                 
                                    //we have no idea of the location of the lineno                       
                                    //set the operand to 00
                                    //update the instrRegister
                                    //append the incomplete instr to the SML array
                                    //store the lineno in the flags array based on the index of the unresolved instruction in the SML array
                                    //increment the instrCounter                        
                                    operand.setOperand(0);
                                    instrRegister.setInstructionRegister((opCode.getOperationCode()*100)+operand.getOperand());
                                    smlArray.add(instrRegister.getInstructionRegister(), instrCounter.getInstructionCounter());
                                    flags.addFlag(Integer.parseInt(tokens[6]), instrCounter.getInstructionCounter());
                                    instrCounter.setInstructionCounter(instrCounter.getInstructionCounter() + 1);
                                 }//end else if ,forward reference check
                           }//end if, relational operator check
                           else
                           {
                              throw new simple.exceptions.InvalidAssignmentOperatorException("An invalid relational operator has been used");
                           }//end else
                       }//end if validate varName check
                       else//if the varName is invalid throw an exception
                       {
                           throw new simple.exceptions.InvalidVariableNameException("An invalid variable name declaration has been encountered.");
                       }//end else
                break;
                case GOTO:
                   //perform unconditional branching
                    //there are two cases:
                    //either the line number refers to a forward or a backward reference
                    //first determine the location of the line number in the symbol table                  
                    int location = functions.findTableIndexBasedOnSymbol(symbolTable, Integer.parseInt(tokens[2]),'L');//locate the line number that has been referenced
                   /**
                    * if this is a backward reference
                    * acquire a tableEntry object based on the location of the symbol
                    * set the opCode to the unconditional branch
                    * set the operand to reference the location of the line number
                    * update the instructionRegister
                    * append the result to the smlArray
                    * increment the instructionCounter
                    */
                    if(location >= 0)
                    {
                        //create a temporary tableEntry object based on the location of the symbol
                        simple.register.TableEntry tableEntry = symbolTable.getTableEntry(location);
                        opCode.setOperationCode(40);
                        operand.setOperand(tableEntry.getLocation());
                        instrRegister.setInstructionRegister((opCode.getOperationCode()*100)+operand.getOperand());
                        smlArray.add(instrRegister.getInstructionRegister(), instrCounter.getInstructionCounter());
                        instrCounter.setInstructionCounter(instrCounter.getInstructionCounter() + 1);
                    }
                    /**
                     * if this is a forward reference
                     * The SML instruction must be flagged to indicate that the second pass of the compiler should complete the instruction
                     * store the lineno in the flags object in the element with the same index as the incomplete instruction
                     * temporarily set the operand of the incomplete instruction to 00 temporarily
                     * the unresolved references will be handled in the second pass of the compiler
                     */
                    else if(location == -1)
                    {
                        //we have no idea of the location of the lineno
                        //set the opCode to 40 
                        //set the operand to 00
                        //update the instrRegister
                        //append the incomplete instr to the SML array
                        //store the lineno in the flags array based on the index of the unresolved instruction in the SML array
                        //increment the instrCounter
                        opCode.setOperationCode(40);
                        operand.setOperand(0);
                        instrRegister.setInstructionRegister((opCode.getOperationCode()*100)+operand.getOperand());
                        smlArray.add(instrRegister.getInstructionRegister(), instrCounter.getInstructionCounter());
                        flags.addFlag(Integer.parseInt(tokens[2]), instrCounter.getInstructionCounter());
                        instrCounter.setInstructionCounter(instrCounter.getInstructionCounter() + 1);
                    }//end else if, forward ref check
                    break;
                default://a syntax error exception is thrown by default if an invalid comman statement is detected
                    throw new SimpleSyntaxErrorException("An error with the simple language syntax has been detected");                    
            }//end switch
             /**
              * if the dataCounter and the instructionCounter clash, generate an exception
              * This error may cause the compiler to overwrite previously declared instructions
              */
             if(instrCounter.getInstructionCounter() == dataCounter.getDataCounter())
             {
                 throw new simple.exceptions.OutOfMemoryException("The compiler has run out of memory");
             }//end if                      
      }//end try
      catch(SimpleSyntaxErrorException e)
      {
            outputStream.writeToGUI(e);
      }
      catch(OutOfMemoryException e)
      {
          outputStream.writeToGUI(e, "The Compiler has run out of memory");
      }  //end catch   
      catch(EmptyLineNumberException e)
      {
          outputStream.writeToGUI(e, "An empty or an invalid line number has been encountered"); 
      }
      catch(InvalidVariableNameException e)
      {
            outputStream.writeToGUI(e); 
      }
      catch(InvalidAssignmentOperatorException e)
      {
            outputStream.writeToGUI(e); 
      }
      catch(NumberFormatException e)
      {
         outputStream.writeToGUI(e,"An invalid constant has been encountered, please check that you have input the correct constant");  
      }
      catch(InputMismatchException e)
      {
          outputStream.writeToGUI(e,"An invalid variable name or command statement has been encountered, please check that you have input the correct variable name or command statement"); 
      }
      catch(InvalidVariableSeparatorException e)
      {
          outputStream.writeToGUI(e);
      }
   }//end the first pass of the compilation
    /**
     * performs the second pass of the simple file compilation
     * searches the flags array for any unresolved references values (>=0)
     * locates the object in the symbolTable containing the symbol stored in the flags array
     * ensures the symbol is of type 'L'
     * inserts the memory location from field location into the instruction with the unresolved ref (with operand 00)
     * repeats the steps until the end of the flags array is reached
     */
    public void secondPass()
    {
       try
       {
          //loop through the flags array
        for(int i=0; i<flags.getFlagSize(); i++)
        {
           if(flags.getElement(i) >= 0)//search for unresolved references
           {
               //check if the lineno exists in the symbolTable
               //create a temporary tableEntry object based on the location of the symbol  
               //set the operand to the location of the symbol
               //update the instrRegister with the location of the instr (symbol) in the SML array
               //append the instrRegister with the operand
               //reset the smlArray element (unresolved ref) with the new resolved reference
               int location = functions.findTableIndexBasedOnSymbol(symbolTable,flags.getElement(i) , 'L');
               //System.out.println("flag: "+flags.getElement(i));
               if(location >= 0)//the lineno exists within the symbolTable
               {                  
                   simple.register.TableEntry tableEntry = symbolTable.getTableEntry(location);
                 //  System.out.println("location of flag: "+i);
                   operand.setOperand(tableEntry.getLocation());                 
                   instrRegister.setInstructionRegister(smlArray.getElement(i));
                  // System.out.println("unresolved sml: "+smlArray.getElement(i));
                   instrRegister.setInstructionRegister(instrRegister.getInstructionRegister() + operand.getOperand());
                 //  System.out.println("register "+instrRegister.getInstructionRegister());
                   smlArray.add(instrRegister.getInstructionRegister(), i);
               }//end if
               else if(location == -1)//the lineno does not exist within the symbolTable
               {
                  throw new simple.exceptions.EmptyLineNumberException("An illegal jump to an undeclared line number has been found"
				  +"\nEnsure the goto statement references a line number either at the begining or at the end of the file."); 
               }//end else if
           }//end if, unresolved ref check
        }//end for loop  
       }
       catch(simple.exceptions.EmptyLineNumberException e)
       {
            outputStream.writeToGUI(e);//print the exception           
       }
    }//end second pass of the compiler
    /**
     * This method finalizes all the actions of the simple compiler,
     * for instance, the smlArray is updated with a sentinel value,
     * and the smlArray is finally written to the currently specified *.sml file
     * @see simple.records.WriteDataToSML#openFile() 
     * @see simple.records.WriteDataToSML#addRecords() 
     * @see simple.records.WriteDataToSML#closeFile() 
     */
    public void finalization()
    {
         /**
              * At this point, the compiler has finished writing all the necessary instructions
              * The SML simulator loads instructions into memory if an only if they are &lt; -9999 &amp; &gt; +9999
              * the value -99999 indicates the sentinel,
              * thus, the SML simulator will continue loading instructions and data into memory until the sentinel is reached
              * to achieve this effect the data word (sentinel) is placed at the end of the SML array
              * object dataCounter provides an abstraction which allows the compiler to know exactly the final location where the sentinel 
              * will be placed
              * hence, the dataCounter constant MAXIMUM_SIZE will be appended directly into the last location of the SMl array
              * one effect of this approach leaves the final index of the SML array in the simulator with the last index empty
              * for example, if MAXIMUM_SIZE = 99, then, the final SML array will have data contained up until index 98
              */
             //begin writing the constant
             //the value of the constant location equals MAXIMUM_SIZE
             // the sentinelValue = -99999
             //no searches within the smlArray are required
             //update the instrCounter with the sentinel
             //directly append the instr to the sml array
             //incrementing of the instruction counter or decrementing of the data counter is not necessary
             final int SENTINEL = -99999;//the default simpletron sentinel value
             instrRegister.setInstructionRegister(SENTINEL);
             smlArray.add(instrRegister.getInstructionRegister(), dataCounter.MAXIMUM_SIZE); 
             //write the current smlRecord object to *.sml file
            simple.records.WriteDataToSML writeData = new simple.records.WriteDataToSML(smlArray, this.smlFile);//create a call to the object for writing to a file
            writeData.openFile();//open the SML file
            writeData.addRecords();//add the smlArray data to the smlFile
            writeData.closeFile();//close the smlFile
    }
    /**
     * opens the simple file
     */
    public void openFile()
    {
        try
        {
            fileReader = new FileReader(this.currFile);//wraps the current simple file into a FileReaderObject
            bufferedReader = new BufferedReader(fileReader);//appends the contents of the fileReader wrapper into a bufferedReader
        }
        catch(FileNotFoundException e)
        {
            outputStream.writeToGUI(e, "An error occurred while attempting to locate the specified file.");
        }
        catch(NullPointerException e)
        {
             outputStream.writeToGUI(e,"The file received is either invalid or null.");
        }//end catch statement
    }
    /**
     * closes the simple file
     */
    public void closeFile()
    {
        try
        {
            if(bufferedReader != null)
            bufferedReader.close();
        }
        catch(IOException e)
        {
           outputStream.writeToGUI(e, "An error occurred while attempting to close the specified file.");  
        }
    }
}//end class 
/**
 * This object tests the compiler object before it is finally deployed
 * It is useful for debugging purposes
 * @see simple.compiler.Compiler
 * @author gerry dreamer
 */
class CompilerTest
{
    public static void main(String[] args) {
        File file = new File("E:\\project de java\\simple.sim");
        File smlFile = new File("C:\\Users\\gerry dreamer\\Desktop\\compileOutput.sml");
        Compiler c = new Compiler(file,smlFile);
    }
}