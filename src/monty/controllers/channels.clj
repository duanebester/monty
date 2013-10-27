(ns monty.controllers.channels
  (:require [clojure.data.json :refer [write-str]])
  (:use [org.httpkit.server]
  		[clojure.tools.logging :only (info error)]
  		[monty.templating.json.response :only [jsonRes]]))

(defprotocol ChannelStore
  (add-to-group [store group ch attach])
  (remove-from-group [store group ch])
  (broadcast [store group {}]))

;; Implementation using atom.
(deftype MemoryChannelStore [store-map]
  ChannelStore
  (add-to-group [_ group ch attach]
    #_(send! ch (pr-str {:message "Success"}))
    (swap! store-map (fn [old-store]
                       (assoc old-store
                         group
                         (assoc (or (old-store group) {})
                           ch attach)))))
  (remove-from-group [_ group ch]
    (swap! store-map (fn [old-store]
                       (let [m (update-in old-store [group] dissoc ch)]
                         (if (empty? (m group))
                           (dissoc m group)
                           m)))))
  (broadcast [store group dmap]
    (doseq [[ch attach] (@store-map group)]
      (if (websocket? ch)
        (do
        (info (class dmap))
        (send! ch (pr-str dmap) false)))))) ; close it

;; Example usage
#_(defonce channel-store (MemoryChannelStore. (atom {})))

;; Some logic in other place
#_(on-some-event
 (.broadcast channel-store a-group
            (fn [ch since_ts] ; since_ts is passed back.
              (some-data-return-to-clients since_ts))))