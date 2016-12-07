(ns clj-mailgun.routes-test
  (:require [clojure.test :refer :all]
            [clj-mailgun.routes :refer :all]
            [clj-mailgun.unique :as u]
            [clojure.data.json :as json]))

(defn request-json
  [method uri body]
  (with-redefs [clj-mailgun.mailgun/mail (fn [x] {:status 200 :id (u/gen-id) :message "Thanks!"})]
    (app
      {:request-method method
       :uri uri
       :body body
       :content-type"application/json"})))

(deftest send-mail-test
  (testing "Send email"
    (let [resp (request-json :post "/send" {:test "hello"})
          body (json/read-str (:body resp))]
      (is (get body "id"))
      (is (get body "mailgun-id")))))
