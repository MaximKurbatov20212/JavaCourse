package com.github.maxwell.bencodeparser;

import com.github.maxwell.bencodeparser.lexer.Lexer;
import com.github.maxwell.bencodeparser.printer.JsonPrinter;
import com.github.maxwell.bencodeparser.parser.Parser;

import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException {
        if (hasErrors(args)) {
            System.err.println("""
                    Expected arguments: <filename> [output-file]
                    - filename - torrent file in bencode format.
                    - output-file - output file in json format (optional argument). Default - "out.json".""");
            return;
        }

        String outputFile = (args.length == 1) ? "out.json" : args[1];

        try (BufferedReader br = new BufferedReader(new FileReader(args[0]));
             FileWriter fileWriter = new FileWriter(outputFile)) {

            String result = JsonPrinter.print(Parser.parse(Lexer.scan(br)));

            if (result != null) {
                fileWriter.write(result);
            }
        }
    }

    private static boolean hasErrors(String[] args) {
        if (args.length == 0) {
            System.err.println("Invalid input format: too few arguments.");
            return true;
        }
        if (args.length > 2) {
            System.err.println("Invalid input format: too many arguments.");
            return true;
        }
        return false;
    }
}