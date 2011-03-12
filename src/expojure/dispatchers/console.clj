(ns expojure.dispatchers.console
 (:use expojure.util)
 (:gen-class))

(defn print-dispatch [e]
    (println (expojure.util/stack-trace e)))

