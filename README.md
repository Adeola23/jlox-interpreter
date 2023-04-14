# jlox is an interpreted scripting programming language implemented by reading the book Crafting intepreters

## Syntax Of jlox
jlox is a high-level scripting interpreted garbage collected language with dynamic typing. Functions are first class in Lox, which just means they are real values that you can get a reference to, store in variables, pass around.


## Variables
```
    var x = 15; // number
    var name = "your name"; // string
    var yes = true; // booleans
    var no = false; // booleans
    var nullable = nil;
```

## Logical operators
```
    var x = 15;
    if(x > 0){
        print "x > 0";
    }else {
        print "x <= 0";    
    }
```


## Loops
```
       var i = 0;
    while(i < 10){
        print i;
        i = i + 1;
    }
    
    for(var j=0; j<5; j = j + 1){
        print j;
    }
```

## Functions
```
        fun greetings(name){
        print "hello " + name;
    }
    
    greetings("Cristiano Ronaldo");
```

## Closures
```
    fun addPair(a, b) {
  return a + b;
}

fun identity(a) {
  return a;
}

print identity(addPair)(1, 2); // Prints "3".

fun makeCounter(){
    var c = 0;
    fun counter(){
        c = c + 1;
        print c;
    }
    return counter;
}

var counter1 = makeCounter();
var counter2 = makeCounter();

counter1(); // 1
counter2(); // 1
```

## Classes
```
     class Animal {
        
        init(aname){
            this.name = aname;
        }
        
        makeNoise(){
            print "animal noise";
        }
        
        printDetails(){
            print this.name;
        }
    }
    
    var animal = Animal("something");
    print animal;
    animal.makeNoise();
    animal.printDetails();
```


## Inheritance
```
      class Dog < Animal {
        init(aname, age){
            super.init(aname);
            this.age = age;
        }
        
        makeNoise(){
            print "woof woof";
        }
        
        printDetails(){
            super.printDetails();
            print this.age;
        }
    }
    var dog = Dog("doggo", 2);
    print dog;
    dog.makeNoise();
    dog.printDetails();

```

## File structure

- Lox.java [entry point of the program]
- AST [classes to represent the ast nodes]
- Enviroment [classes for semantic analysis and runtime representation of the functions and classes in lox]
- Intepreter [classes for the interpreter implemented with the visitor pattern]
- Parser [classes for parsing the code in to a syntax tree]
- Scanner [classes for lexical analysis]
- Utils [utility classes]
- Error [error handlers]
- Tests [lox program's to test the interpreter]

## Topics covered

- tokens and lexing
- abstract syntax trees
- recursive descent parsing
- prefix and infix expressions
- runtime representation of objects
- interpreting code using the Visitor pattern
- lexical scope
- environment chains for storing variables
- control flow
- functions with parameters
- closures
- static variable resolution and error detection
- classes
- constructors
- fields
- methods
- inheritance
