(ns expojure.util
  (:import (java.io PrintWriter StringWriter)
	    java.util.Date
	    java.text.SimpleDateFormat)
  (:gen-class))

(defn current-time []
  (let [df (SimpleDateFormat. "yyy-MM-dd'T'HH:mmZ")]
    (.toString (.format df (Date.)))))

(defn stack-trace-as-str [e]
  (let [sw (java.io.StringWriter.)
        pw (doto (java.io.PrintWriter. sw true)
             (#(.printStackTrace e %)) ; side-effect puts stack trace in pw
             (.flush))
        _ (.flush sw)]
    (.toString sw)))

(defn stack-trace-as-vec [e]
  (reduce #(cons (.toString %2) %1) [] (.getStackTrace e)))
