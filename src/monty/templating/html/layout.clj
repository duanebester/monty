(ns monty.templating.html.layout
  (:use [hiccup.page :only [include-css html5]]
  			[compojure.core]
  			[monty.templating.css.main :as style])
  (:require [garden.core :refer [css]]
            [hiccup.page :refer [html5]]))

(defn base-cloj [& content]
  (html5
    [:head
     [:title "Cloj"]
     [:style (css style/fixed)]
     ]
    [:body
      [:div.center
       [:section#top [:h1 "Cloj"]]
       [:section#main [:h2 "Server Ping"] content]
       #_(sidebar [:h2 "Sidebar"])]
      [:script {:type "text/javascript" :src "/js/main.js"}]
      [:script {:type "text/javascript"} "monty.templating.cljs.script.init();"]
      [:script {:type "text/javascript"} "monty.templating.cljs.sockets.init();"]]))

(defn base [& content]
  (html5
    [:head
     [:title "Monty"]
      (include-css "/css/topcoat-desktop-dark.min.css")
     ]
    [:body {:class "dark"}
      [:div.center
       [:section#top [:h1 "Monty"]]
       [:section#main [:h2 "Server Ping"] content]
       #_(sidebar [:h2 "Sidebar"])]
      [:script {:type "text/javascript" :src "/js/main.js"}]
      [:script {:type "text/javascript"} "monty.templating.cljs.script.init();"]
      [:script {:type "text/javascript"} "monty.templating.cljs.sockets.init();"]]))
