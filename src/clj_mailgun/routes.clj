(ns clj-mailgun.routes
  (:require
    [clj-mailgun.mailgun :as mailgun]
    [clj-mailgun.log :as log]
    [clj-mailgun.unique :as u]
    [compojure.core :refer :all]
    [compojure.route :as route]
    [ring.middleware.json :refer [wrap-json-response wrap-json-body]]))

(defn- wrap-exception-handling
  [handler]
  (fn [request]
    (try
      (handler request)
      (catch clojure.lang.ExceptionInfo e
        (log/error (.getMessage e) (ex-data e))
        {:status 400
        :body {:message (.getMessage e)}}))))

  (defn wrap-exception-final
    [handler]
    (fn [request]
      (try
        (handler request)
        (catch Exception e
          (log/error (.getMessage e) {:request request})
          {:status 500 :body "Unknown Error"}))))

(defn- send-mail-action
  [id body]
  (let [resp (mailgun/send-json body)]
    (log/debug "Mailgun Response" resp)
    (if (= 200 (:status resp))
      {:status 200
       :body {:id id
              :mailgun-id (get-in resp [:body :id])}}
      (throw (ex-info (mailgun/response-error-str resp) resp)))))

(defn send-mail
  [id body]
  (let [id (or id (:id body) (u/gen-id))]
    (if (u/id-used? id)
      {:status 409}
      (do
        (u/mark-id-used id)
        (send-mail-action id body)))))

(defroutes app-routes
  (POST "/send/:id" [id :as req]
    (send-mail id (:body req)))
  (POST "/send" [id :as req]
    (send-mail id (:body req)))
  (route/resources "/")
  (route/not-found "Nothing Here"))

(def app
  (try
    (-> app-routes
        (wrap-json-body {:keywords? true})
        (wrap-json-response)
        (wrap-exception-handling)
        (wrap-exception-final))))
