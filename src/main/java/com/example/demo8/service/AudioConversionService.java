package com.example.demo8.service;



import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class AudioConversionService {

    private static final String FFmpeg_PATH = "ffmpeg"; // Update this with the path to your FFmpeg binary

    public void convertMp4ToM4a(String inputFilePath, String outputFilePath) throws IOException, InterruptedException {
        ProcessBuilder processBuilder = new ProcessBuilder(
                FFmpeg_PATH,
                "-i", inputFilePath,
                "-acodec", "aac",
                "-b:a", "192k",
                outputFilePath
        );

        Process process = processBuilder.start();
        int exitCode = process.waitFor();

        if (exitCode != 0) {
            throw new IOException("FFmpeg process failed with exit code " + exitCode);
        }
    }
}
