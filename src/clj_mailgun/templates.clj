(ns clj-mailgun.templates
  (:require
    [clostache.parser :as stache]
    [clojure.java.io :as io]))

(defn render-template
  [template params]
  (stache/render-resource template params))

(defn render-html-template
  [template params]
  (let [htmlTemplate (str "templates/" template "-html.mustache")]
    (when (io/resource htmlTemplate)
      (render-template htmlTemplate params))))

  (defn render-text-template
    [template params]
    (let [textTemplate (str "templates/" template "-text.mustache")
          defaultTemplate (str "templates/" template ".mustache")]
      (cond
        (io/resource textTemplate) (render-template textTemplate params)
        (io/resource defaultTemplate) (render-template defaultTemplate params))))

(defn render
  "Returns map of the HTML and text versions"
  [template params]
  (try
    {:html (render-html-template template params)
     :text (render-text-template template params)}
    (catch Exception e
      (throw (ex-info "Failed to render template!" {:template template :params params})))))
