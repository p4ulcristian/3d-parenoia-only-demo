(ns frontend.animations.core
  (:require [re-frame.core :refer [dispatch-sync subscribe]]))


(defn rotate-object! [path]
  (let [object @(subscribe [:db/get path])
        object-x (-> object .-rotation .-x)
        object-y (-> object .-rotation .-y)]

    (set! (-> object .-rotation .-x) (+ 0.01 object-x))
    (set! (-> object .-rotation .-y) (+ 0.01 object-y))))