(ns clj-mailgun.templates-test
  (:require [clojure.test :refer :all]
            [clojure.string :as str]
            [clj-mailgun.templates :refer :all]))

(deftest render-test
  (testing "Render generic email templates"
    (let [template "generic"
          params {:content "abcdefghi" :title "Title"}
          rendered (render template params)]
      (is (:html rendered))
      (is (:text rendered))
      (is (str/includes? (str (:text rendered)) (:content params))))))
