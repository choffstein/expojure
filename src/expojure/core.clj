(ns expojure.core
  (:use clojure.contrib.logging)
  (:gen-class))

(def *dispatchers* (ref []))
(defn add-dispatch-function [fn]
  (dosync (alter *dispatchers* conj fn)))

; Huge thanks to amalloy and brehaut in #clojure (irc.freenode.net)
(defmacro v-try [execute-clause & arglist]
  (let [catch-clauses (filter (comp #{'catch} first) arglist)
	new-catch-clauses (map
			   #(let [[_ e-class e-name & e-ret ] %
				  dispatched-methods (map (fn [d] `(~d ~e-name))
							  @*dispatchers*)]
			       `(catch ~e-class ~e-name (do ~@dispatched-methods ~@e-ret)))
			    catch-clauses)
	finally-clause (filter (comp #{'finally} first) arglist)]
  `(try ~execute-clause ~@new-catch-clauses ~@finally-clause)))

(defn log-exception [e]
  (error (. e toString)))

(defn print-exception [e]
  (println (. e toString)))

(add-dispatch-function print-exception)
(add-dispatch-function log-exception)

(defn -main [& args]
  (v-try (/ 1 0)
	      (catch java.lang.ArithmeticException e (println (+ 3 4)))
	      (catch Exception e (println (+ 1 2)))
	      (finally (println "Finally block"))))