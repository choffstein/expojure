(ns expojure.dispatchers.exceptional
  (:use clojure.contrib.json
	expojure.util)
  (:require [clj-http.client :as client]
	    [ring.middleware.gzip :as gzip])
  (:gen-class))

(def *api-key* "06dc5bf6733efb97e2ee55c3edc05fcc6a3dbcc3")

(defn body-to-string [resp]
  (let [body-stream (:body resp)
	bytes-available (. body-stream available)
	bytes (byte-array bytes-available)
	_ (. body-stream read bytes 0 bytes-available)]
    (assoc resp :body bytes)))

(defn to-json [e]
  (let [occurred-at (current-time)
	message (. e getMessage)
	class (.. e getClass toString)
	stack-trace (stack-trace-as-vec e)
	body {"application_environment" {"application_root_directory" ""
					 "env" {}}
	      "exception" {"occurred_at" occurred-at
			   "message" message
			   "backtrace" stack-trace
			   "exception_class" class}
	      "client" {"name" "expojure"
			"version" "1.0.0-SNAPSHOT"
			"protocol_version" 6}}]
    (json-str body)))

(defn exceptional-dispatch [e]
  (let [req (body-to-string (gzip/gzipped-response
			     {:body (to-json e)
			      :headers {"Content-Type" "application/json"
					"Accept" "application/json"
					"User-Agent" "expojure/1.0.0-SNAPSHOT"}}))]
    (client/post
     (str "http://api.getexceptional.com/api/errors?api_key=" *api-key* "&protocol_version=6")
     req)))