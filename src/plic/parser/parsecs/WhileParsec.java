package plic.parser.parsecs;

import data.either.Either;
import data.product.Product;
import plic.core.instruction.WhileNode;
import plic.lexer.token.Token;
import plic.parser.Parsec;
import plic.parser.stream.Reader;
import text.parser.combinators.error.ParseError;

public class WhileParsec implements Parsec<WhileNode> {
    @Override
    public Product<Reader, Either<ParseError<Token, Reader>, WhileNode>> apply(Reader reader) {
        return new KeywordParsec("tantque")
            .then(new SymbolParsec("(")
                .then(new ValueParsec())
                .then_(new SymbolParsec(")"))
            )
            .then_(new KeywordParsec("repeter"))
            .bind(cond -> new BlockParsec()
                .fmap(b -> new WhileNode(cond, b))
            )
            .parse(reader);
    }
}
