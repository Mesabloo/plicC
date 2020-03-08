package plic.parser.parsecs;

import data.either.Either;
import data.product.Product;
import plic.lexer.token.Token;
import plic.parser.Parsec;
import plic.parser.ast.ProgramNode;
import plic.parser.stream.Reader;
import text.parser.combinators.Parseable;
import text.parser.combinators.error.ParseError;

public class ProgramParsec implements Parsec<ProgramNode> {
    @Override
    public Product<Reader, Either<ParseError<Token, Reader>, ProgramNode>> apply(Reader reader) {
        return new KeywordParsec("programme")
                .then(new IdentifierParsec())
                .bind(id -> new BlockParsec().fmap(stts -> new ProgramNode(id, stts.toList())))
                .then_(Parseable.eof())
                .parse(reader);
    }
}
