(ns frontend.editor
  (:require ["three" :as three]
            ["@three-ts/orbit-controls" :refer [OrbitControls]]
            ["react" :as react]
            [frontend.state-management]
            [re-frame.core :refer [dispatch-sync subscribe]]))




(def camera (new three/PerspectiveCamera
                 75
                 (/ (.-innerWidth js/window)
                    (.-innerHeight js/window))
                 0.1
                 1000))

(.setZ (-> camera .-position) 30)


(def renderer (new three/WebGLRenderer))

(.setPixelRatio renderer (.-devicePixelRatio js/window))
(.setSize renderer (.-innerWidth js/window) (.-innerHeight js/window))

(defn add-cube! [path]
  (let [scene @(subscribe [:db/get [:scene]])
        geometry (new three/BoxGeometry 2 1 1)
        material (new three/MeshStandardMaterial #js {:color 0x00ff00})
        cube     (new three/Mesh geometry material)]
    (.add scene cube)

    (dispatch-sync [:db/set! path cube])))

(defn add-light! [path]
  (let [scene @(subscribe [:db/get [:scene]])
        light (new three/PointLight 0xffffff)
        light-helper (new three/PointLightHelper light)]
    (.set (.-position light) 5 5 5)
    (.add scene light light-helper)
    (dispatch-sync [:db/set! path light])))

(defn add-grid-helper! []
  (let [scene @(subscribe [:db/get [:scene]])
        controls (new OrbitControls camera)
        grid-helper (new three/GridHelper 200 50)]
    (.add scene controls grid-helper)))


(defn add-three-js-to-dom! []
  (.appendChild
   (.-body js/document)
   (.-domElement renderer)))

(defn add-scene-to-db! []
  (dispatch-sync [:db/set! [:scene] (new three/Scene)]))

(add-scene-to-db!)


(defn rotate-cube! [path]
  (let [cube @(subscribe [:db/get path])
        cube-x (-> cube .-rotation .-x)
        cube-y (-> cube .-rotation .-y)]

    (set! (-> cube .-rotation .-x) (+ 0.01 cube-x))
    (set! (-> cube .-rotation .-y) (+ 0.01 cube-y))))

(defn animate! []
  (let [scene @(subscribe [:db/get [:scene]])]
    (rotate-cube! [:cube 1])
    (.requestAnimationFrame js/window animate!)
    (.render renderer scene camera)))

(defn view []
  (react/useEffect
   (fn []
     (add-three-js-to-dom!)
     (add-cube! [:cube 1])
     (add-light! [:light 1])
     (add-grid-helper!)
     (animate!)
     (fn []))
   #js [])
  [:div "Three"
   [:div#editor]])