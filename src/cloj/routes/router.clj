(ns cloj.routes.router
  (:require [cloj.database.redis :as redis]
            [cloj.html.layout :as layout]
            [cloj.system.sysinfo :as sysinfo])
  (:use [compojure.route :only [files resources not-found]]
        [compojure.handler :only [site]] ; form query params decode; cookie; session, etc
        [compojure.core :only [defroutes GET POST DELETE ANY context]]
        [org.httpkit.server]
        [cloj.json.response :only [jsonRes]]
        [clojure.tools.logging :only (info error)]
        [hiccup.core]
        [clojure.pprint :only (pprint)]))

(defn- now [] (quot (System/currentTimeMillis) 1000))

(def channels (atom []))


(defn poll-mesg [req]
  (info "/api")
  (jsonRes {:test "value"}))

(defn sys-info [req]
  (info "/sysinfo")
  (pprint (sysinfo/get-sysinfo-map)))

(defn show-landing-page [req]
  (info "/")
  #_(redis/set "foo" "bar")
  #_(println (redis/get "foo"))
  (layout/base [:ul {:id "foo" :class "bar"}
                    (for [x (range 1 4)]
                    [:li x])]
                [:input {:id "terminal" :type "text"}]
                [:button {:id "ping"} "Ping"]
                [:button {:id "websocket"} "Socket"])
)

#_(defn update-userinfo [req]          ;; ordinary clojure function
  (let [user-id (-> req :params :id)    ; param from uri
        password (-> req :params :password)] ; form param
    ....
    ))

(defn chat-handler [req]
  (with-channel req channel              ; get the channel
    ;; communicate with client using method defined above
    (on-close channel (fn [status]
                        (println "channel closed")))
    (if (websocket? channel)
      (println "WebSocket channel")
      (println "HTTP channel"))
    (on-receive channel (fn [data]       ; data received from client
           ;; An optional param can pass to send!: close-after-send?
           ;; When unspecified, `close-after-send?` defaults to true for HTTP channels
           ;; and false for WebSocket.  (send! channel data close-after-send?)
                          (send! channel data))))) ; data is sent directly to the client

(defn async-handler [request]
  (with-channel request channel
    (info "/async")
    (on-close channel (fn [status] (println "channel closed: " status)))
    (on-receive channel (fn [data] ;; echo it back
                          (send! channel data)
                          (info "Sent WebSocket Data.")))))

(defroutes all-routes
  (GET "/" [] show-landing-page)
  (GET "/ws" [] chat-handler)     ;; websocket
  (GET "/api" [] poll-mesg)     ;; websocket
  (GET "/sysinfo" [] sys-info)
  (GET "/async" [] async-handler) ;; asynchronous(long polling)
  ;;(context "/user/:id" []
  ;;         (GET / [] get-user-by-id)
  ;;         (POST / [] update-userinfo))
  (resources "/js/" {:root "js"}) ;; static file url prefix /static, in `public` folder
  #_(route/not-found "<p>Page not found.</p>")) ;; all other, return 404
