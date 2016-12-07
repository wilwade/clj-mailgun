(ns clj-mailgun.core
  (:gen-class)
  (:require
    [clj-mailgun.config :as config]
    [clj-mailgun.routes :as routes]
    [clj-mailgun.log :as log]
    [org.httpkit.server :as srv]
    [clojure.data.json :as json]
    [clojure.string :as str]
    [clojure.java.io :as io]
    [clojure.tools.cli :refer [parse-opts]]))

(defonce server (atom nil))

(defn stop-server []
  (when-not (nil? @server)
    ;; graceful shutdown: wait 100ms for existing requests to be finished
    ;; :timeout is optional, when no timeout, stop immediately
    (@server :timeout 100)
    (reset! server nil)))

(defn- start-server
  []
  (reset! server (srv/run-server #'routes/app config/server)))

(defn exit
   [status msg]
   (log/error msg)
   (System/exit status))

(defn- validate-config
  []
  (let [mg config/mailgun]
    (when (empty? (:key mg))
      (exit 1 "Missing environment Mailgun Key! (export MAILGUN_API_KEY=\"your_key\")"))
    (when (empty? (:domain mg))
      (exit 1 "Missing environment Mailgun Domain! (export MAILGUN_API_DOMAIN=\"your_api_domain\")"))))

(defn -main
  []
  (validate-config)
  (start-server)
  (log/info (str "clj-mailgun http server started on port " (:port config/server))))
