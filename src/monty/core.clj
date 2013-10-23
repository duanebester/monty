(ns monty.core
  (:require [monty.http.server :as ms])
  (:gen-class))


(defn -main
  "Start Monty Server"
  [& args]
  (ms/start-server 1337))