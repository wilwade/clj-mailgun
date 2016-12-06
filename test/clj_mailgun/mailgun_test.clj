(ns clj-mailgun.mailgun-test
  (:require [clojure.test :refer :all]
            [clj-mailgun.mailgun :refer :all]))

(deftest url-test
  (testing "URL generation for the Mailgun API"
    (with-redefs [clj-mailgun.config/mailgun {:url "url" :domain "domain"}]
      (is (= (url "messages") "url/v3/domain/messages")))))
