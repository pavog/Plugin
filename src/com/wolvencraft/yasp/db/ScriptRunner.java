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
 * - bitWolfy
 * 
 * Added the ability to change the delimiter so you can run scripts that 
 * contain stored procedures.
 * - ChaseHQ
 * 
 * Original release
 * - The MyBatis Team
 */

package com.wolvencraft.yasp.db;

import java.io.BufferedReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;

import org.apache.commons.lang.StringUtils;

import com.wolvencraft.yasp.exceptions.RuntimeSQLException;
import com.wolvencraft.yasp.settings.LocalConfiguration;
import com.wolvencraft.yasp.util.Message;

/**
 * A library designed to execute extremely long database queries from file
 * @author MyBatis Team
 * @author ChaseHQ
 * @author bitWolfy
 *
 */
public class ScriptRunner {

    private static final String LINE_SEPARATOR = System.getProperty("line.separator", "\n");
    private static final String DEFAULT_DELIMITER = ";";

    private Connection connection;

    private String delimiter = ScriptRunner.DEFAULT_DELIMITER;
    private boolean fullLineDelimiter = false;
    
    /**
     * <b>Constructor</b><br />
     * Creates a new ScriptRunner instance
     * @param connection Database connection instance
     */
    public ScriptRunner(Connection connection) {
        this.connection = connection;
    }
    
    /**
     * Sets a custom delimiter
     * @param delimiter Delimiter to set
     */
    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }
    
    /**
     * Sets a custom full line delimiter
     * @param fullLineDelimiter Delimiter to set
     */
    public void setFullLineDelimiter(boolean fullLineDelimiter) {
        this.fullLineDelimiter = fullLineDelimiter;
    }
    
    /**
     * Executes a database script
     * @param reader Reader
     * @throws RuntimeSQLException thrown if an error occurs while executing a line
     */
    public void runScript(Reader reader) throws RuntimeSQLException {
        Message.log(Level.FINER, "Executing a database script");

        try {
            StringBuilder command = new StringBuilder();
            try {
                BufferedReader lineReader = new BufferedReader(reader);
                int i = 0;
                String line = "";
                String dbName = LocalConfiguration.DBName.toString();
                String dbPrefix = LocalConfiguration.DBPrefix.toString();
                boolean debug = LocalConfiguration.Debug.toBoolean();
                while ((line = lineReader.readLine()) != null) {
                    line = StringUtils.replace(line, "$dbname", dbName);
                    line = StringUtils.replace(line, "$prefix_", dbPrefix);
                    command = this.handleLine(command, line);
                    i++;
                    if(i % 50 == 0 && debug) Message.log(Level.FINEST, "Executing line " + i);
                }
                Message.log(Level.FINER, "Executed " + i + " lines total");
                this.commitConnection();
                this.checkForMissingLineTerminator(command);
            } catch (Exception e) {
                String message = "Error executing: " + command + ".  Cause: " + e;
                throw new RuntimeSQLException(message, e);
            }
        }
        finally { this.rollbackConnection(); }
    }
    
    /**
     * Closes the database connection
     */
    public void closeConnection() {
        try { this.connection.close(); }
        catch (Exception e) { }
    }
    
    /**
     * Commits the changes to the database
     * @throws RuntimeSQLException thrown if unable to commit the transaction.
     */
    private void commitConnection() throws RuntimeSQLException {
        try { this.connection.commit(); }
        catch (Throwable t) { throw new RuntimeSQLException("Could not commit transaction. Cause: " + t, t); }
    }
    
    /**
     * Rolls back the connection
     */
    private void rollbackConnection() {
        try { this.connection.rollback(); }
        catch (Throwable t) { }
    }
    
    /**
     * Checks for the missing line terminator
     * @param command Line to check
     * @throws RuntimeSQLException Thrown if the line is missing a terminator
     */
    private void checkForMissingLineTerminator(StringBuilder command) throws RuntimeSQLException {
        if (command != null && command.toString().trim().length() > 0) {
            throw new RuntimeSQLException("Line missing end-of-line terminator (" + this.delimiter + ") => " + command);
        }
    }
    
    /**
     * Handles the individual line
     * @param command
     * @param line
     * @return Parsed string
     * @throws SQLException
     * @throws UnsupportedEncodingException
     */
    private StringBuilder handleLine(StringBuilder command, String line) throws SQLException, UnsupportedEncodingException {
        String trimmedLine = line.trim();
        if (trimmedLine.toLowerCase().startsWith("delimiter")) {
            this.setDelimiter(trimmedLine.substring(10));
        } else if (this.lineIsComment(trimmedLine)) {
        } else if (this.commandReadyToExecute(trimmedLine)) {
            command.append(line.substring(0, line.lastIndexOf(this.delimiter)));
            command.append(ScriptRunner.LINE_SEPARATOR);
            this.executeStatement(command.toString());
            command.setLength(0);
        } else if (trimmedLine.length() > 0) {
            command.append(line);
            command.append(ScriptRunner.LINE_SEPARATOR);
        }
        return command;
    }
    
    /**
     * Checks if the specified line is a comment
     * @param trimmedLine Line to check
     * @return <b>true</b> if the line is a comment, <b>false</b> otherwise
     */
    private boolean lineIsComment(String trimmedLine) {
        return trimmedLine.startsWith("//") || trimmedLine.startsWith("--");
    }
    
    /**
     * Checks if the specified line is ready to be executed
     * @param trimmedLine Line to check
     * @return <b>true</b> if the line is ready to be executed, <b>false</b> otherwise
     */
    private boolean commandReadyToExecute(String trimmedLine) {
        return !this.fullLineDelimiter && trimmedLine.endsWith(this.delimiter) || this.fullLineDelimiter && trimmedLine.equals(this.delimiter);
    }
    
    /**
     * Executes the specified command
     * @param command Command to execute
     * @throws SQLException
     * @throws UnsupportedEncodingException
     */
    private void executeStatement(String command) throws SQLException, UnsupportedEncodingException {
        Statement statement = this.connection.createStatement();
        String sql = command;
        sql = sql.replaceAll("\r\n", "\n");
        
        statement.execute(sql);
        
        try { statement.close(); }
        catch (Exception e) { }
    }
    
}
