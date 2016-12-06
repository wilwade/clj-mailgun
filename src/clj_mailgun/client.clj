(ns clj-mailgun.client
  (:require
    [org.httpkit.client :as http]
    [clojure.data.json :as json]))

(def defaults
  {:timeout 1000
   :insecure? false})

(def defaults-json
  {:headers {"Content-Type" "application/json; charset=utf-8"}})

(defn- request
  [url method opts]
  (http/request
    (merge defaults
           {:url url :method method}
           opts)))

(defn post-json
  ([url m] (post-json url m {}))
  ([url m opts]
   (request url :post
     (merge defaults-json
            {:body (json/write-str m :key-fn name)}
            opts))))

(defn post
  ([url params] (post url params {}))
  ([url params opts]
    (request url :post
      (merge
        {:form-params params}
        opts))))
