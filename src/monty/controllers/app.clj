(ns monty.controllers.app
  (:require [sigmund.core :as sig]
            [monty.database.redis :as redis]
            [clojurewerkz.quartzite.jobs :refer [defjob]]
            [monty.templating.html.layout :as layout]
            [monty.controllers.channels]
            [clojure.data.json :refer [write-str]])
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
  (.broadcast channel-store "sysinfo" msg))

(defn- now [] (quot (System/currentTimeMillis) 1000))

(defn ^{:doc "Handles WebSocket Channels"}
  async-handler [req]
  (info "/async/sysinfo")
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


#_(into {} 
  (for [[k v] my-map] 
    [(keyword k) v]))
;; We start our get-sysinfo service

(defjob sendSysInfo
  [ctx]
  (broadcastWS (sig/cpu-usage)))

#_(defjob sendSysInfo
  [ctx]
  (broadcastWS (into {} (for [[k v] get-sysinfo-map]
                          [(keyword k) (str v)]))))
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
  (layout/base [:div {:id "props"}
                  [:span {:id "username"}]
                  [:span {:id "userhome"}]
                  [:span {:id "javaVersion"}]
                  [:span {:id "osName"}]]
                [:ul {:id "foo" :class "bar"}
                    (for [x (range 1 4)]
                    [:li x])]
                [:button {:id "ping" :class "topcoat-button--large--cta"} "Ping"]
                (comment [:button {:id "websocket" :class "topcoat-button--large--cta"} "Socket"])
                [:label {:class "topcoat-switch"}
                  [:input {:type "checkbox" :class "topcoat-switch__input" :id "start"}]
                  [:div {:class "topcoat-switch__toggle"}]]))

#_(defn ^{:doc "Monty Settings Page"}
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
