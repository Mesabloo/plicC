package plic.parser.parsecs;

import data.either.Either;
import data.product.Product;
import plic.core.*;
import plic.lexer.token.Token;
import plic.parser.Parsec;
import plic.parser.stream.Reader;
import text.parser.combinators.error.ParseError;

/**
 * Parses any possible value.
 */
public class ValueParsec implements Parsec<ValueNode> {
    @Override
    public Product<Reader, Either<ParseError<Token, Reader>, ValueNode>> apply(Reader reader) {
        return (
            new TermParsec()
                .bind(e1 -> new OperatorParsec()
                    .bind(op -> new TermParsec()
                        .fmap(e2 -> op.set(e1, e2))
                    )
                )
            ).try_()
                .fmap(v -> (ValueNode) v)
            .orElse(new TermParsec())
            .parse(reader);
    }

    private static final class TermParsec implements Parsec<ValueNode> {
        @Override
        public Product<Reader, Either<ParseError<Token, Reader>, ValueNode>> apply(Reader reader) {
            return (new SymbolParsec("-")
                    .then(new ValueParsec())
                    .fmap(NegateExprNode::new))
                .fmap(v -> (ValueNode) v)
                .orElse(new KeywordParsec("non")
                    .then(new ValueParsec())
                    .fmap(NotExprNode::new))
                .orElse(new AccessParsec())
                .orElse(new IntegerParsec().fmap(v -> (ValueNode) v))
                .orElse(new SymbolParsec("(")
                        .then(new ValueParsec())
                        .then_(new SymbolParsec(")")))
                .parse(reader);
        }
    }

    private static final class OperatorParsec implements Parsec<BinaryOperatorNode> {
        @Override
        public Product<Reader, Either<ParseError<Token, Reader>, BinaryOperatorNode>> apply(Reader reader) {
            return (new SymbolParsec("+")
                        .fmap(s_ -> new AddOperatorNode())
                        .fmap(v -> (BinaryOperatorNode) v))

                .parse(reader);
        }
    }
}
