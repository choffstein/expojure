(ns expojure.dispatchers.console
 (:use expojure.util)
 (:gen-class))

(defn print-dispatch [e]
  (binding [*out* *err*]
    (println (expojure.util/stack-trace-as-str e))))

