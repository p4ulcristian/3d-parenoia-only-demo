(ns frontend.editor.render-cycle
  (:require [frontend.editor.camera :refer [camera]]
            [re-frame.core :refer [dispatch-sync subscribe]]
            ["three" :as three]
            ["@three-ts/orbit-controls" :refer [OrbitControls]]))

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
    ;(animations/rotate-object! [:cube 1])
    ;(animations/rotate-object! [:css3d :scene-elements :text 1])
    (.requestAnimationFrame js/window animate!)
    (.render ^js webgl-renderer webgl-scene camera)
    (.render ^js css3d-renderer css3d-scene camera)))

(defn start! []
  (add-grid-helper!)
  (animate!))