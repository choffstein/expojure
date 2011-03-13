(ns expojure.core
  (:use expojure.dispatchers)
  (:gen-class))

; Huge thanks to amalloy and brehaut in #clojure (irc.freenode.net) for helping me write
; these macros

(defmacro local-try [dispatchers execute-clause & arglist]
  (let [catch-clauses (filter (comp #{'catch} first) arglist)
	new-catch-clauses (map
			   #(let [[_ e-class e-name & e-ret ] %
				  dispatched-methods (map (fn [d] `(~d ~e-name))
							  dispatchers)]
			       `(catch ~e-class ~e-name (do ~@dispatched-methods ~@e-ret)))
			    catch-clauses)
	finally-clause (filter (comp #{'finally} first) arglist)]
    `(try ~execute-clause ~@new-catch-clauses ~@finally-clause)))

(defmacro global-try [execute-clause & arglist]
  (let [catch-clauses (filter (comp #{'catch} first) arglist)
	new-catch-clauses (map
			   #(let [[_ e-class e-name & e-ret ] %
				  dispatched-methods (map (fn [d] `(~d ~e-name))
							  @*dispatchers*)]
			       `(catch ~e-class ~e-name (do ~@dispatched-methods ~@e-ret)))
			    catch-clauses)
	finally-clause (filter (comp #{'finally} first) arglist)]
    `(try ~execute-clause ~@new-catch-clauses ~@finally-clause)))