package com.github.maxwell.bencodeparser;

import com.github.maxwell.bencodeparser.lexer.Lexer;
import com.github.maxwell.bencodeparser.parser.JsonPrinter;
import com.github.maxwell.bencodeparser.parser.Parser;

import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException {
        // CR: please fix program input / output according to task: https://docs.google.com/document/d/1cCvY-qxurNCsEtj-expMcRlBJkq1FuLW4vDDOn426_c
        FileReader fileReader = new FileReader("src/main/resources/in.txt");
        BufferedReader br = new BufferedReader(fileReader);
        // CR: npe if Lexer returned null
        System.out.println(JsonPrinter.print(Parser.parse(Lexer.scan(br))));
    }
}