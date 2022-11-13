package com.craftinginterpreters.lox;

/**
 * @author yangjiaxin
 * @date 2022/10/19 11:36
 */
public class Interpreter implements Expr.Visitor<Object> {

    void interpreter(Expr expression) {
        try {
            Object value = evaluate(expression);
            System.out.println(stringify(value));
        } catch (RuntimeError error) {
            Lox.runTimeError(error);
        }
    }

    private String stringify(Object value) {
        if (value == null) return "nil";
        if (value instanceof Double) {
            String text = value.toString();
            if (text.endsWith(".0")) {
                text = text.substring(0, text.length() - 2);
            }
            return text;
        }
        return value.toString();
    }

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
        Object left = evaluate(expr.left);
        Object right = evaluate(expr.right);

        switch (expr.operator.type) {
            case MINUS:
                checkNumberOperands(expr.operator,left,right);
                return (double) left - (double) right;
            case PLUS:
                if (left instanceof Double && right instanceof Double) {
                    return (double) left + (double) right;
                }

                if (left instanceof String && right instanceof String) {
                    return (String) left + (String) right;
                }
                throw new RuntimeError(expr.operator, "Operands mus be two numbers or two strings");
            case SLASH:
                checkNumberOperands(expr.operator,left,right);
                return (double) left / (double) right;
            case STAR:
                checkNumberOperands(expr.operator,left,right);
                return (double) left * (double) right;
            case GREATER:
                checkNumberOperands(expr.operator,left,right);
                return (double) left > (double) right;
            case GREATER_EQUAL:
                checkNumberOperands(expr.operator,left,right);
                return (double) left >= (double) right;
            case LESS:
                checkNumberOperands(expr.operator,left,right);
                return (double) left < (double) right;
            case LESS_EQUAL:
                checkNumberOperands(expr.operator,left,right);
                return (double) left <= (double) right;
            case BANG_EQUAL:
                return !isEqual(left, right);
            case EQUAL_EQUAL:
                return isEqual(left, right);
        }
        return null;
    }

    private boolean isEqual(Object left, Object right) {
        if (left == null && right == null) return true;
        if (left == null) return false;
        return left.equals(right);
    }

    @Override
    public Object visitUnaryExpr(Expr.Unary expr) {
        Object right = evaluate(expr.right);

        switch (expr.operator.type) {
            case BANG:
                return !isTruthy(right);
            case MINUS:
                checkNumberOperand(expr.operator, right);
                return -(double) right;
        }

        return null;
    }

    private void checkNumberOperand(Token operator, Object operand) {
        if (operand instanceof Double) return;
        throw new RuntimeError(operator, "Operand must be a number");
    }

    private void checkNumberOperands(Token operator, Object left, Object right) {
        if (left instanceof Double && right instanceof Double) return;
        throw new RuntimeError(operator, "Operands must be numbers");
    }

    private boolean isTruthy(Object right) {
        if (right == null) return false;
        if (right instanceof Boolean) return (boolean) right;
        return true;
    }

    private Object evaluate(Expr expr) {
        return expr.accept(this);
    }
}
