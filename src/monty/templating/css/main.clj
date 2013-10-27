(ns monty.templating.css.main
  (:use compojure.core)
  (:refer-clojure :exclude [+ - * /])
  (:require [garden.units :as gu :refer [px em]]
            [garden.color :as gc :refer [hsl rgb]]
            [garden.def :refer [defrule]]
            [garden.core :refer [css]]
            [hiccup.page :refer [html5]]))
 
(def ^{:doc "The famous \"micro\" clearfix."}
  clearfix
  ["&"
   {:*zoom 1}
   ["&:before" "&:after"
    {:content "\"\""
     :display "table"}]
   ["&:after"
    {:clear "both"}]])
 
(def styles list)
 
;; Define a few rule functions to make the code more expressive and
;; readable.
 
(defrule center :div.center)
(defrule top :section#top)
(defrule main :section#main)
(defrule sidebar :section#sidebar)
(defrule headings :h1 :h2 :h3)
 
;; I like this approach as well.
(def center-text {:text-align "center"})
 
;; Define our "fixed" grid CSS.
(def fixed
  ;; Create a standard grid and bind the key values.

    (styles
     ["*" "*:after" "*:before"
      {:box-sizing "border-box"}]

     [:button {
      :margin-left (px 5)
      }]
 
     [:body
      {:width (gu/percent 100)
       :background (rgb [253 254 255])
       :padding 0
       :margin 0
       :font-weight 200}
      clearfix]

     [:#foo
      {:min-height (px 500)
       :max-height (px 500)
       :overflow-y "auto"}]
 
     (headings
      {:font-weight 200})
 
     (center
      {:width (gu/percent 100)
       :margin [0 "auto"]
       :overflow "hidden"})
 
     (top
      center-text
      {:width (gu/percent 100)
       :margin (em 0)
       :padding (px 5)
       :background (rgb [240 241 241])
       :color (rgb [40 142 223])})
 
     (main
      {:margin [(px 20) "auto"]
       :float "none"
       :padding (px 40)
       :text-align "left"})
 
     (sidebar
      center-text
      {:float "left"
       :padding (px 20)})))