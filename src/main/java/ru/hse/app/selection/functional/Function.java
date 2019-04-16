package ru.hse.app.selection.functional;

public class Function {

    private String expression;
    private ICalculator calculator;

    public Function(String expression) {
        this.expression = expression;
        analyze();
    }

    private void analyze() {
        if(expression.isEmpty()) {
            throw new IllegalArgumentException("Неверный формат функции.");
        }

        //Find +
        int index = expression.indexOf('+');
        while(index >= 0) {
            String leftOperandExpr = expression.substring(0, index);
            String rightOperandExpr = expression.substring(index + 1);
            if(checkParentheses(leftOperandExpr) &&
                    checkParentheses(rightOperandExpr)) {
                Function leftOperand = new Function(leftOperandExpr);
                Function rightOperand = new Function(rightOperandExpr);
                this.calculator = (value) -> leftOperand.calculate(value) + rightOperand.calculate(value);
                return;
            }
            index = expression.indexOf('+', index + 1);
        }

        //Find -
        index = expression.indexOf('-');
        while(index >= 0) {
            String leftOperandExpr = expression.substring(0, index);
            String rightOperandExpr = expression.substring(index + 1);
            if(checkParentheses(leftOperandExpr) &&
                    checkParentheses(rightOperandExpr)) {
                Function leftOperand = new Function(leftOperandExpr);
                Function rightOperand = new Function(rightOperandExpr);
                this.calculator = (value) -> leftOperand.calculate(value) - rightOperand.calculate(value);
                return;
            }
            index = expression.indexOf('-', index + 1);
        }

        //Find *
        index = expression.indexOf('*');
        while(index >= 0) {
            String leftOperandExpr = expression.substring(0, index);
            String rightOperandExpr = expression.substring(index + 1);
            if(checkParentheses(leftOperandExpr) &&
                    checkParentheses(rightOperandExpr)) {
                Function leftOperand = new Function(leftOperandExpr);
                Function rightOperand = new Function(rightOperandExpr);
                this.calculator = (value) -> leftOperand.calculate(value) * rightOperand.calculate(value);
                return;
            }
            index = expression.indexOf('*', index + 1);
        }

        //Find ^
        index = expression.indexOf('^');
        while(index >= 0) {
            String leftOperandExpr = expression.substring(0, index);
            String rightOperandExpr = expression.substring(index + 1);
            if(checkParentheses(leftOperandExpr) &&
                    checkParentheses(rightOperandExpr)) {
                Function leftOperand = new Function(leftOperandExpr);
                Function rightOperand = new Function(rightOperandExpr);
                this.calculator = (value) -> (int)Math.pow(leftOperand.calculate(value), rightOperand.calculate(value));
                return;
            }
            index = expression.indexOf('^', index + 1);
        }

        //Find ()
        if(expression.charAt(0) == '(' && expression.charAt(expression.length() - 1) == ')') {
            String expr = expression.substring(1, expression.length() - 1);
            Function operand = new Function(expr);
            this.calculator = (value) -> operand.calculate(value);
            return;
        }

        //Find variable
        if(expression.equals("n")) {
            this.calculator = (value) -> value;
            return;
        }

        //Find number
        try{
            int number = Integer.parseInt(expression);
            this.calculator = (value) -> number;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Неверный формат функции: " + expression);
        }
    }

    private boolean checkParentheses(String expr) {
        int openPar = 0;
        int closePar = 0;
        for(int i = 0; i < expr.length(); ++i) {
            char symbol = expr.charAt(i);
            if(symbol == '(') {
                openPar++;
            }
            if(symbol == ')') {
                closePar++;
            }
        }
        return openPar == closePar;
    }

    public int calculate(int value) {
        return calculator.calculate(value);
    }

}
