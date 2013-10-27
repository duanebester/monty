(ns monty.templating.cljs.script
(:require [ajax.core :refer [GET POST]]
      [dommy.utils :as utils]
      [dommy.core :as dommy]
      [monty.templating.cljs.util :refer [log]]
      [cljs.core.async :refer [chan <! >! put!]]
      [cljs.reader :as reader])
(:use-macros
    [dommy.macros :only [node sel sel1]])
(:require-macros
   [cljs.core.async.macros :refer [go]]))

(defn toggle-atr!
  [elem k]
    (if (boolean (dommy/attr elem k))
     (dommy/set-attr! elem k)
     (dommy/remove-attr! elem k)))

(defn receive [event]
  (let [resp (js->clj event)]
    (log "ping")
    (dommy/append! (sel1 :#foo) [:li (get-in resp ["test"])])
    (set! (.-scrollTop (sel1 :#foo)) (.-scrollHeight (sel1 :#foo)))))

(defn error-handler [event]
  (log (str "Something went wrong: " event)))
 
(defn ping-server [e]
 (GET "/api" {:handler receive :error-handler error-handler})
 (.preventDefault e))

(defn start-server [e]
 (GET "/start" {:handler receive :error-handler error-handler})
 (.preventDefault e))

(defn stop-server [e]
 (GET "/stop" {:handler receive :error-handler error-handler})
 (.preventDefault e))

(defn myalert [e]
  (.alert js/window (str "Hello!"))
  (log "something happened")
  (.preventDefault e))

;; it marks selected list item as selected
(defn toggle-start [ev]
  (if (boolean (dommy/attr (.-currentTarget ev) :checked))
    (do
      (dommy/remove-attr! (.-currentTarget ev) :checked)
      (GET "/stop" {:handler receive :error-handler error-handler}))
    (do
      (dommy/set-attr! (.-currentTarget ev) :checked)
      (GET "/start" {:handler receive :error-handler error-handler}))))
 
(defn ^:export init []
  (dommy/listen! (sel1 :#start) :click toggle-start)
  #_(set! (.-onclick (sel1 :#stop)) stop-server)
  (set! (.-onclick (sel1 :#ping)) ping-server))

