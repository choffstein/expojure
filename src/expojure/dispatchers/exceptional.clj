(ns expojure.dispatchers.exceptional
  (:use clojure.contrib.json
	expojure.util)
  (:require [clj-http.client :as client])
  (:gen-class))

(def *api-key* "06dc5bf6733efb97e2ee55c3edc05fcc6a3dbcc3")

(defn to-json [e]
  (let [occurred-at (current-time)
	message (. e getMessage)
	class (.. e getClass toString)
	stack-trace [(stack-trace e)]]
    (json-str {"application_environment" {"application_root_directory" ""
				"env" {}}
     "exception" {"occurred_at" occurred-at
		  "message" message
		  "backtrace" stack-trace
		  "exception_class" class}})))

(defn exceptional-dispatch [e]
  (println (client/post (str "http://api.getexceptional.com/api/errors?api_key=" *api-key* "&protocol_version=6") {:body (to-json e) :content-type :json})))