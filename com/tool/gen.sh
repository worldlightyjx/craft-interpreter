#!/usr/bin/env bash

echo 'compile GenerateAst'

javac GenerateAst.java


echo 'generate AST'

cd ../../

java  com/tool/GenerateAst "com/craftinginterpreters/lox"