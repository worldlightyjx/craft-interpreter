package com.craftinginterpreters.lox;

public class AstPrinter implements Expr.Visitor<String> {
    String print(Expr expr) {
        return expr.accept(this);
    }

    @Override
    public String visitBinaryExpr(Expr.Binary expr) {
        return parenthesize(expr.operator.lexeme, expr.left, expr.right);
    }

    @Override
    public String visitGroupingExpr(Expr.Grouping expr) {
        return parenthesize("group", expr.expression);
    }

    @Override
    public String visitLiteralExpr(Expr.Literal expr) {
        return expr.value == null ? "nil" : expr.value.toString();
    }

    @Override
    public String visitUnaryExpr(Expr.Unary expr) {
        return parenthesize(expr.operator.lexeme, expr.right);
    }

    private String parenthesize(String name, Expr... exprs) {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("(").append(name);

        for (Expr expr : exprs) {
            stringBuilder.append(" ");
            stringBuilder.append(expr.accept(this));
        }
        stringBuilder.append(")");

        return stringBuilder.toString();
    }

    public static void main(String[] args) {
        Expr expr = new Expr.Binary(new Expr.Unary(new Token(TokenType.MINUS, "-", null, 1),
                new Expr.Literal(123)),
                new Token(TokenType.STAR, "*", null, 1), new Expr.Grouping(new Expr.Literal(45.67)));
        System.out.println(new AstPrinter().print(expr));
    }
}
