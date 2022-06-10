package com.github.maxwell.bencodeparser;

import com.github.maxwell.bencodeparser.lexer.Lexer;
import com.github.maxwell.bencodeparser.parser.JsonPrinter;
import com.github.maxwell.bencodeparser.parser.Parser;

import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException {
        FileWriter fileWriter = new FileWriter("src/main/resources/" + args[1]);
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