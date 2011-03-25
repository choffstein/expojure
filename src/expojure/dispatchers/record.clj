(ns expojure.dispatchers.record
  (:use expojure.util)
  (:require [clojure.contrib.string :as string]
	    [clojure.contrib.duck-streams :as ds]
	    [clojure.contrib.json :as json])
  (:gen-class))

(def output-directory (ref "."))
(defn set-output-director [dir]
  (dosync (ref-set output-directory dir)))

(defn record-dispatch [e]
  (let [occurred-at (current-time)
	message (.getMessage e )
	class (.toString (.getClass e))
	stack-trace (stack-trace-as-vec e)
	as-str (json/json-str {"exception" {"occurred_at" occurred-at
			   "message" message
			   "backtrace" stack-trace
			   "exception_class" class}})
	file-name (string/replace-char \ \- (str class "-" occurred-at ".exception"))]
    (ds/spit (str @output-directory "/" file-name) as-str)))
    

