Demo project to demonstrate how to structure your code to make it easy to test in a whitebox(component-test) manner.

Setup is quite simple, you have an application module(folder) that we dockerize and then in the component-test module
we boot it up through the Testcontainers library. We use the Testcontainers library to make it easy for us to handle the
lifecycles of our containers. Each component-test will boot up a container so tests can't taint the data for other tests.

###Tip for local
Of course, booting up a Spring application for each test does take some time so to help in your local environment we have
added a toggle for running the application containers on the side. That is, **you** are responsible for booting and killing
the application