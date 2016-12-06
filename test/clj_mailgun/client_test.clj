(ns clj-mailgun.client-test
  (:require [clojure.test :refer :all]
            [clojure.data.json :as json]
            [clj-mailgun.client :refer :all]))

(deftest post-json-test
  (testing "Posting JSON"
    (with-redefs [org.httpkit.client/request identity]
      (let [req (post-json "url-test" {:a "1" :b 2})]
        (is (:url req))
        (is (= (:url req) "url-test"))
        (is (:body req))
        (is (= (:body req) "{\"a\":\"1\",\"b\":2}"))))))
