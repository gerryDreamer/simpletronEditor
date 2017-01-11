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
package simple.register;

/**
 * this class object represents the standard instruction commands currently supported by the simple language
 * @author gerry dreamer
 */
public class CommandStatements {
/**
 * this command allows for processing of comments within the program
 */    
 public static final String REM = "rem";
 /**
  * this command allows for input of integers into the program memory
  * displays a question mark to prompt the user to enter an integer
  */
 public static final String INPUT = "input";
 /**
  * this command allows for processing of arithmetic operations
  */
 public static final String LET = "let";
 /**
  * this command enables the program to display output, either 
  * in the console or a specified file.
  */
 public static final String PRINT = "print";
 /**
  * this command allows for processing of transfer control within the program
  */
 public static final String GOTO = "goto";
 /**
  * this command allows for processing of transfer control, it is
  * used in conjunction with the goto statement
  * if well implemented, it will allow for iteration within the program
  */
 public static final String IF = "if";
 /**
  * this command ultimately ends the program execution
  * this command can only be executed once within the program.
  */
 public static final String END = "end";
}
