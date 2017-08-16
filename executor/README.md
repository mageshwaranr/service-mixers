# Executor

Responsible for actual execution of the function. Responsible to do following

* Populate necessary inputs of the function by interacting with input sources
* Replace input template with actual values.
* Invoke the actual function with required inputs.

## Sources

Only REST sources are supported for now. Sources are usually template and those should be replaced before invocation. Using the original expectedAPI issued to invoke the function, the REST API template should be resolvable.
Please test the templates using the test utility provided to ensure that the REST APIs are completely resolvable else it may result in exception.

## Arguments

While invoking the function, Service Mixers preserves the order and no. of arguments of the function and its signature/expected type should match exactly.
