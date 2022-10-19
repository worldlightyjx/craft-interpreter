package com.craftinginterpreters.lox;

/**
 * @author yangjiaxin
 * @date 2022/10/19 11:36
 */
public class Interpreter implements Expr.Visitor {

    @Override
    public Object visitLiteralExpr(Expr.Literal expr) {
        return expr.value;
    }

    @Override
    public Object visitGroupingExpr(Expr.Grouping expr) {
        return evaluate(expr.expression);
    }

    @Override
    public Object visitBinaryExpr(Expr.Binary expr) {
        return null;
    }

    @Override
    public Object visitUnaryExpr(Expr.Unary expr) {
        Object right = evaluate(expr.right);

        switch (expr.operator.type) {
            case MINUS:
                return -(double) right;
        }

        return null;
    }

    private Object evaluate(Expr expr) {
        return expr.accept(this);
    }
}

