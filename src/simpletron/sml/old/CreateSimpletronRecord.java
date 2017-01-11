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

package simpletron.sml.old;

import simpletron.records.SimpletronRecord;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * this object creates a byte based representation of the current object within the memory
 * @author gerry dreamer
 */
public class CreateSimpletronRecord {
/**
 * outputs the instruction information to a file
 */
        private ObjectOutputStream output;  
        /**
         * creates reference to the file to be created 
         * within the local disk memory or external devices
         */
        public void openFile()
        {
           try
           {
               output = new ObjectOutputStream(new FileOutputStream("SML.ser"));           
           }//end try
           catch(IOException e)
           {
              System.err.println("error in opening file.");            
           }//end catch
        }//end openFile
         /**
          * writes objects to the file specified as output
          */
        public void addRecords()
        {
            SimpletronRecord record;//object to be written to the file
            int word = +1000;//the default instruction          
            int sentinel = 0;
            Scanner input = new Scanner(System.in);
                       System.out.printf("%s\n%s","Please enter the current four letter word (instruction)\n"
                               + "and the value associated with the word, enter -1 to terminate.","? ");
            while(sentinel != -1)
            {
              try
              {
               word = input.nextInt();              
               sentinel = input.nextInt();
               //create a new record
               record = new SimpletronRecord(word);
               output.writeObject(record);
              }//end try
              catch(IOException e)
              {
                             System.err.println("error in writing to file");
                             return;
              }//end catch
              catch(NoSuchElementException e)
              {
                             System.err.println("Invalid input. Please try again");
                             input.nextLine();
              }//end catch
               System.out.printf("%s\n%s","Please enter the current four letter word, enter -1 to terminate.","? ");
            }//end while loop
        }//end method addRecords
        /**
         * closes the current file created for the SML 
         */
       public void closeFile()
       {
            try
            {
                 if(output != null)
                                output.close();
            }
            catch(IOException e)
            {
                           System.err.println("Error closing file");
                           System.exit(1);
            }
       }//end method closeFile
}//end class
class CreateSimpletronRecordTest
{
public static void main(String[] args) {
               CreateSimpletronRecord s = new CreateSimpletronRecord();
               s.openFile();
               s.addRecords();
               s.closeFile();
}
}