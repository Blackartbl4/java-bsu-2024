package by.TyapkovArtem.quizer.tasks;

import by.TyapkovArtem.quizer.Task;

public abstract class AbstractTask implements Task {
    private final String text_;

    public AbstractTask(String text) {
        text_ = text;
    }

    @Override
    public String getText() {
        return text_;
    }
}
