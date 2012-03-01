(defproject expojure "1.0.0-SNAPSHOT"
  :description "Catch and store all exceptions"
  :dependencies [[org.clojure/clojure "1.3.0-beta1"]]

  :plugins [[s3-wagon-private "1.1.1"]]

  :repositories {"nfr-releases" "s3p://newfound-mvn-repo/releases/"
                 "nfr-snapshots" "s3p://newfound-mvn-repo/snapshots/"})
