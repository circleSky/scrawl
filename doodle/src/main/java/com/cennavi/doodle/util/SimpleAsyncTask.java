package com.cennavi.doodle.util;

import android.os.Binder;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Process;

import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public abstract class SimpleAsyncTask<Params, Progress, Result> {
    private static final String LOG_TAG = "SimpleAsyncTask";
    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    private static final int CORE_POOL_SIZE;
    private static final int MAXIMUM_POOL_SIZE;
    private static final int KEEP_ALIVE = 1;
    private static final ThreadFactory sThreadFactory;
    private static int sMaxSizeLIFO;
    private static final ThreadPoolExecutor EXECUTOR;
    private static final ThreadPoolExecutor EXECUTOR_LIFO;
    private static final ThreadPoolExecutor EXECUTOR_PRIORITY;
    private static final int MESSAGE_POST_RESULT = 1;
    private static final int MESSAGE_POST_PROGRESS = 2;
    private static volatile Executor sDefaultExecutor;
    private static SimpleAsyncTask.InternalHandler sHandler;
    private final SimpleAsyncTask.WorkerRunnable<Params, Result> mWorker;
    private final FutureTask<Result> mFuture;
    private volatile SimpleAsyncTask.Status mStatus;
    private final AtomicBoolean mCancelled;
    private final AtomicBoolean mTaskInvoked;

    private static Handler getHandler() {
        Class var0 = SimpleAsyncTask.class;
        synchronized(SimpleAsyncTask.class) {
            if (sHandler == null) {
                sHandler = new SimpleAsyncTask.InternalHandler();
            }

            return sHandler;
        }
    }

    public static void setDefaultExecutor(Executor exec) {
        sDefaultExecutor = exec;
    }

    public SimpleAsyncTask() {
        this.mStatus = SimpleAsyncTask.Status.PENDING;
        this.mCancelled = new AtomicBoolean();
        this.mTaskInvoked = new AtomicBoolean();
        this.mWorker = new SimpleAsyncTask.WorkerRunnable<Params, Result>() {
            public Result call() throws Exception {
                SimpleAsyncTask.this.mTaskInvoked.set(true);
                Process.setThreadPriority(10);
                Result result = SimpleAsyncTask.this.doInBackground(this.mParams);
                Binder.flushPendingCommands();
                return SimpleAsyncTask.this.postResult(result);
            }
        };
        this.mFuture = new FutureTask<Result>(this.mWorker) {
            protected void done() {
                try {
                    SimpleAsyncTask.this.postResultIfNotInvoked(this.get());
                } catch (InterruptedException var2) {
                    LogUtil.w("SimpleAsyncTask", var2);
                } catch (ExecutionException var3) {
                    throw new RuntimeException("An error occurred while executing doInBackground()", var3.getCause());
                } catch (CancellationException var4) {
                    SimpleAsyncTask.this.postResultIfNotInvoked((Result)null);
                }

            }
        };
    }

    public boolean reset() {
        if (SimpleAsyncTask.Status.RUNNING == this.mStatus) {
            return false;
        } else {
            this.mStatus = SimpleAsyncTask.Status.PENDING;
            this.mCancelled.set(false);
            this.mTaskInvoked.set(false);
            return true;
        }
    }

    private void postResultIfNotInvoked(Result result) {
        boolean wasTaskInvoked = this.mTaskInvoked.get();
        if (!wasTaskInvoked) {
            this.postResult(result);
        }

    }

    private Result postResult(Result result) {
        Message message = getHandler().obtainMessage(1, new SimpleAsyncTask.AsyncTaskResult(this, new Object[]{result}));
        message.sendToTarget();
        return result;
    }

    public final SimpleAsyncTask.Status getStatus() {
        return this.mStatus;
    }

    protected abstract Result doInBackground(Params... var1);

    protected void onPreExecute() {
    }

    protected void onPostExecute(Result result) {
    }

    protected void onProgressUpdate(Progress... values) {
    }

    protected void onCancelled(Result result) {
        this.onCancelled();
    }

    protected void onCancelled() {
    }

    public final boolean isCancelled() {
        return this.mCancelled.get();
    }

    public final boolean cancel(boolean mayInterruptIfRunning) {
        this.mCancelled.set(true);
        return this.mFuture.cancel(mayInterruptIfRunning);
    }

    public final Result get() throws InterruptedException, ExecutionException {
        return this.mFuture.get();
    }

    public final Result get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return this.mFuture.get(timeout, unit);
    }

    public final SimpleAsyncTask<Params, Progress, Result> execute(Params... params) {
        return this.executeOnExecutor(sDefaultExecutor, params);
    }

    public final SimpleAsyncTask<Params, Progress, Result> executeLIFO(Params... params) {
        return this.executeOnExecutor(EXECUTOR_LIFO, params);
    }

    public final SimpleAsyncTask<Params, Progress, Result> executePriority(Priority priority, Params... params) {
        if (priority == null) {
            throw new RuntimeException("priority is null!");
        } else {
            return this.executeOnExecutor(EXECUTOR_PRIORITY, priority, params);
        }
    }

    public final SimpleAsyncTask<Params, Progress, Result> executeOnExecutor(Executor exec, Params... params) {
        return this.executeOnExecutor(exec, (Priority)null, params);
    }

    private final SimpleAsyncTask<Params, Progress, Result> executeOnExecutor(Executor exec, Priority priority, Params... params) {
        if (this.mStatus != SimpleAsyncTask.Status.PENDING) {
            switch(this.mStatus) {
                case RUNNING:
                    throw new IllegalStateException("Cannot execute task: the task is already running.");
                case FINISHED:
                    throw new IllegalStateException("Cannot execute task: the task has already been executed (a task can be executed only once)");
            }
        }

        this.mStatus = SimpleAsyncTask.Status.RUNNING;
        this.onPreExecute();
        this.mWorker.mParams = params;
        if (priority != null) {
            exec.execute(new PriorityRunnable(priority, this.mFuture));
        } else {
            exec.execute(this.mFuture);
        }

        return this;
    }

    public static void execute(Runnable runnable) {
        sDefaultExecutor.execute(runnable);
    }

    protected final void publishProgress(Progress... values) {
        if (!this.isCancelled()) {
            getHandler().obtainMessage(2, new SimpleAsyncTask.AsyncTaskResult(this, values)).sendToTarget();
        }

    }

    private void finish(Result result) {
        if (this.isCancelled()) {
            this.onCancelled(result);
        } else {
            this.onPostExecute(result);
        }

        this.mStatus = SimpleAsyncTask.Status.FINISHED;
    }

    static {
        CORE_POOL_SIZE = CPU_COUNT + 1;
        MAXIMUM_POOL_SIZE = CPU_COUNT * 2 + 1;
        sThreadFactory = new ThreadFactory() {
            private final AtomicInteger mCount = new AtomicInteger(1);

            public Thread newThread(Runnable r) {
                return new Thread(r, "SimpleAsyncTask #" + this.mCount.getAndIncrement());
            }
        };
        sMaxSizeLIFO = 128;
        EXECUTOR = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, 1L, TimeUnit.MILLISECONDS, new SimpleAsyncTask.LinkedBlockingStack(SimpleAsyncTask.Policy.FIFO));
        EXECUTOR_LIFO = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, 1L, TimeUnit.MILLISECONDS, new SimpleAsyncTask.LinkedBlockingStack(SimpleAsyncTask.Policy.LIFO));
        EXECUTOR_PRIORITY = new ThreadPoolExecutor(1, 1, 1L, TimeUnit.MILLISECONDS, new PriorityBlockingQueue<Runnable>() {
            public boolean offer(Runnable runnable) {
                return super.offer(new SimpleAsyncTask.CompareRunnableFIFOWrapper((CompareRunnable)runnable));
            }
        });
        sDefaultExecutor = EXECUTOR;
    }

    private static class AsyncTaskResult<Data> {
        final SimpleAsyncTask mTask;
        final Data[] mData;

        AsyncTaskResult(SimpleAsyncTask task, Data... data) {
            this.mTask = task;
            this.mData = data;
        }
    }

    private abstract static class WorkerRunnable<Params, Result> implements Callable<Result> {
        Params[] mParams;

        private WorkerRunnable() {
        }
    }

    private static class InternalHandler extends Handler {
        public InternalHandler() {
            super(Looper.getMainLooper());
        }

        public void handleMessage(Message msg) {
            SimpleAsyncTask.AsyncTaskResult<?> result = (SimpleAsyncTask.AsyncTaskResult)msg.obj;
            switch(msg.what) {
                case 1:
                    result.mTask.finish(result.mData[0]);
                    break;
                case 2:
                    result.mTask.onProgressUpdate(result.mData);
            }

        }
    }

    public static enum Status {
        PENDING,
        RUNNING,
        FINISHED;

        private Status() {
        }
    }

    private static class CompareRunnableFIFOWrapper implements CompareRunnable<SimpleAsyncTask.CompareRunnableFIFOWrapper> {
        private static AtomicLong mCount = new AtomicLong(0L);
        private final long mSecondPriority;
        private CompareRunnable mRunnable;

        private CompareRunnableFIFOWrapper(CompareRunnable runnable) {
            this.mRunnable = runnable;
            this.mSecondPriority = mCount.incrementAndGet();
        }

        public CompareRunnable getRunnable() {
            return this.mRunnable;
        }

        public long getSecondPriority() {
            return this.mSecondPriority;
        }

        public void run() {
            this.mRunnable.run();
        }

        public int compareTo(SimpleAsyncTask.CompareRunnableFIFOWrapper another) {
            int res = this.mRunnable.compareTo(another.getRunnable());
            if (res == 0) {
                return this.mSecondPriority < another.getSecondPriority() ? -1 : 1;
            } else {
                return res;
            }
        }
    }

    public static class LinkedBlockingStack<T> extends LinkedBlockingDeque<T> {
        private static SimpleAsyncTask.Policy sPolicy;

        private LinkedBlockingStack(SimpleAsyncTask.Policy policy) {
            sPolicy = policy;
        }

        public boolean offer(T e) {
            switch(sPolicy) {
                case LIFO:
                    this.offerFirst(e);
                    if (this.size() > SimpleAsyncTask.sMaxSizeLIFO) {
                        this.removeLast();
                    }

                    return true;
                default:
                    this.offerLast(e);
                    return true;
            }
        }

        static {
            sPolicy = SimpleAsyncTask.Policy.FIFO;
        }
    }

    public static enum Policy {
        FIFO,
        LIFO;

        private Policy() {
        }
    }
}
