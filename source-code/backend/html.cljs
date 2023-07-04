(ns backend.html
  (:require [reagent.dom.server :as reagent-dom]
            [config   :refer [version]]))


(defn with-version [url]
  (str url "?version=" version))

(defn css [url]
  [:link {:type "text/css"
          :href url
          :rel "stylesheet"}])

(defn js [url]
  [:script {:type "text/javascript"
            :src url}])

(defn js-anon [url]
  [:script {:type "text/javascript"
            :src url
            :cross-origin "anonymous"}])

(defn template [body]
  [:html
   [:head
    [:title "Wizard"]
    [:link {:rel "icon"
            :type "image/png"
            :href "/images/favicon.png"}]
    [:meta {:charset "utf-8"}]
    [:meta {:name    "viewport"
            :content "width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no"}]
    [css (with-version "/css/normalize.css")]
    [css (with-version "/css/generator.css")]
    [css (with-version "/css/color-picker.css")]
    [css (with-version "/css/wizard.css")]]

   [:body
    [:div#app]
    [js       (with-version "/js/libs/node-modules.js")]
    [js       (with-version "/js/core.js")]]])


(defn ^:export render-page [path]
  (reagent-dom/render-to-static-markup [template [:div]]))

