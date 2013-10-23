(ns monty.http.server
  (:require [monty.routes.router :as routes])
  (:gen-class)
  (:use     [compojure.handler :only [site]] 
            [org.httpkit.server]))

(defn start-server [port]
	(run-server (site #'routes/all-routes) {:port 1337})
	(println "                          ")
	(println " ,-.-.          |         ")
	(println " | | |,---.,---.|--- ,   .")
	(println " | | ||   ||   ||    |   |")
	(println " ` ' '`---'`   '`---'`---|")
	(println "   @localhost:1337   `---'")
	(println "                          "))