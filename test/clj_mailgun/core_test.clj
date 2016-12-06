(ns clj-mailgun.core-test
  (:require [clojure.test :refer :all]
            [clj-mailgun.core :refer :all]
            [clojure.data.json :as json]))

(deftest parse-json-test
  (testing "Parse a JSON file on the disk"
    (let [file "parse-json-test.json"]
    (with-open [w (clojure.java.io/writer file)]
      (json/write {"a" 1 "b" 2} w))
      (is (= (parse-json file) {:a 1 :b 2}))
    (clojure.java.io/delete-file file))))
