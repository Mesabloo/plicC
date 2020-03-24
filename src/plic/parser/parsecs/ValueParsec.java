package plic.parser.parsecs;

import data.either.Either;
import data.product.Product;
import plic.core.expression.ValueNode;
import plic.core.expression.binary.*;
import plic.core.expression.unary.NegateExprNode;
import plic.core.expression.unary.NotExprNode;
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
                    .fmap(v -> new NegateExprNode().set(v)))
                .fmap(v -> (ValueNode) v)
                .orElse(new KeywordParsec("non")
                    .then(new ValueParsec())
                    .fmap(v -> new NotExprNode().set(v)))
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
                .orElse(new SymbolParsec("-")
                        .fmap(s_ -> new SubOperatorNode()))
                .orElse(new SymbolParsec("*")
                        .fmap(s_ -> new MulOperatorNode()))
                .orElse(new SymbolParsec(">")
                        .fmap(s_ -> new GreaterOperatorNode()))
                .orElse(new SymbolParsec("<")
                        .fmap(s_ -> new LowerOperatorNode()))
                .orElse(new SymbolParsec(">=")
                        .fmap(s_ -> new GreaterEqOperatorNode()))
                .orElse(new SymbolParsec("<=")
                        .fmap(s_ -> new LowerEqOperatorNode()))
                .orElse(new SymbolParsec("=")
                        .fmap(s_ -> new EqualOperatorNode()))
                .orElse(new SymbolParsec("#")
                        .fmap(s_ -> new DifferentOperatorNode()))
                .orElse(new KeywordParsec("et")
                        .fmap(s_ -> new AndOperatorNode()))
                .orElse(new KeywordParsec("ou")
                        .fmap(s_ -> new OrOperatorNode()))
                .parse(reader);
        }
    }
}
