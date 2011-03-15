# Expojure

Expojure helps you track errors in your Clojure applications via customized dispatch methods

## Installation

1. Install
2. ??
3. Profit!

## Dispatch
The currently written dispatch methods are for

* Console Printing
* GetExceptional.com

## Roll Your Own
To add your own dispatch extensions to the library, simply construct a function that takes an Exception class, and handle it!  You could write it to your logs, a database, or send it by carrier pigeon!

## Usage
There are two ways of using this library.  The first is the local-try macro, which is just like a normal try block, but the first parameter is a list of dispatch methods.  e.g.

<(expojure.core.local-try [expojure.dispatchers.console/print-dispatch] 
  (/ 1 0)
  (catch ArithmeticException e (println "Whoops, exception!")))>

Global try is a bit different.  It allows you to set up a set of dispatch functions that get called anytime global-try is used.  So instead of having to put your console, file, and server logger into local-try every time, you can just add them via add-dispatch-function, and use global try.
<(expojure.dispatchers/add-dispatch-function [expojure.dispatchers.exceptional/exceptional-dispatch])
(expojure.core.global-try (/ 1 0)
  (catch ArithmeticException e (println "Whoops, exception!")))>


Copyright © 2011 Corey M. Hoffstein