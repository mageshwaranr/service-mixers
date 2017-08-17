# Gateway

An External interface to communicate with Service Mixer. Responsible to do following

* Acts as a reverse proxy for all the Function registration and look-up queries
* Validates external input and performs a OAUTH functionality based on API KEY
* Caches all the Manifest metadata and serves requests with minimal latency.
* Invokes executor and returns actual response of computation 

Configuration,

* All the configurations to be done in Configurations.yaml