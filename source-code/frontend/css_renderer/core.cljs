(ns frontend.css-renderer.core
  (:require
   ["three" :as three]
   ["react" :as react]
   ["@three-ts/orbit-controls" :refer [OrbitControls]]
   ["three-css3d" :refer [CSS3DRenderer, CSS3DSprite, CSS3DObject]]
   [re-frame.core :refer [dispatch-sync subscribe]]
   [reagent.dom.server :as reagent-server]))

; Renderer settings 





; Connecting webgl to the MVC model

(defn append-to-canvas! []
  (let [renderer @(subscribe [:db/get [:css3d :renderer]])]
    (.appendChild
     (.getElementById js/document "css-container")
     (.-domElement ^js renderer))))


(defn fancy-html []
  (let [html-string (reagent-server/render-to-string
                     [:div "hello there"])
        dom-element  (js/document.createElement "div")]
    (set! (.-innerHTML dom-element) html-string)
    dom-element))


(defn add-scene-to-db! []
  (let [scene (new three/Scene)
        css-object  (new CSS3DObject (fancy-html))]
    (.add scene css-object)
    (dispatch-sync [:db/set! [:css3d :scene] scene])))

(defn add-renderer-to-db! []
  (let [renderer (new CSS3DRenderer
                      #js {:antialias true
                           :alpha true})]
    (.setSize renderer (.-innerWidth js/window) (.-innerHeight js/window))
    (.setClearColor renderer 0xffffff 0)
    (dispatch-sync [:db/set! [:css3d :renderer] renderer])))



; View
(add-scene-to-db!)
(add-renderer-to-db!)

(defn setup-effect []
  (fn []
    (append-to-canvas!)
    (fn [])))

(defn view []
  (react/useEffect (setup-effect)
                   #js [])
  [:div#css-container {:style {:position :absolute
                               :top 0
                               :left 0
                               :width "100%"
                               :height "100%"
                               :z-index 2}}])