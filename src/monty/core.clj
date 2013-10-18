(ns monty.core
  (:require [monty.http.server :as cs])
  (:gen-class))


(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (cs/start-server 1337))