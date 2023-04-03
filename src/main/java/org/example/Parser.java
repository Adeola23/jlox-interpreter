package org.example;
import java.util.ArrayList;
import java.util.List;

import static org.example.TokenType.*;

class Parser {

    private static class ParseError extends RuntimeException{}
    private final List<Token> tokens;
    private int current = 0;

    Parser (List<Token> tokens){
        this.tokens = tokens;

    }

    List<Stmt> parse(){
        List<Stmt> statements = new ArrayList<>();
        while(!isAtEnd()){
            statements.add(statement());


        }
        return statements;
    }

//    Expr parse(){
//        try {
//            return expression();
//        } catch (ParseError error){
//            return null;
//        }
//    }

    private Expr expression(){
        return equality();

    }

    private Stmt statement(){
        if(match(PRINT)) return printStatement();
        return expressionStatement();
    }

    private Stmt printStatement(){
        Expr value = expression();
        consume(SEMICOLON, "Expect ';' after value.");
        return new Stmt.Print(value);
    }

    private Stmt expressionStatement(){
        Expr expr = expression();
        consume(SEMICOLON, "Expect ';' after expression.");
        return new Stmt.Expression(expr);
    }

    // The first comparison nonterminal in the body translates
    // to the first call to the comparison() in the method.

    private Expr equality(){
        Expr expr = comparison();

        while(match (BANG_EQUAL,  EQUAL_EQUAL)){
            Token operator = previous();
            Expr right = comparison();
            expr = new Expr.Binary(expr, operator, right);
        }

        return expr;

    }

    private Expr comparison(){
        Expr expr =     term();
        while(match (GREATER, GREATER_EQUAL, LESS, LESS_EQUAL)){
            Token operator = previous();
            Expr right = term();
            expr = new Expr.Binary(expr, operator, right);
        }

        return expr;
    }

    private Expr term(){
        Expr expr = factor();

        while (match(MINUS, PLUS)){
            Token operator = previous();
            Expr right = factor();
            expr = new Expr.Binary(expr, operator, right);
        }
        return expr;
    }

    private Expr factor(){
        Expr expr = unary();

        while (match (SLASH, STAR)){
            Token operator = previous();
            Expr right = unary();
            expr = new Expr.Binary(expr, operator, right);
        }
        return expr;
    }

    private Expr unary(){
        if(match(BANG, MINUS)){
            Token operator = previous();
            Expr right = unary();
            return new Expr.Unary(operator
            , right);
        }
        return primary();
    }

    private  Expr primary() {
        if (match(FALSE)) return new Expr.Literal(false);
        if (match(TRUE)) return new Expr.Literal(true);
        if (match(NIL)) return new Expr.Literal(null);
        if (match(NUMBER, STRING)) {
            return new Expr.Literal(previous().literal);
        }

        if (match(LEFT_PAREN)) {
            Expr expr = expression();
            consume(RIGHT_PAREN, "Expect ')' after expression.");
            return new Expr.Grouping(expr);
        }

        throw error(peek(), "Expect expression.");


    }

    private boolean match(TokenType...types){
        for(TokenType type : types){
            if(check(type)){
                advance();
                return true;
            }
        }

        return false;
    }

    //It’s similar to match() in that it checks to see if the next token is of the expected type.
    // If so, it consumes it
    // and everything is groovy. If some other token is there, then we’ve hit an error.

    private Token consume(TokenType type, String message) {
        if(check(type)) return advance();

        throw error(peek(), message);
    }

    private ParseError error(Token token, String message){
        Lox.error(token, message);
        return new ParseError();
    }

    private void synchronize (){
        advance();

        while (!isAtEnd()){
            if(previous().type == SEMICOLON) return;

            switch (peek().type){
                case CLASS:
                case FUN:
                case VAR:
                case FOR:
                case IF:
                case WHILE:
                case PRINT:
                case RETURN:
                    return;
            }

            advance();
        }
    }

    private boolean check (TokenType type){
        if(isAtEnd()) return false;
        return peek().type == type;
    }

    private Token advance(){
        if(!isAtEnd()) current++;
        return previous();

    }



    //isAtEnd checks if we've run out of tokens to parse

    private boolean isAtEnd(){
        return peek().type == EOF;
    }


    //peek returns the current token we have yet to consume

    private Token peek() {
        return tokens.get(current);
    }


    //previous returns the most recently consumed token

    private Token previous(){
        return tokens.get(current-1);
    }
}
