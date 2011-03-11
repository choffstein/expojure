(ns.dispatchers.console
 (:gen-class))

(defn print-dispatch [e]
  (println (. e toString)))

