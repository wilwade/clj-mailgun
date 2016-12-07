(ns clj-mailgun.config
  (:require [environ.core :refer [env]]))

(def mailgun
  {:key (env :mailgun-api-key)
   :domain (env :mailgun-api-domain)
   :url "https://api.mailgun.net"})

(def log
 {:debug (env :log-debug false)
  :info (env :log-info true)
  :error (env :log-info true)})

(def server
  {:port (Integer/parseInt (env :server-port "8081"))
   :ip (env :server-ip "0.0.0.0")})
