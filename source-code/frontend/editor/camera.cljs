(ns frontend.editor.camera
  (:require ["three" :as three]))

(def ^js camera (new three/PerspectiveCamera
                     75
                     (/ (.-innerWidth js/window)
                        (.-innerHeight js/window))
                     0.1
                     5000))


(.setZ (-> camera .-position) 50)