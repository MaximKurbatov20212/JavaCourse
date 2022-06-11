package com.github.maxwell.bencodeparser;

import com.github.maxwell.bencodeparser.lexer.Lexer;
import com.github.maxwell.bencodeparser.printer.JsonPrinter;
import com.github.maxwell.bencodeparser.parser.Parser;

import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException {
        // CR: user can give you any path, not just to sources
        FileWriter fileWriter = new FileWriter("src/main/resources/" + args[1]);
        // CR: second argument is optional
        FileReader fileReader = new FileReader("src/main/resources/" + args[0]);

        BufferedReader br = new BufferedReader(fileReader);
        String result = JsonPrinter.print(Parser.parse(Lexer.scan(br)));

        if(result != null) {
            fileWriter.write(result);
        }

        fileWriter.close();
        fileReader.close();
    }
}