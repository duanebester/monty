(ns monty.controllers.service

(:import
[java.lang Runtime System]
[java.io File])
(:require [sigmund.core :as sig]
	        [clojurewerkz.quartzite.scheduler :as qs]
          [clojurewerkz.quartzite.triggers :as trigger]
          [clojurewerkz.quartzite.jobs :as job]
          [clojurewerkz.quartzite.jobs :refer [defjob]]
          [clojurewerkz.quartzite.schedule.cron :refer [schedule cron-schedule]])
(:use [clojure.tools.logging :only (info error)]))

(def get-sysinfo-map
  (merge
  {"processorCount" (.availableProcessors (Runtime/getRuntime)),
   "osName"         (System/getProperty "os.name"),
   "osArch"         (System/getProperty "os.arch"),
   "username"       (System/getProperty "user.name"),
   "userhome"       (System/getProperty "user.home"),
   "javaVersion"    (System/getProperty "java.version")}
  (apply #(hash-map (str "freeSpace_" (.getAbsolutePath %))
                    ;; (1024 * 1024) = MB, (1024 * 1024 * 1024) = GB
                    (float (/ (.getFreeSpace %) (* 1024 1024 1024))))
  (java.io.File/listRoots))))

#_(defjob sendSysInfo
  [ctx]
  (broadcastWS get-sysinfo-map))

;; jk = "jobs.sysinfo.1"
;; tk = "triggers.1"

(defn start-service
  [jk tk jobType & m]
  (qs/initialize)
  (qs/start)
  (let [job (job/build
              (job/of-type jobType)
              (job/with-identity (job/key jk)))
        tk    (trigger/key tk)
        trigger (trigger/build
              (trigger/with-identity tk)
              (trigger/start-now)
              (trigger/with-schedule 
              	(schedule
                  (cron-schedule "0 0/1 * 1/1 * ? *"))))]
  (info "started Job" (str jobType))
  (qs/schedule job trigger)))

(defn stop-service
  "tk here is a trigger string key"
  [tk]
  (qs/unschedule-job (trigger/key tk)))