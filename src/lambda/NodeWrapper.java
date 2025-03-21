package info.kgeorgiy.ja.razinkov.lambda;

public interface  NodeWrapper <T> {
    NodeWrapper<T> getNext(NodeWrapper<T> currentChild);
    int getChildPos(NodeWrapper<T> currentChild);
    boolean isLeaf();
    int childCount();
    T getValue();
    NodeWrapper<T> getNthChild(int pos);
    long getSize();
}

/*
    ERROR: Test AdvancedLambdaTest.test51_nestedBinaryTreeSpliterator() failed: >>> AdvancedLambdaTest Advanced for info.kgeorgiy.ja.razinkov.lambda.Lambda / === test51_nestedBinaryTreeSpliterator() / .trySplit() / org.opentest4j.AssertionFailedError: [, , @, \$F, -].trySplit() ==> expected: <[, , @, \$F, -]> but was: <[, ]>

    info.kgeorgiy.java.advanced.base.ContextException: >>> AdvancedLambdaTest Advanced for info.kgeorgiy.ja.razinkov.lambda.Lambda / === test51_nestedBinaryTreeSpliterator() / .trySplit() / org.opentest4j.AssertionFailedError: [, , @, \$F, -].trySplit() ==> expected: <[, , @, \$F, -]> but was: <[, ]>
        at info.kgeorgiy.java.advanced.base/info.kgeorgiy.java.advanced.base.Context.checked(Context.java:79)
        at info.kgeorgiy.java.advanced.base/info.kgeorgiy.java.advanced.base.Context.context(Context.java:67)
        at info.kgeorgiy.java.advanced.base/info.kgeorgiy.java.advanced.base.Context.context(Context.java:50)
        at info.kgeorgiy.java.advanced.lambda/info.kgeorgiy.java.advanced.lambda.LambdaTest.lambda$testSpliterator$6(LambdaTest.java:61)
        at java.base/java.util.Spliterators$ArraySpliterator.forEachRemaining(Spliterators.java:1024)
        at java.base/java.util.stream.Streams$ConcatSpliterator.forEachRemaining(Streams.java:735)
        at java.base/java.util.stream.ReferencePipeline$Head.forEachOrdered(ReferencePipeline.java:817)
        at info.kgeorgiy.java.advanced.lambda/info.kgeorgiy.java.advanced.lambda.LambdaTest.testSpliterator(LambdaTest.java:61)
        at info.kgeorgiy.java.advanced.lambda/info.kgeorgiy.java.advanced.lambda.HardLambdaTest.test51_nestedBinaryTreeSpliterator(HardLambdaTest.java:22)
        at java.base/java.lang.reflect.Method.invoke(Method.java:580)
        at java.base/java.util.ArrayList.forEach(ArrayList.java:1597)
        at java.base/java.util.ArrayList.forEach(ArrayList.java:1597)
    Caused by: org.opentest4j.AssertionFailedError: [, , @, \$F, -].trySplit() ==> expected: <[, , @, \$F, -]> but was: <[, ]>
        at info.kgeorgiy.java.advanced.lambda/info.kgeorgiy.java.advanced.lambda.LambdaTest$TestSpliterator.lambda$checker$0(LambdaTest.java:122)
        at java.base/java.lang.Iterable.forEach(Iterable.java:75)
        at info.kgeorgiy.java.advanced.lambda/info.kgeorgiy.java.advanced.lambda.LambdaTest.lambda$testSpliterator$5(LambdaTest.java:61)
        at info.kgeorgiy.java.advanced.base/info.kgeorgiy.java.advanced.base.Context.lambda$context$0(Context.java:51)
        at info.kgeorgiy.java.advanced.base/info.kgeorgiy.java.advanced.base.Context.checked(Context.java:75)
        ... 11 more
    ERROR: Test AdvancedLambdaTest.test52_nestedSizedBinaryTreeSpliterator() failed: >>> AdvancedLambdaTest Advanced for info.kgeorgiy.ja.razinkov.lambda.Lambda / === test52_nestedSizedBinaryTreeSpliterator() / .trySplit() / org.opentest4j.AssertionFailedError: [/, M, `P, , v"K].trySplit() ==> expected: <[/, M, `P, , v"K]> but was: <[/, M]>

    info.kgeorgiy.java.advanced.base.ContextException: >>> AdvancedLambdaTest Advanced for info.kgeorgiy.ja.razinkov.lambda.Lambda / === test52_nestedSizedBinaryTreeSpliterator() / .trySplit() / org.opentest4j.AssertionFailedError: [/, M, `P, , v"K].trySplit() ==> expected: <[/, M, `P, , v"K]> but was: <[/, M]>
        at info.kgeorgiy.java.advanced.base/info.kgeorgiy.java.advanced.base.Context.checked(Context.java:79)
        at info.kgeorgiy.java.advanced.base/info.kgeorgiy.java.advanced.base.Context.context(Context.java:67)
        at info.kgeorgiy.java.advanced.base/info.kgeorgiy.java.advanced.base.Context.context(Context.java:50)
        at info.kgeorgiy.java.advanced.lambda/info.kgeorgiy.java.advanced.lambda.LambdaTest.lambda$testSpliterator$6(LambdaTest.java:61)
        at java.base/java.util.Spliterators$ArraySpliterator.forEachRemaining(Spliterators.java:1024)
        at java.base/java.util.stream.Streams$ConcatSpliterator.forEachRemaining(Streams.java:735)
        at java.base/java.util.stream.ReferencePipeline$Head.forEachOrdered(ReferencePipeline.java:817)
        at info.kgeorgiy.java.advanced.lambda/info.kgeorgiy.java.advanced.lambda.LambdaTest.testSpliterator(LambdaTest.java:61)
        at info.kgeorgiy.java.advanced.lambda/info.kgeorgiy.java.advanced.lambda.HardLambdaTest.test52_nestedSizedBinaryTreeSpliterator(HardLambdaTest.java:31)
        at java.base/java.lang.reflect.Method.invoke(Method.java:580)
        at java.base/java.util.ArrayList.forEach(ArrayList.java:1597)
        at java.base/java.util.ArrayList.forEach(ArrayList.java:1597)
    Caused by: org.opentest4j.AssertionFailedError: [/, M, `P, , v"K].trySplit() ==> expected: <[/, M, `P, , v"K]> but was: <[/, M]>
        at info.kgeorgiy.java.advanced.lambda/info.kgeorgiy.java.advanced.lambda.LambdaTest$TestSpliterator.lambda$checker$0(LambdaTest.java:122)
        at java.base/java.lang.Iterable.forEach(Iterable.java:75)
        at info.kgeorgiy.java.advanced.lambda/info.kgeorgiy.java.advanced.lambda.LambdaTest.lambda$testSpliterator$5(LambdaTest.java:61)
        at info.kgeorgiy.java.advanced.base/info.kgeorgiy.java.advanced.base.Context.lambda$context$0(Context.java:51)
        at info.kgeorgiy.java.advanced.base/info.kgeorgiy.java.advanced.base.Context.checked(Context.java:75)
        ... 11 more
ERROR: Tests: failed
 */