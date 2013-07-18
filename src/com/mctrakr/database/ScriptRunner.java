/* 
 * ScriptRunner.java
 * 
 * Statistics
 * 
 * Copyright 2009-2013 The MyBatis Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * Changelog:
 * 
 * Cut down on unused code and generally optimized for desired tasks.
 * Removed the ability to change the delimiter - we do not need that.
 * - bitWolfy
 * 
 * Added the ability to change the delimiter so you can run scripts that 
 * contain stored procedures.
 * - ChaseHQ
 * 
 * Original release
 * - The MyBatis Team
 */

package com.mctrakr.database;

import java.io.BufferedReader;
import java.io.Reader;
import java.sql.Connection;
import java.sql.Statement;
import java.util.logging.Level;

import org.apache.commons.lang.StringUtils;

import com.mctrakr.database.exceptions.RuntimeSQLException;
import com.mctrakr.settings.LocalConfiguration;
import com.mctrakr.util.ExceptionHandler;
import com.mctrakr.util.Message;

/**
 * A library designed to execute extremely long database queries from file
 * @author MyBatis Team
 * @author ChaseHQ
 * @author bitWolfy
 *
 */
public class ScriptRunner {
    
    private static final String LINE_SEPARATOR = System.getProperty("line.separator", "\n");
    private static final String DELIMITER = ";";
    
    private String dbName = LocalConfiguration.DBName.toString();
    private String dbPrefix = LocalConfiguration.DBPrefix.toString();
    private boolean debug = LocalConfiguration.Debug.toBoolean();
    
    private Connection connection;
    
    public ScriptRunner(Connection connection) {
        this.connection = connection;
    }
    
    /**
     * Executes a database script
     * @param reader Reader
     */
    public void runScript(Reader reader) {
        Message.log(Level.FINER, "| [X] Executing database script          |");
        
        StringBuilder command = new StringBuilder();
        BufferedReader lineReader = new BufferedReader(reader);
        int i = 0;
        String line = "";

        try {
            do {
                try {
                    line = lineReader.readLine();
                    command = this.handleLine(command, line);
                } catch (Throwable t) {
                    Message.log(Level.FINER,
                            "|  |-- An error on line " + Message.fillString(i + "! Skipping.", 17) + "|",
                            "|      Stack trace is available in the   |",
                            "|      server log file.                  |"
                            );
                    ExceptionHandler.handle(t, true);
                }
                i++;
                if(i % 50 == 0 && debug) Message.log(Level.FINEST, "|  |-- Executing line " + Message.fillString(i + "", 19) + "|");
            } while (line != null);
            Message.log(Level.FINER, "|  |-- Executed " + Message.fillString(i + " lines total", 25) + "|");
            
            try { this.connection.commit(); }
            catch (Throwable t) { throw new RuntimeSQLException("Could not commit transaction. Cause: " + t, t); }
            
            if (command != null && command.toString().trim().length() > 0) {
                throw new RuntimeSQLException("Line missing end-of-line terminator (" + DELIMITER + ") => " + command);
            }
            
        } finally {
            try { this.connection.rollback(); }
            catch (Throwable t) { }
        }
    }
    
    /**
     * Parses the SQL script lines and executes the statement once it is complete
     * @param command Combined command string that is passed from one iteration to another
     * @param line A new line to add to the statement
     * @return Combined command to be passed to the next iteration
     * @throws RuntimeSQLException Thrown if an error occurred while executing a statement
     */
    private StringBuilder handleLine(StringBuilder command, String line) throws RuntimeSQLException {
        String trimmedLine = line.trim();
        if (trimmedLine.startsWith("//") || trimmedLine.startsWith("--")) {
            // Skip the comments
        } else if (trimmedLine.endsWith(DELIMITER)) {
            command.append(line.substring(0, line.lastIndexOf(DELIMITER)));
            command.append(ScriptRunner.LINE_SEPARATOR);
            
            try {
                Statement statement = connection.createStatement();
                String sql = command.toString();
                sql = StringUtils.replace(sql, "\r\n", "\n");
                sql = StringUtils.replace(sql, "$dbname", dbName);
                sql = StringUtils.replace(sql, "$prefix_", dbPrefix);
                statement.execute(sql);
                statement.close();
            } catch (Throwable t) {
                throw new RuntimeSQLException("An error occurred while executing a script line", t);
            } finally { command.setLength(0); }
        } else if (trimmedLine.length() > 0) {
            command.append(line);
            command.append(ScriptRunner.LINE_SEPARATOR);
        }
        return command;
    }
    
}
