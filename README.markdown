# Expojure

Expojure helps you track errors in your Clojure applications using customized handler methods

## Installation

1. Install
2. ??
3. Profit!

## Usage
	
		(expojure.core/try 	[exception-handler] 
							(/ 1 0)
							(catch Exception e (...optional block here)
							(finally (...optional block here))))

## Handlers
To add your own dispatch extensions to the library, simply construct a function that takes an Exception class, and handle it!  You could write it to your logs, a database, or send it by carrier pigeon!

## Example handlers
A handler that prints the exception to stderr

		(defn print-handler [e]
  			(binding [*out* *err*]
    		(println (expojure.util/stack-trace-as-str e))))

		(expojure.core/try [print-handler] 
	      			   (/ 1 0))

		
A handler that writes the exception to disk in json format

		(defn file-handler [directory e]
			  (let [occurred-at (expojure.util/current-time)
				message (.getMessage e )
				class (.toString (.getClass e))
				stack-trace (stack-trace-as-vec e)
				as-str (json/json-str {"exception" {"occurred_at" occurred-at
						   "message" message
						   "backtrace" stack-trace
						   "exception_class" class}})
				file-name (string/replace-char \ \- (str class "-" occurred-at ".exception"))]
			    (ds/spit (str @output-directory "/" file-name) as-str)))

		(expojure.core/try [(partial file-handler "exceptions")] 
			      			(/ 1 0))

A handler that writes exceptions to the getexceptional.com API

		(require '[clj-http.client :as client])
		(require '[ring.middleware.gzip :as gzip])
		(require '[clojure.contrib.json :as json])
		
		(def api-key "")

		(defn- body-to-bytes [resp]
		  (let [body-stream (:body resp)
			bytes-available (.available body-stream)
			bytes (byte-array bytes-available)
			_ (.read bytes body-stream 0 bytes-available)]
		    (assoc resp :body bytes)))

		(defn- to-json [e]
		  (let [occurred-at (current-time)
			message (.getMessage e )
			class (.toString (.getClass e))
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

		(defn exceptional-handler [e]
		  (let [req (body-to-bytes (gzip/gzipped-response
					     {:body (to-json e)
					      :headers {"Content-Type" "application/json"
							"Accept" "application/json"
							"User-Agent" "expojure/1.0.0-SNAPSHOT"}}))]
		    (client/post
		     (str "http://api.getexceptional.com/api/errors?api_key=" api-key "&protocol_version=6")
		     req)))

		(expojure.core/try [exceptional-handler] 
				      		(/ 1 0))

A handler that writes exceptions to Amazon's S3 service

		(require '[cemerick.rummage :as sdb])
		(require '[cemerick.rummage.encoding :as enc])
		(require '[clojure.contrib.json :as json])
		(import 'java.util.UUID)
		
		(def aws-key "")
		(def aws-secret-key "")
		(def db-name "")
		(defn- uuid [] (.toString (UUID/randomUUID)))
		
		(defn- store-message [message]
		  (let [client (sdb/create-client aws-key aws-secret-key)
			    aws-config (assoc enc/keyword-strings :client client)
			    host-name (.. java.net.InetAddress getLocalHost getHostName)]
		    (do
		      (sdb/create-domain aws-config db-name) ;a bit of a silly hack to reload the db
		      (sdb/put-attrs aws-config db-name {::sdb/id (uuid)
								 :time-stamp (util/current-time)
								 :message message}))))
								
		(defn aws-s3-handler [e]
		  (let [occurred-at (expojure.util/current-time)
				message (.getMessage e )
				class (.toString (.getClass e))
				stack-trace (expojure.util/stack-trace-as-vec e)
				; note that there is a maximum length of 1024 characters per value
				; so we only take the top 5 stack elements
				message (json/json-str {:exception {:message message
													:class class
													 :stack-trace (take 5 stack-trace)}})]
		    (store-message message)))
		
		(expojure.core/try [aws-s3-handler] 
					      	(/ 1 0))
		
Copyright © 2011 Corey M. Hoffstein


	