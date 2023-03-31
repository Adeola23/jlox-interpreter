package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;


public class Lox {
    static  boolean hadError = false;
    public static void main(String[] args)  throws IOException{
        if (args.length > 1){
            System.out.println("Usage: jlox [script]");
            System.exit(64);
        } else if (args.length == 1){
            runFile(args[0]);
        } else {
            runPrompt();
        }
        System.out.println("Hello world!");
    }




     // Lox is a scripting language, which means it executes directly
    // from  source. If you start jlox from the command line and
    // give it a path to file, it reads the file and executes it.

    private static void runFile(String path) throws IOException{
        byte[] bytes = Files.readAllBytes(Paths.get(path));
        run (new String(bytes, Charset.defaultCharset())); // creating a string object from bytes
        if (hadError) System.exit(65);
    }

    //Interactive prompt

    private static void runPrompt() throws  IOException {
        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input);
        for(;;){
            System.out.println("> ");
            run(reader.readLine());
            hadError= false;
        }

    }



    private static  void  run(String source){
        Scanner scanner = new Scanner(source);
        List<Token> tokens = scanner.scanTokens();
        Parser parser = new Parser(tokens);
        Expr expression = parser.parse();
        //stop if there was a syntax error

        if(hadError) return;

        System.out.println(new AstPrinter().print(expression));
    }

    //error handling
    // It's a good engineering practice to separate the code that generates
    // the error from the code that reports them

    static void error (int line, String message){
        report (line,"", message);
    }

    private static void report (int line, String where, String message){
        System.err.println("[line " + line + "] Error" + where + ": " + message);
        hadError = true;
    }

    static void error(Token token, String message){
        if(token.type == TokenType.EOF){
            report(token.line,  " at end", message);

        } else {
            report (token.line, " at  '" + token.lexeme + "'", message);
        }
    }

}