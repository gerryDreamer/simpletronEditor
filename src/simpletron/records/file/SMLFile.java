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

package simpletron.records.file;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import simpletron.hardware.OutputStream;

/**
 * this class provides utilities for accessing a file object 
 * @author gerry dreamer
 */
public class SMLFile {
  /**
   * the default file object
   */     
private File file;
/**
 * the default JFileChooser object
 */
private JFileChooser chooser;
/**
 * the default JFileChooser close/open dialog
 */
private int result = 0;
/**
 * the default open dialog constant
 */
private final int OPEN = 0;
/**
 * the default close dialog constant
 */
private final int SAVE = 1;
/**
 * the customized open dialog constant
 */
private final int LOAD = 2;
/**
 * the current dialog title of this object
 */
private String dialogTitle = "";
/**
 * the current filter for supported SML files
 */
private FileNameExtensionFilter filter;
/**
 * this method sets the current file based on the arguments received from
 * the default file chooser dialog
 * @param dialog the current file chooser dialog constant
 */
private void setFile(int dialog)
{
    OutputStream outputStream = new simpletron.hardware.OutputStream();
         try
         {
                /**
                * initialize the current file chooser dialog
                */
     chooser = new JFileChooser();  
     chooser.setDragEnabled(true);//set this dialog to be drag enabled 
     filter = new FileNameExtensionFilter("SML,TXT,sim, and simple files","sml","txt","sim","simple");
     chooser.addChoosableFileFilter(filter);
     /**
      * display both files and directories
      */
     chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
     /**
      * set the default value of the file chooser dialog
      */
     switch(dialog)
     {
     case OPEN:
          result = chooser.showOpenDialog(chooser);
          chooser.setDialogTitle(" "+dialogTitle);
      break;
     case SAVE:
        result = chooser.showSaveDialog(chooser);
          chooser.setDialogTitle(""+dialogTitle);
      break;              
     case LOAD:
             result = chooser.showDialog(chooser, "Load");
               chooser.setDialogTitle("Load SML file");
      break;           
     default:
          result = chooser.showOpenDialog(chooser);            
      break;              
     }//end switch
     /**
      * end the dialog if the user clicks the cancel button
      */
     if(result == JFileChooser.CANCEL_OPTION)
     {
                  //  System.exit(1);
         //do not exit the system
          new simpletron.hardware.OutputStream().writeToGUI("Exiting file chooser dialog.");
     }
     /**
      * initialize the current file to the selected file
      */
     file = chooser.getSelectedFile();
     /**
      * handle errors for empty or null files
      */
     if((file == null) || (file.getName().equals("")))
     {
      outputStream.writeToGUI(null, "Invalid file name");
      throw new NullPointerException();
     }//end if   
         } //end try  
         catch(NullPointerException e)
         {
             outputStream.writeToGUI(null, "You need to specify a file name");
         }
}//end set file
/**
 * the current open file method
 * @return the current file chosen by the JFileChooser
 */
 public File getOpenFile()
 {
    setFile(0);//set the dialog integer to the OPEN constant
   return file;
 }//end method
 /**
  * the current save file method
  * @return the current file chosen by the JFileChooser
  */
 public File getSaveFile()
 {
   setFile(1);//set the dialog integer to the OPEN constant
   return file;              
 }//end file
 /**
  * this is a customized open file method
  * @return the current file chosen by the JFileChooser
  */
 public File getLoadFile()
 {
    setFile(2);//set the dialog integer to the OPEN constant
   return file;                
 }//end method
 /**
 * the current open file method
 * @param dialogTitle the current title for this dialog
 * @return the current file chosen by the JFileChooser
 */
 public File getOpenFile(String dialogTitle)
 {
     this.dialogTitle = dialogTitle;
    setFile(0);//set the dialog integer to the OPEN constant
   return file;
 }//end method
 /**
  * the current save file method
  * @param dialogTitle the current title for this dialog
  * @return the current file chosen by the JFileChooser
  */
 public File getSaveFile(String dialogTitle)
 {
   this.dialogTitle = dialogTitle;
   setFile(1);//set the dialog integer to the OPEN constant
   return file;       
 }//end file
}
