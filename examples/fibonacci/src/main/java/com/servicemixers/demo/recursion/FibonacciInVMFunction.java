package com.servicemixers.demo.recursion;

/**
 * Used in FunctionLoaderTest case of executor.
 */
public class FibonacciInVMFunction {

    public static int val = 3;

    public int fibonacci(int count) {
        if (count == 0) {
            return 0;
        } else if (count == 1) {
            return 1;
        } else {
            return fibonacci(count - 1) + fibonacci(count - 2);
        }
    }

}
