(ns clj-mailgun.mailgun
  (:require
    [clojure.string :as str]
    [clojure.data.json :as json]
    [clj-mailgun.client :as client]
    [clj-mailgun.templates :as templates]
    [clj-mailgun.config :as config]))

(defn response-error-str
  [resp]
  (if (= 400 (:status resp))
    (let [body (json/read-str (:body resp) :key-fn keyword)]
      (or (:message body) (:body resp)))
    (:body resp)))

(defn url
  [& parts]
  (let [mc config/mailgun]
    (str (:url mc) "/v3/" (:domain mc) "/" (str/join "/" parts))))

(defn validate-email
  "Really simple email validation. @TODO make better"
  [email]
  (when (not (re-matches #".+\@.+\..+" email))
    (throw (ex-info (str "Invalid email: " email), {:email email}))))

(defn mail
  [{:keys [to from cc bcc subject text html] :as all}]
  (when (not (and from to))
    (throw (ex-info "Unable to send message without a from and to!" all)))
  (when (not (or text html))
    (throw (ex-info "Unable to send message without content in text or html!" all)))
  (validate-email to)
  (validate-email from)
  (let [resp @(client/post
              (url "messages")
              {:from from
               :to to
               :cc cc
               :bcc bcc
               :subject subject
               :text text
               :html html}
               {:basic-auth ["api" (:key config/mailgun)]})]
    (if (= 200 (:status resp))
      (->
        resp
        (assoc :body (json/read-str (:body resp) :key-fn keyword))
        (assoc :status 200))
      resp)))

(defn send-html
  [to from subject body]
  (mail
    {:from from
     :to to
     :subject subject
     :html body}))

(defn send-text
  [to from subject text]
  (mail
    {:from from
     :to to
     :subject subject
     :text text}))

(defn send-template
  [{:keys [to from subject template params] :as all}]
  (let [content (templates/render template params)]
    (if (or (:html content) (:text content))
      (mail (merge
              content
              {:from from
               :to to
               :subject subject}))
      (throw (ex-info "Email content was empty!" all)))))

(defn send-json
  [{:keys [to from subject body template params] :as all}]
  (if template
    (send-template all)
    (send-html to from subject body)))
