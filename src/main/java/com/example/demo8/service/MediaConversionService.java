package com.example.demo8.service;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.springframework.stereotype.Service;
import ws.schild.jave.EncoderException;
import ws.schild.jave.EncodingAttributes;

import java.beans.Encoder;
import java.io.File;
import java.io.IOException;
@Service
public class MediaConversionService {

    public void convertMp4ToM4a(String inputFilePath, String outputFilePath) {
        CommandLine commandLine = new CommandLine("ffmpeg");
        commandLine.addArgument("-i");
        commandLine.addArgument(inputFilePath);
        commandLine.addArgument("-c:a");
        commandLine.addArgument("aac");
        commandLine.addArgument("-b:a");
        commandLine.addArgument("128k");
        commandLine.addArgument(outputFilePath);

        DefaultExecutor executor = new DefaultExecutor();
        try {
            executor.execute(commandLine);
            System.out.println("Conversion completed successfully.");
        } catch (ExecuteException e) {
            System.err.println("Conversion failed with exit code: " + e.getExitValue());
        } catch (IOException e) {
            System.err.println("Conversion failed: " + e.getMessage());
        }
    }
}