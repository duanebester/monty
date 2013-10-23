(ns monty.controllers.app
  (:require [monty.database.redis :as redis]
            [clojurewerkz.quartzite.jobs :refer [defjob]]
            [monty.templating.html.layout :as layout]
            [clojure.edn :as edn]
            [monty.controllers.channels])
  (:import (monty.controllers.channels MemoryChannelStore))
  (:use [org.httpkit.server]
        [monty.templating.json.response :only [jsonRes]]
        [clojure.tools.logging :only (info error)]
        [hiccup.core]
        [clojure.pprint :only (pprint)]
        [monty.controllers.service :only (start-service stop-service get-sysinfo-map)]))


(defonce channel-store (MemoryChannelStore. (atom {})))

(defn broadcastWS
  [msg]
  (.broadcast channel-store "sysinfo"
            (fn [ch since_ts] ; dont do anything, this fn just returns msg
              {:data "message"})))

(defn- now [] (quot (System/currentTimeMillis) 1000))

(defn ^{:doc "Handles WebSocket Channels"}
  async-handler [req]
  (let [group (-> req :params :group)
        since_ts (-> req :params :since_ts)] ;; state variable, from client
    (with-channel req ch
      (.add-to-group channel-store group ch since_ts)
      (on-close ch (fn [status]
                     (.remove-from-group channel-store group ch))))))

;; Testing, sends dummy data back
(defn poll-mesg [req]
  (info "/api")
  (jsonRes {:test "value"}))

;; We start our get-sysinfo service

(defjob sendSysInfo
  [ctx]
  (broadcastWS get-sysinfo-map))
;; jk = "jobs.sysinfo.1"
;; tk = "triggers.1"
(defn start [req]
  (info "/start")
  (start-service "jobs.sysinfo.1" "triggers.1" sendSysInfo)
  (jsonRes {:message "success"}))

;; We stop our get-sysinfo service
(defn stop [req]
  (info "/stop")
  (stop-service "triggers.1")
  (jsonRes {:message "success"}))



(defn ^{:doc "Monty Home Page"}
  home [req]
  (info "/")
  (layout/base [:ul {:id "foo" :class "bar"}
                    (for [x (range 1 4)]
                    [:li x])]
                [:button {:id "ping"} "Ping"]
                [:button {:id "websocket"} "Socket"]))

(defn ^{:doc "Monty Settings Page"}
  settings 
  [req]
  (info "/settings")
  (layout/settings [:ul {:id "foo" :class "bar"}
                    (for [x (range 1 4)]
                    [:li x])]
                [:input {:id "terminal" :type "text" :class "topcoat-text-input--large"}]
                [:button {:id "start" :class "topcoat-button--large--cta"} "Start"]
                [:button {:id "websocket":class "topcoat-button--large--cta"} "Socket"]))


#_(defn update-userinfo [req]          ;; ordinary clojure function
  (let [user-id (-> req :params :id)    ; param from uri
        password (-> req :params :password)] ; form param
    ....
    ))
