(ns monty.routes.router
  (:require [monty.controllers.app :as app])
  (:use     [compojure.route :only [files resources not-found]]
            [compojure.handler :only [site]]
            [compojure.core :only [defroutes GET POST DELETE ANY context]]
            [org.httpkit.server]))


(defroutes all-routes
  (GET "/" [] app/home)
  #_(GET "/ws" [] app/chat-handler)     ;; websocket
  (GET "/api" [] app/poll-mesg)       ;; websocket
  (GET "/async/:group" [] app/async-handler) ;; asynchronous(long polling)
  #_(GET "/settings" [] app/settings)
  (GET "/start" [] app/start)
  (GET "/stop" [] app/stop)
  #_(context "/async" []
           (GET / [] app/async-handler))
  ;;(context "/user/:id" []
   ;;        (GET / [] get-user-by-id)
   ;;        (POST / [] update-userinfo))
  #_(resources "/js/" {:root "js"})
  (resources "/") ;; static file url prefix /static, in `public` folder
  (not-found "<p>Page not found. Return <a href=\"/\">Home</a></p>")) ;; all other, return 404
