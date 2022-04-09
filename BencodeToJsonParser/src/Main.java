import Lexer.Lexer;
import Parser.Parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

public class Main {
    public static void main(String[] args) throws IOException {
//        String str = "d  e"; // error
//        String str = "d d e e"; // error
        String str = "d 1:a i1e e"; // ok
//        String str = "d 1:a d i1e e e"; // error
//        String str = "d 1:a d 1:b i1e e e"; // ok
//        String str = "d 1:a d 1:b i1e e e"; // ok
//        String str = "d 1:a e"; // error
//        String str = "d 1:a 1:b e"; // ok
//        String str = "d 1:a l i1e e e"; // ok
//        String str = "d8:announce41:http://bttracker.debian.org:6969/announce7:comment33:Debian CD from cdimage.debian.org13:creation datei1573903810e9:httpseedsl145:https://cdimage.debian.org/cdimage/release/10.2.0//srv/cdbuilder.debian.org/dst/deb-cd/weekly-builds/amd64/iso-cd/debian-10.2.0-amd64-netinst.iso145:https://cdimage.debian.org/cdimage/archive/10.2.0//srv/cdbuilder.debian.org/dst/deb-cd/weekly-builds/amd64/iso-cd/debian-10.2.0-amd64-netinst.isoe4:infod6:lengthi351272960e4:name31:debian-10.2.0-amd64-netinst.iso12:piece lengthi262144e6:piecesi123eee";
        BufferedReader br = new BufferedReader(new StringReader(str));
        Lexer lexer = new Lexer(br);
        Parser parser = new Parser(lexer.getTokens());
        parser.parse();
    }
}