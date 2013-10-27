(ns monty.templating.cljs.sockets
(:require [cljs.core.async :refer [chan <! >! put!]]
   [cljs.reader :as reader]
   [monty.templating.cljs.util :refer [log]]
   [dommy.utils :as utils]
   [dommy.core :as dommy])
(:use-macros
    [dommy.macros :only [node sel sel1]])
(:require-macros
   [cljs.core.async.macros :refer [go]]))

(def send (chan))
(def receive (chan))
(def alert-view (chan))

(def ws-url "ws://localhost:1337/async/sysinfo")
(def ws (new js/WebSocket ws-url))


(defn event-chan
  [c el type]
  (let [writer #(put! c %)]
    (dommy/listen! el type writer)
    {:chan c
     :unsubscribe #(dommy/unlisten! el type writer)}))

(defn make-sender []
  (event-chan send (sel1 :#websocket) :click)
  (go
   (while true
     (let [evt  (<! send)
           name "Duane"
           msg  "Message"]
       (when (= (.-type evt) "click")
         (log (str "Sent WebSocket Message to Server.") "\n")
         (.send ws {:msg msg :name name}))))))


(defn messages []
  (sel1 :#foo))

(defn add-message []
  (go
   (while true
     (let [msg            (<! receive)
           raw-data       (.-data msg)
           data           (reader/read-string raw-data)]
       (.alert js/window (str data))
       (dommy/append! (sel1 :#foo) [:li (str (:message data))])
       (set! (.-scrollTop (sel1 :#foo)) (.-scrollHeight (sel1 :#foo)))
       (dommy/set-html! (sel1 :#username) (str (:username data)))
       (dommy/set-html! (sel1 :#userhome) (str (:userhome data)))
       (dommy/set-html! (sel1 :#javaVersion) (str (:javaVersion data)))
       (dommy/set-html! (sel1 :#osName) (str (:osName data)))))))

(defn make-receiver []
  (set! (.-onmessage ws) (fn [msg] (put! receive msg)))
  (add-message))

(defn socket-init []
  (make-sender)
  (make-receiver)
  (log "Socket Init"))

(defn ^:export init []
  (set! (.-onload js/window) socket-init))
