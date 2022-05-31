package com.github.maxwell.bencodeparser;

import com.github.maxwell.bencodeparser.lexer.Lexer;
import com.github.maxwell.bencodeparser.parser.JsonPrinter;
import com.github.maxwell.bencodeparser.parser.Parser;

import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException {
        FileReader fileReader = new FileReader("src/main/resources/in.txt");
        BufferedReader br = new BufferedReader(fileReader);
        System.out.println(JsonPrinter.print(Parser.parse(Lexer.scan(br))));
    }
}