(ns clj-mailgun.log
  (:refer-clojure :rename {min core-min})
  (:require
   [clj-mailgun.config :as config]
   [clojure.string :as str]))

(defn- out
  [prefix message data]
  (println (str prefix message "\n" data)))

(defn debug
  ([message] (debug message nil))
  ([message data]
    (when (:debug config/log)
      (out "DEBUG: " message data))))

(defn info
  ([message] (info message nil))
  ([message data]
    (when (:info config/log)
      (out "INFO: " message data))))

(defn error
  ([message] (error message nil))
  ([message data]
    (when (:error config/log)
      (out "ERROR: " message data))))
