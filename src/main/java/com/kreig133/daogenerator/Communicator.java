package com.kreig133.daogenerator;

import org.jetbrains.annotations.NotNull;

import java.io.*;

/**
 * @author eshangareev
 * @version 1.0
 */
public class Communicator {

    public static Process communicate( String[] cmdArray ) throws IOException {
        Process exec = Runtime.getRuntime().exec( cmdArray );
        Communicator.communicate(
                exec,
                new OutputStreamWriter( System.out, "Cp866" ), //написано под винду, да
                new OutputStreamWriter( System.err, "Cp866" )
        );
        return exec;
    }

    /**
     *  Handle communication with a process, reading its output/error and feeding its input
     *  @param process The process to execute
     *  @param out Writer that will receive the output of the process
     *  @param err Writer that will receive the error pipe of the process
     */
    private static void communicate(
            @NotNull Process process,
            @NotNull final Writer out,
            @NotNull final Writer err )
    {

        // Final versions of the the params, to be used within the threads
        final BufferedReader stdOut = new BufferedReader(new InputStreamReader(process.getInputStream()));
        final BufferedReader stdErr = new BufferedReader(new InputStreamReader(process.getErrorStream()));

        // Thread that reads std out and feeds the writer given in input
        new Thread() {
            @Override public void run() {
                String line;
                try {
                    while ((line = stdOut.readLine()) != null) {
                        out.write(line + "\n");
                        out.flush();
                    }
                } catch (Exception e) {throw new Error(e);}
                try {
                    out.flush();
                } catch (IOException e) { /* Who cares ?*/ }
            }
        }.start(); // Starts now

        // Thread that reads std err and feeds the writer given in input
        new Thread() {
            @Override public void run() {
                String line;
                try {
                    while ((line = stdErr.readLine()) != null) {
                        err.write(line + "\n");
                        err.flush();
                    }
                } catch (Exception e) {throw new Error(e);}
                try {
                    err.flush();
                } catch (IOException e) { /* Who cares ?*/ }
            }
        }.start(); // Starts now

        // Wait until the end of the process
        try {
            process.waitFor();
        } catch (Exception e) {
            throw new Error(e);
        }

    }
}
