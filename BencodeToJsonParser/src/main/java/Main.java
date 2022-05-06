import Lexer.Lexer;
import Parser.Parser;

import Parser.JsonPrinter;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

public class Main {
    public static void main(String[] args) throws IOException {
//        String str = """
//               d
//                    5:hello 4:name
//                    7:numbers d
//                        5:Maxim i18e
//                        e
//                    4:list l e e""";

//        String str = "d 1:a d  e e"; // error
//          String str = "d 1:a e"; // ok
//        String str = "d 1:a d i1e e e"; // error
//        String str = "d 1:a d 1:b i1e e e"; // ok
//        String str = "d 1:a d 1:b i1e e e"; // ok
//        String str = "d 1:a e"; // error
//        String str = "d 1:a 1:b e"; // ok
//        String str = "d 1:a d e e"; // ok
//        String str = "d 1:a l i1e 3:abc 5:hello e e"; // ok
        String str = """
        d
            8:announce  41:http://bttracker.debian.org:6969/announce
            7:comment33:Debian CD from cdimage.debian.org
            13:creation date i1573903810e
            9:httpseeds
            l
                145:https://cdimage.debian.org/cdimage/release/10.2.0//srv/cdbuilder.debian.org/dst/deb-cd/weekly-builds/amd64/iso-cd/debian-10.2.0-amd64-netinst.iso
                145:https://cdimage.debian.org/cdimage/archive/10.2.0//srv/cdbuilder.debian.org/dst/deb-cd/weekly-builds/amd64/iso-cd/debian-10.2.0-amd64-netinst.iso
            e
            4:info
            d
                6:length i351272960e
                4:name 31:debian-10.2.0-amd64-netinst.iso
                12:piece length i262144e
                6:pieces i123e
            e
        e""";
//        BufferedReader br = new BufferedReader(new StringReader(str));
//        Lexer lexer = new Lexer(br);
//
//        Parser parser = new Parser(lexer.getTokens());
//        JsonPrinter printer = new JsonPrinter(parser.parse());
//        printer.print();
    }
}