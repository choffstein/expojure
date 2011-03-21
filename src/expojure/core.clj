(ns expojure.core
  (:require [expojure.dispatchers :as dispatch])
  (:gen-class))

; Huge thanks to amalloy and brehaut in #clojure (irc.freenode.net) for helping me write
; these macros

(defmacro local-try [dispatchers execute-clause & arglist]
  (let [given-catch-clauses (filter (comp #{'catch} first) arglist)
	catch-clauses (if (empty? given-catch-clauses)
			 (cons '(catch Exception e ()) given-catch-clauses)
			 given-catch-clauses)
	new-catch-clauses (map
			   #(let [[_ e-class e-name & e-ret ] %
				  dispatched-methods (map (fn [d] `(~d ~e-name))
							  dispatchers)]
			       `(catch ~e-class ~e-name (do ~@dispatched-methods ~@e-ret)))
			    catch-clauses)
	finally-clause (filter (comp #{'finally} first) arglist)]
    `(try ~execute-clause ~@new-catch-clauses ~@finally-clause)))

(defmacro global-try [& arglist]
  (let [derefed @dispatch/dispatchers]
    `(local-try ~derefed ~@arglist)))
