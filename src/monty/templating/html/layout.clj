(ns monty.templating.html.layout
  (:use [hiccup.page :only [include-css html5]]
        [compojure.core]
        [monty.templating.css.main :as style])
  (:require [garden.core :refer [css]]
            [hiccup.page :refer [html5]]))

(defn base [& content]
  (html5
    [:head
     [:title "Monty"]
     [:style (css style/fixed)]
     (include-css "/css/foundation.min.css")
     (include-css "/css/topcoat-desktop-dark.min.css")]
    [:body {:class "dark"}
    [:nav {:class "top-bar"}
    [:ul {:class "title-area"}
      [:li {:class "name"}
      [:h1 [:a {:href "/"} "Monty"]]]]]
      [:div.center
       [:section#main [:h2 "Server Ping"] content]
       #_(sidebar [:h2 "Sidebar"])]
      [:script {:type "text/javascript" :src "/js/main.js"}]
      [:script {:type "text/javascript"} "monty.templating.cljs.script.init();"]
      [:script {:type "text/javascript"} "monty.templating.cljs.sockets.init();"]]))