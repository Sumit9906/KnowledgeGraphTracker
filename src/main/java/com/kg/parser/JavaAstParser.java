package com.kg.parser;



import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;

import java.io.File;

public class JavaAstParser {

    public static CompilationUnit parse(File file) throws Exception {
        return StaticJavaParser.parse(file);
    }
}

