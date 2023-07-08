(ns frontend.core
  (:require [reagent.dom :as reagent-dom]
            [reagent.core :as reagent]
            ["react-dom/client" :as react-client]
            [frontend.editor :as editor]
            [reagent.impl.template :as reagent-template]))

(def functional-compiler (reagent/create-compiler {:function-components true}))

(reagent-template/set-default-compiler! functional-compiler)

(defn view []
  [editor/view])

(def root  (react-client/createRoot (.getElementById js/document "app")))

(defn start! [] (.render root (reagent/as-element [view])))


