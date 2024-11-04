package by.TyapkovArtem.quizer.tasks.math;

import by.TyapkovArtem.quizer.Result;
import by.TyapkovArtem.quizer.tasks.AbstractTask;

public class AbstractMathTask extends AbstractTask {
    private int answer_;

    public AbstractMathTask(String text, int answer) {
        super(text);
        answer_ = answer;
    }

    @Override
    public Result validate(String answer) {
        try {
            int intanswer = Integer.parseInt(answer.trim());
            if (intanswer == answer_) {
                return Result.OK;
            } else return Result.WRONG;
        } catch (NumberFormatException nfe) {
            return Result.INCORRECT_INPUT;
        }
    }
}