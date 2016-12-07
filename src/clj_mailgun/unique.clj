(ns clj-mailgun.unique
  "Ensures idempotent messages")

(defonce sent-ids (atom #{}))

(defn gen-id
  []
  (str (java.util.UUID/randomUUID)))

(defn id-used?
  [id]
  (@sent-ids id))

(defn mark-id-used
  [id]
  (swap! sent-ids conj id))
