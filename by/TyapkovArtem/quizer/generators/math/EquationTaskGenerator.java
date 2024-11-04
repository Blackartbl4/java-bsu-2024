package by.TyapkovArtem.quizer.generators.math;

import by.TyapkovArtem.quizer.Operation;
import by.TyapkovArtem.quizer.Task;
import by.TyapkovArtem.quizer.tasks.math.EquationTask;


public class EquationTaskGenerator extends AbstractMathTaskGenerator {
    public EquationTaskGenerator(Config config) {
        super(config);
    }

    @Override
    public Task generate() {
        boolean XisSecond = Rand_.nextBoolean();
        int a = GenNextArg_();
        int b = GenNextArg_();
        Operation oper = GenNextOp_();
        if (oper.equals(Operation.Division) && !XisSecond) {
            return new EquationTask(XisSecond, a * b, a, b, oper);
        } else if (oper.equals(Operation.Division)) {
            return new EquationTask(XisSecond, a, a * b, b, oper);
        } else if (oper.equals(Operation.Subtraction) && !XisSecond) {
            return new EquationTask(XisSecond, a + b, a, b, oper);
        } else if (oper.equals(Operation.Subtraction)) {
            return new EquationTask(XisSecond, a, a + b, b, oper);
        } else return new EquationTask(XisSecond, a, b, oper.Funciton(a , b), oper);
    }
}