PROG        → 'programme' idf BLOC
BLOC        → { DECLARATION* INSTRUCTION+ }
DECLARATION → TYPE idf ;
TYPE        → 'entier' 
            | 'tableau' [ cstEntiere ]
INSTRUCTION → ES
            | AFFECTATION
            | CONDITION
            | ITERATION
ES          → 'ecrire' EXPRESSSION ;
            | 'lire' idF ;
AFFECTATION → ACCES := EXPRESSION ;
ACCES       → idF | idF [ EXPRESSION ]
CONDITION   → 'si' '(' EXPRESSION ')' 'alors' BLOC 'sinon' BLOC
            | 'si' '(' EXPRESSION ')' 'alors' BLOC
ITERATION   → 'pour' idF 'dans' EXPRESSION '..' EXPRESSION 'repeter' BLOC
            | 'tantque' '(' EXPRESSION ')' 'repeter' BLOC
EXPRESSION  → OPERANDE OPERATEUR OPERANDE
            | OPERANDE
OPERATEUR   → '+'
            | '-'
            | '*'
            | 'et'
            | 'ou'
            | '<'
            | '>'
            | '='
            | '#'
            | '<='
            | '>='
OPERANDE    → cstEntiere
            | ACCES
            | '-' '(' EXPRESSION ')'
            | 'non' EXPRESSION
            | '(' EXPRESSION ')'

; `idF` is defined as a non-empty sequence of alphabetic characters from 'a' to 'z' (case doesn't matter).
; `cstEntiere` is defined as a non-empty sequence of digits.
; comments begin with '//' and end at the end of the current line.
