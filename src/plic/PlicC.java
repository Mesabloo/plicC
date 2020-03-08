package plic;

import plic.lexer.Lexer;
import plic.lexer.token.Token;
import plic.parser.Parser;
import plic.parser.ast.SyntaxTree;

import java.util.ArrayList;

public class PlicC {
    public static void main(String[] args) {
        new PlicC(args);
    }

    public PlicC(String[] args) {
        try {
            // We do not handle multiple files for now.
            if (args.length > 1)
                throw new Exception("Too much files given to compile...");
            if (args.length < 1)
                throw new Exception("No file given to compile...");

            final String path = args[0];
            if (!path.endsWith(".plic"))
                throw new Exception("Wrong type of file given. Expected '.plic'.");

            ArrayList<Token> tokens = new Lexer(path).lex();
            SyntaxTree ast = new Parser(tokens).parse();
            // We do not do anything with the AST right now, but soon.
        } catch (Exception e) {
            System.err.printf("ERREUR: %s\n", e.getMessage());
            System.exit(1);
        }
    }
}
