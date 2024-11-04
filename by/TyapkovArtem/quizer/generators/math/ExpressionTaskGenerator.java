package by.TyapkovArtem.quizer.generators.math;

import by.TyapkovArtem.quizer.Operation;
import by.TyapkovArtem.quizer.Task;
import by.TyapkovArtem.quizer.tasks.math.ExpressionTask;

public class ExpressionTaskGenerator extends AbstractMathTaskGenerator {
    public ExpressionTaskGenerator(Config config) {
        super(config);
    }

    @Override
    public Task generate() {
        Operation oper = GenNextOp_();
        int a = GenNextArg_();
        int b = GenNextArg_();
        if (oper.equals(Operation.Division)) {
            return new ExpressionTask(a * b, b, oper);
        }
        return new ExpressionTask(a, b, oper);
    }
}
