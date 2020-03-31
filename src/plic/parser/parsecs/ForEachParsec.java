package plic.parser.parsecs;

import data.either.Either;
import data.product.Product;
import plic.core.instruction.ForEachNode;
import plic.lexer.token.Token;
import plic.parser.Parsec;
import plic.parser.stream.Reader;
import text.parser.combinators.error.ParseError;

public class ForEachParsec implements Parsec<ForEachNode> {
    @Override
    public Product<Reader, Either<ParseError<Token, Reader>, ForEachNode>> apply(Reader reader) {
        return new KeywordParsec("pour")
            .then(new IdentifierParsec())
            .then_(new KeywordParsec("dans"))
            .bind(index -> new ValueParsec()
                .bind(begin -> new SymbolParsec("..")
                    .then(new ValueParsec())
                    .bind(end -> new KeywordParsec("repeter")
                        .then(new BlockParsec())
                        .fmap(block -> new ForEachNode(index, begin, end, block))
                    )
                )
            )
            .parse(reader);
    }
}
