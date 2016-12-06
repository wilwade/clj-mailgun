(ns clj-mailgun.core
  (:gen-class)
  (:require
    [clj-mailgun.config :as config]
    [clj-mailgun.mailgun :as mailgun]
    [clojure.data.json :as json]
    [clojure.string :as str]
    [clojure.java.io :as io]
    [clojure.tools.cli :refer [parse-opts]]))

(defn parse-json
  [file]
  (let [f (io/as-file file)]
    (when (.exists f)
      (some-> f
              slurp
              (json/read-str :key-fn keyword :eof-error? false)))))

(def cli-options
  [["-j" "--json JSON_FILE" "JSON file for sending email"
    :parse-fn parse-json
    :validate [#(not (empty? %)) (str "Unable to locate or parse JSON file." )]]
   ["-h" "--help"]])

 (defn usage
   [options-summary]
   (->> ["Send email using Mailgun"
         ""
         "Remember, you need your enviroment variables for Mailgun API:"
         "MAILGUN_API_KEY - API Key"
         "MAILGUN_API_DOMAIN - Maingun Domain"
         ""
         "Usage: lein run -- --json JSON_FILE"
         ""
         "JSON File Should have keys:"
         "to - To Email Address"
         "from - From Email Address"
         "subject - Email Subject"
         "body - Email HTML body"
         ""
         "Options:"
         options-summary
         ""]
        (str/join \newline)))

 (defn error-msg
   [errors]
   (str "Failed to send:\n"
        (str/join \newline errors)))

 (defn exit
   [status msg]
   (println msg)
   (System/exit status))

(defn- validate-config
  []
  (let [mg config/mailgun]
    (when (empty? (:key mg))
      (exit 1 (error-msg ["Missing environment Mailgun Key! (export MAILGUN_API_KEY=\"your_key\")"])))
    (when (empty? (:domain mg))
      (exit 1 (error-msg ["Missing environment Mailgun Domain! (export MAILGUN_API_DOMAIN=\"your_api_domain\")"])))))

(defn -main
  [& args]
  (let [{:keys [options arguments errors summary]} (parse-opts args cli-options)]
    ;; Handle help and error conditions
    (cond
      (:help options) (exit 0 (usage summary))
      errors (exit 1 (error-msg errors)))
    (validate-config)
    (try
      (let [resp @(mailgun/send-json (:json options))]
        (if (= 200 (:status resp))
          (let [body (json/read-str (:body resp) :key-fn keyword)]
            (exit 0 (str "Success! Mailgun id: " (:id body))))
          (exit 1 (str "Failed: " (mailgun/response-error-str resp)))))
      (catch Exception e
        (exit 1 (error-msg [(.getMessage e)]))))))
