package com.cennavi.doodle.util;

public class PriorityRunnable implements CompareRunnable<PriorityRunnable> {
    private final Priority mPriority;
    private final Runnable mRunnable;

    public PriorityRunnable(Priority priority) {
        this(priority, (Runnable)null);
    }

    public PriorityRunnable(Priority priority, Runnable runnable) {
        this.mPriority = priority == null ? Priority.DEFAULT : priority;
        this.mRunnable = runnable;
    }

    public int compareTo(PriorityRunnable another) {
        if (this.mPriority.ordinal() < another.mPriority.ordinal()) {
            return -1;
        } else {
            return this.mPriority.ordinal() > another.mPriority.ordinal() ? 1 : 0;
        }
    }

    public void run() {
        if (this.mRunnable != null) {
            this.mRunnable.run();
        }

    }
}
