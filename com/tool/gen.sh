#!/usr/bin/env bash

echo 'compile GenerateAst'

javac GenerateAst.java


echo 'generate AST'

cd ../../

dir="$(pwd)"

echo "now in dir: $dir"


java  com/tool/GenerateAst "com/craftinginterpreters/lox"

