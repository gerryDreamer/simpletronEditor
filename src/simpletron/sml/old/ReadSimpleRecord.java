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
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

/**
 *
 * @author gerry dreamer
 */
public class ReadSimpleRecord {
               
}
class ReadSimpleRecordTest
{
 ObjectInputStream input;
 public void openFile()
 {
                try
                {
                               input = new ObjectInputStream(new FileInputStream("SML.ser"));
                }
                catch(IOException e)
                {
                               System.err.println("error opening file.");              
                }
 }
 
 public void readRecords()
 {
       SimpletronRecord record;
                System.out.printf("%s\n","Word");
                try
                {
                   while(true)
                   {
                         record = (SimpletronRecord) input.readObject();
                         //display the SML instructions
                                  System.out.printf("%s%d\n",record.getWord() < 0 ? "-":"+",record.getWord());
                   }//end while
                }
                catch(EOFException e)
                {
                return;               
                }
                catch(ClassNotFoundException e)
                {
                               System.err.println("unable to create object");       
                }
                catch(IOException e)
                {
                               System.err.println("error during read from file.");
                }
 }
 
 public void closeFile()
 {
    try
    {
                   if(input != null)
                                  input.close();
    }
    catch(IOException e)
    {
                   System.err.println("error closing file");
                   System.exit(1);
    }
 }
}
class ReadSimpleRecordTestTest
{
public static void main(String[] args) {
               ReadSimpleRecordTest r = new ReadSimpleRecordTest();
               r.openFile();
               r.readRecords();
               r.closeFile();
}
}