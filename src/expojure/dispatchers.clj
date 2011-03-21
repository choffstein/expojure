(ns expojure.dispatchers
  (:gen-class))

(def dispatchers (ref []))

(defn add-dispatch-function [fn]
  (dosync (alter dispatchers conj fn)))