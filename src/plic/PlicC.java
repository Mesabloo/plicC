package plic;

import plic.core.ProgramNode;
import plic.generator.MIPSGenerator;
import plic.lexer.Lexer;
import plic.lexer.exceptions.LexerException;
import plic.lexer.token.Token;
import plic.parser.Parser;
import plic.core.SyntaxTree;
import plic.parser.exceptions.ParserException;
import plic.typechecker.TypeChecker;
import plic.typechecker.core.SymbolTable;

import java.util.ArrayList;

public class PlicC {
    public static void main(String[] args) {
        new PlicC(args);
    }

    public PlicC(String[] args) {
        try {
            // We do not handle multiple files for now.
            if (args.length > 1)
                throw new PlicException("Too much files given to compile...");
            if (args.length < 1)
                throw new PlicException("No file given to compile...");

            final String path = args[0];
            if (!path.endsWith(".plic"))
                throw new PlicException("Wrong type of file given. Expected '.plic'.");

            ArrayList<Token> tokens = new Lexer(path).lex();
            SyntaxTree ast = new Parser(tokens).parse();
            SymbolTable syms = new TypeChecker().check(ast);

            MIPSGenerator.symbols.append(syms);
            System.out.println(ast.generateMIPS(new StringBuilder(), 0));
        } catch (LexerException | ParserException | PlicException e) {
            System.err.printf("ERREUR: %s\n", e.getMessage());
            System.exit(1);
        } catch (Exception e) {
            System.err.printf("ERREUR: native exception: %s (something is probably not handled correctly)", e.toString());
            System.exit(2);
        }
    }
}
