package by.TyapkovArtem.quizer.tasks.math;

import by.TyapkovArtem.quizer.Operation;

public class ExpressionTask extends AbstractMathTask {
    public ExpressionTask(int a, int b, Operation op) {
        super(a + " " + op.Symbol + " " + b + " = X", op.Funciton(a, b));
    }
}