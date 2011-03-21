(ns expojure.util
  (:import (java.io PrintWriter StringWriter)
	    java.util.Date
	    java.text.SimpleDateFormat)
  (:gen-class))

(defn current-time []
  (let [df (new SimpleDateFormat "yyy-MM-dd'T'HH:mmZ")]
    (.toString (.format df (new Date)))))

(defn stack-trace-as-str [e]
   (let [sw (new java.io.StringWriter)
	pw (new java.io.PrintWriter sw true)
	_ (.printStackTrace e pw) ; side-effect puts stack trace in pw
	_ (.flush pw)
	_ (.flush sw )]
     (.toString sw)))

(defn stack-trace-as-vec [e]
  (reduce #(cons (.toString %2) %1) [] (.getStackTrace e)))