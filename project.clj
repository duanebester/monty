(defproject monty "0.1.0-SNAPSHOT"
  :description "Monitor and report system activity"
  :url "http://duanebester.com"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [com.taoensso/carmine "2.2.0"]             ;; Redis
                 [http-kit "2.1.12"]                        ;; Super Fast Http
                 [compojure "1.1.5"]                        ;; Compojure for Routing
                 [hiccup "1.0.4"]                           ;; Html from Clojure
                 [garden "1.1.3"]                     ;; CSS from Clojure
                 [org.clojure/core.match "0.2.0-rc5"]
                 [org.clojure/core.logic "0.8.4"]
                 [org.clojure/core.async "0.1.242.0-44b1e3-alpha"]
                 [org.clojure/data.json "0.2.3"]
                 [org.clojure/tools.logging "0.2.6"]        ;; Logging
                 [clj-ssh "0.5.6"]                          ;; SSH
                 ;;[clj-logging-config "1.9.10"]              ;; Use this to set up logging formatting
                 [cljs-ajax "0.2.1"]
                 [prismatic/dommy "0.1.1"]
                 [org.clojure/clojurescript "0.0-1934"]
                 [org.slf4j/slf4j-log4j12 "1.7.5"]
                 [clojurewerkz/quartzite "1.1.0"]
                 [sigmund "0.1.1"]
        				 ]

  ;; Plugin to compile ClojureScript to JavaScript
  :plugins [[lein-cljsbuild "0.3.4"]]
  
  ;; Three builds - dev, production, pre-production
  :cljsbuild {
              :builds [{:source-paths ["src/monty/templating/cljs"]
              :compiler {:output-to "resources/public/js/main.js"
                         :optimizations :whitespace
                         :pretty-print true}}]
              ; :builds
              ; {:dev
              ;  {:source-paths ["src/monty/js/script"]
              ;   :compiler {:output-to "static/js/main_dev.js"
              ;              :optimizations :whitespace
              ;              :pretty-print true}}
               
              ;  :prod
              ;  {:source-paths ["src/monty/js/script"]
              ;   :compiler {:output-to "static/js/main.js"
              ;              :pretty-print false
              ;              :optimizations :advanced}}
               
              ;  :pre-prod
              ;  {:source-paths ["src/monty/js/script"]
                
              ;   :compiler {:output-to "static/js/main_pre.js"
              ;              :optimizations :simple
              ;              :pretty-print false
              ;              }}}
              }

  ;; Repos
  :repositories {"sonatype-oss-public" "https://oss.sonatype.org/content/repositories/snapshots/"}

  ;; Main function is in cloj.core namespace
  :main monty.core)