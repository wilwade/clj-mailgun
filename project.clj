(defproject clj-mailgun "0.1.0-SNAPSHOT"
  :description "Sample Clojure code for working with the Mailgun API"
  :url "https://github.com/wilwade/clj-mailgun"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies
    [[org.clojure/clojure "1.8.0"]
     [environ "1.1.0"]
     [org.clojure/data.json "0.2.6"]
     [org.clojure/tools.cli "0.3.5"]
     [http-kit "2.2.0"]]
  :main ^:skip-aot clj-mailgun.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
