(ns frontend.editor
  (:require [frontend.state-management]
            [frontend.webgl-renderer.core :as webgl-renderer]
            [frontend.css-renderer.core :as css-renderer]
            [re-frame.core :refer [dispatch-sync subscribe]]
            ["@three-ts/orbit-controls" :refer [OrbitControls]]
            ["three" :as three]
            ["react" :as react]
            [frontend.animations.core :as animations]))

(def ^js camera (new three/PerspectiveCamera
                     75
                     (/ (.-innerWidth js/window)
                        (.-innerHeight js/window))
                     0.1
                     5000))


(.setZ (-> camera .-position) 50)

(defn add-grid-helper! []
  (let [scene @(subscribe [:db/get [:webgl :scene]])
        controls (new OrbitControls camera)
        grid-helper (new three/GridHelper 200 50)]
    (.add scene controls grid-helper)))

(defn animate! []
  (let [webgl-scene     @(subscribe [:db/get [:webgl :scene]])
        webgl-renderer  @(subscribe [:db/get [:webgl :renderer]])
        css3d-scene     @(subscribe [:db/get [:css3d :scene]])
        css3d-renderer  @(subscribe [:db/get [:css3d :renderer]])]
    (animations/rotate-object! [:cube 1])
    (animations/rotate-object! [:text 1])
    (.requestAnimationFrame js/window animate!)
    (.render ^js webgl-renderer webgl-scene camera)
    (.render ^js css3d-renderer css3d-scene camera)))

(defn view []
  (react/useEffect (fn []
                     (animate!)
                     (add-grid-helper!)
                     (fn []))
                   #js [])
  [:div
   [css-renderer/view]
   [webgl-renderer/view]])