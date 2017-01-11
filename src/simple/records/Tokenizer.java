/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package simple.records;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StreamTokenizer;
import java.util.ArrayList;
import java.util.StringTokenizer;
import simple.exceptions.OutOfMemoryException;
import simple.register.DataCounter;
import simple.register.InstructionCounter;
import simple.register.SymbolTable;

/**
 *
 * @author gerry dreamer
 */
public class Tokenizer {
  private  StreamTokenizer s;
  private  FileReader reader;
     /**
 * create an instruction array list to hold the current instruction
 */
private ArrayList<String> instructionList = new ArrayList<String>(); 
/**
 * create a variable name array list to hold the current variable name
 */
private ArrayList<Character> variableNameList = new ArrayList<Character>();
/**
 *  create a variable array list to hold the current variable
 */
private ArrayList<Integer> variableList = new ArrayList<Integer>();
/**
 * create an array list to hold the current line number
 */
private ArrayList<Integer> lineNumberList = new ArrayList<Integer>();

private SymbolTable symbolTable;
InstructionCounter instrCounter = new InstructionCounter();
DataCounter  dataCounter = new DataCounter();
   // BufferedReader input;
    public Tokenizer(SymbolTable symbolTable)
    {
        this.symbolTable = symbolTable;
        openFile();
        addRecords();
     printList(this.instructionList);
     printList(this.variableNameList);
     printList(this.variableList);
     printList(this.lineNumberList);
    }
    
    public void addRecords()
    {
        s = new StreamTokenizer(reader);      
       try
       {
         s.lowerCaseMode(true);     
         s.eolIsSignificant(true);
         s.resetSyntax();
         s.wordChars(33, 255);      
         s.eolIsSignificant(true);
         s.pushBack();//do not modify the tokens as numbers of strings
             int index = 0;
            instrCounter.setInstructionCounter(100);
            dataCounter.setDataCounter(0);
        do
        {
             index = s.nextToken();
           switch(s.ttype)
           {
               case StreamTokenizer.TT_NUMBER:
                  
               break;
               case StreamTokenizer.TT_WORD:
                    System.out.println(""+s.sval);
                 switch(s.sval)
                 {
                     case "rem":
                       //  symbolTable.addTableEntry(index, type, index, index);
                        
                         break;
                 }//end switch
//                 if(s.sval != "rem")
//                 {
//                     instrCounter.setInstructionCounter(instrCounter.getInstructionCounter() + 1);
//                 }
                   break;
               case StreamTokenizer.TT_EOL:
                   
                   break;
               case StreamTokenizer.TT_EOF:
                   
                   break;                   
           }//end switch
        }      
         while(index != StreamTokenizer.TT_EOF);
        }
       catch(IOException e)
       {
           
       }       
    }
    public void openFile()
    {
         try
       {
           File name = new File("E:\\project de java\\simple.txt");
                      reader = new FileReader(name);
                    //  input = new BufferedReader(reader);
       }
       catch(IOException e)
       {
                      System.err.println("error in opening file");  
                      System.exit(1);
       }
    }
    public void printList(ArrayList list)
    {
        for(int i=0; i<list.size(); i++)
        {
            System.out.print(" "+list.get(i));
        }
    }
}
class TokenizerTest
{
    public static void main(String[] args) {
        SymbolTable s = new SymbolTable();
        Tokenizer t = new Tokenizer(s);
                System.out.printf("%-10s%-10s%10s\n","Symbol","Type","Location");       
        for(int i=0; i<s.getSymbolTableSize(); i++)
     {
         if(s.getTableEntry(i).getType() != ' ')
          System.out.printf("%-10d%-10s%10d\n",s.getTableEntry(i).getSymbol(),s.getTableEntry(i).getType(),s.getTableEntry(i).getLocation());         
     }
    }
}