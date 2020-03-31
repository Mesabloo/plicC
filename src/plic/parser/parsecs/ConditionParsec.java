package plic.parser.parsecs;

import data.either.Either;
import data.product.Product;
import plic.core.instruction.ConditionalInstructionNode;
import plic.core.instruction.IfThenElseNode;
import plic.core.instruction.IfThenNode;
import plic.lexer.token.Token;
import plic.parser.Parsec;
import plic.parser.stream.Reader;
import text.parser.combinators.error.ParseError;

public class ConditionParsec implements Parsec<ConditionalInstructionNode> {
    @Override
    public Product<Reader, Either<ParseError<Token, Reader>, ConditionalInstructionNode>> apply(Reader reader) {
        return new IfThenElseParsec().try_().orElse(new IfThenParsec()).parse(reader);
    }

    private class IfThenElseParsec implements Parsec<ConditionalInstructionNode> {
        @Override
        public Product<Reader, Either<ParseError<Token, Reader>, ConditionalInstructionNode>> apply(Reader reader) {
            return new KeywordParsec("si")
                .then(new SymbolParsec("(")
                    .then(new ValueParsec())
                    .then_(new SymbolParsec(")"))
                )
                .bind(condition -> new KeywordParsec("alors")
                    .then(new BlockParsec())
                    .bind(then -> new KeywordParsec("sinon")
                        .then(new BlockParsec())
                        .fmap(else_ -> new IfThenElseNode(condition, then, else_))
                    )
                )
                .fmap(c -> (ConditionalInstructionNode) c)
                .parse(reader);
        }
    }

    private class IfThenParsec implements Parsec<ConditionalInstructionNode> {
        @Override
        public Product<Reader, Either<ParseError<Token, Reader>, ConditionalInstructionNode>> apply(Reader reader) {
            return new KeywordParsec("si")
                .then(new SymbolParsec("(")
                    .then(new ValueParsec())
                    .then_(new SymbolParsec(")"))
                )
                .bind(condition -> new KeywordParsec("alors")
                    .then(new BlockParsec())
                    .fmap(then -> new IfThenNode(condition, then))
                )
                .fmap(c -> (ConditionalInstructionNode) c)
                .parse(reader);
        }
    }
}
