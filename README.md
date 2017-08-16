# service-mixers
Service Mixers

Service Mixers enable users to build new functional services by just providing business logic in form of functions. Users should focus only on writing business logic leaving rest of the plumbing work like interactions with other services to fetch input, expose stateless functions as a REST API, passing other services output to function to the framework. This also in-directly supports scaling by leveraging container orchestration frameworks like kubernetes.
Frameworks like Istio takes care of most of the infrastructural aspects required for Service Mush. Service Mixers focuses only on application space and leaves the infrastructural aspects to desired architecture.

## Terminology

Terminology used in service Mixers are detailed below.

### App

App is a logical group of functions and input sources. App creation is mandatory before creating a function / source.

### Function

Function is a stateless business logic exposed as a Service / API by Service Mixers. Such function can depend on registered input sources. When an exposed API is invoked, the corresponding registered function will be invoked with registered inputs.
Input's could be obtained either from input sources or directly via API call. At the moment, Service Mixers only support HTTP API for input sources.

A function is identified by

* Function Definition containing the meta-data about function, input sources and other runtime behaviour related information
* Executable containing the actual business logic. At the moment, java fat/uber jar and java class files depending only on JDK are supported. More will be added later

Function should be stateless and only depend on its input for the context and state information. similarly it can't store its state with-in itself. It has to depend on other services (any http services) for its state

### Sources

Source defines a data source with in the system. At the moment, only supported source is a HTTP (REST?) API. Multiple functions belonging to an APP can leverage the same source belonging to the same APP.

## Example


## Try out !!!

