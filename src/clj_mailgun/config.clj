(ns clj-mailgun.config
  (:require [environ.core :refer [env]]))

(def mailgun
  {:key (env :mailgun-api-key)
   :domain (env :mailgun-api-domain)
   :url "https://api.mailgun.net"})
