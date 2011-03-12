(ns expojure.util
  (:import (java.io PrintWriter StringWriter)
	    java.util.Date
	    java.text.SimpleDateFormat)
  (:gen-class))

(defn current-time []
  (let [df (new SimpleDateFormat "yyy-MM-dd'T'HH:mmZ")]
    (. (. df format (new Date)) toString)))

(defn stack-trace [e]
   (let [sw (new java.io.StringWriter)
	pw (new java.io.PrintWriter sw true)
	_ (. e printStackTrace pw) ; side-effect puts stack trace in pw
	_ (. pw flush)
	_ (. sw flush)]
     (. sw toString)))
