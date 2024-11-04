package by.TyapkovArtem.quizer.generators.math;

import by.TyapkovArtem.quizer.TaskGenerator;

public interface MathTaskGenerator extends TaskGenerator {
    int GetMaximum();
    int GetMinimum();
    default int GetDiff() {
        return  GetMaximum() - GetMinimum();
    }
}
