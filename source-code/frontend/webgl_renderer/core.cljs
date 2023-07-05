(ns frontend.webgl-renderer.core
  (:require
   ["three" :as three]
   ["react" :as react]
   ["@three-ts/orbit-controls" :refer [OrbitControls]]
   [re-frame.core :refer [dispatch-sync subscribe]]))


; Renderer settings 




; Settings up scenes

(defn add-cube! [{:keys [path position]}]
  (let [[depth index] position
        scene @(subscribe [:db/get [:webgl :scene]])
        geometry (new three/BoxGeometry 500 1000 0.01)
        texture  (.load
                  (new three/TextureLoader)
                  "/images/texture.jpg")
        material (new three/MeshStandardMaterial #js {:map texture})
        cube     (new three/Mesh geometry material)]
    (.setX (.-position cube) (* index 700))
    (.setZ (.-position cube) (* depth -700))
    (.add scene cube)

    (dispatch-sync [:db/set! path cube])))

(defn add-light! [path]
  (let [scene @(subscribe [:db/get [:webgl :scene]])
        light (new three/PointLight 0xffffff 1)
        light-helper (new three/PointLightHelper light)]
    (.set (.-position light) 0 0 10)
    (.add scene light light-helper)
    (dispatch-sync [:db/set! path light])))


; Connecting webgl to the MVC model

(defn append-to-canvas! []
  (let [renderer @(subscribe [:db/get [:webgl :renderer]])]
    (.appendChild
     (.getElementById js/document "webgl-container")
     (.-domElement ^js renderer))))


(defn add-scene-to-db! []
  (let [scene (new three/Scene)
        space-texture (.load
                       (new three/TextureLoader)
                       "/images/background.jpg")]
    (set! (.-background scene) space-texture)
    (dispatch-sync [:db/set! [:webgl :scene] scene])))

(defn add-renderer-to-db! []
  (let [renderer (new three/WebGLRenderer
                      #js {:antialias true
                           :alpha true})]
    (.setSize renderer (.-innerWidth js/window) (.-innerHeight js/window))
    (.setClearColor renderer 0xffffff 0)
    (dispatch-sync [:db/set! [:webgl :renderer] renderer])))

(add-scene-to-db!)
(add-renderer-to-db!)





; View


(defn process-file-structure [file-structure])

(defn setup-effect []
  (fn []
    (append-to-canvas!)
    (add-cube! {:path [:cube 1]
                :position [0 0]})
    (add-cube! {:path [:cube 1]
                :position [0 1]})
    (add-cube! {:path [:cube 1]
                :position [0 2]})
    (add-cube! {:path [:cube 1]
                :position [1 0]})
    (add-cube! {:path [:cube 1]
                :position [2 0]})
    (add-cube! {:path [:cube 1]
                :position [2 1]})
    (add-light! [:light 1])
    (fn [])))

(defn view []
  (react/useEffect (setup-effect)
                   #js [])
  [:div#webgl-container {:style {:position :absolute
                                 :top 0
                                 :left 0
                                 :width "100%"
                                 :height "100%"
                                 :z-index 1}}])