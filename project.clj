(defproject expojure "1.0.0-SNAPSHOT"
  :description "Catch and store all exceptions"
  :dependencies [[org.clojure/clojure "1.2.0"]
                 [org.clojure/clojure-contrib "1.2.0"]
		 [clj-http "0.1.3"]
		 [org.clojars.mikejs/ring-gzip-middleware "0.1.0-SNAPSHOT"]]
  :dev-dependencies [[swank-clojure "1.3.0-SNAPSHOT"]]
  :main expojure.core)
