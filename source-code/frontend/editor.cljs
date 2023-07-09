(ns frontend.editor
  (:require ["@react-three/cannon" :refer [Physics usePlane useBox useCylinder useRaycastVehicle]]
            ["@react-three/drei" :refer [useGLTF Sky Environment PerspectiveCamera Html RoundedBox Box, OrbitControls
                                         useBoundingBox
                                         Center
                                         Text Text3D]]
            ["@react-three/fiber" :refer [Canvas useFrame]]
            ["react" :refer [useRef Suspense useEffect]]
            ["three" :as THREE :refer [DoubleSide]]
            [re-frame.core :refer [dispatch subscribe]]

            [reagent.core :as r]))

(def demo-text
  "(ns frontend.editor\n  (:require [\"@react-three/cannon\" :refer [Physics usePlane useBox useCylinder useRaycastVehicle]]\n            [\"@react-three/drei\" :refer [useGLTF Sky Environment PerspectiveCamera Html RoundedBox Box, OrbitControls\n                                         useBoundingBox\n                                         Center\n                                         Text Text3D]]\n            [\"@react-three/fiber\" :refer [Canvas useFrame]]\n            [\"react\" :refer [useRef Suspense useEffect]]\n            [\"three\" :as THREE :refer [DoubleSide]]\n            [re-frame.core :refer [dispatch subscribe]]\n\n            [reagent.core :as r]))\n\n(def canvas (r/adapt-react-class Canvas))\n(def sky (r/adapt-react-class Sky))\n(def environment (r/adapt-react-class Environment))\n(def physics (r/adapt-react-class Physics))\n(def suspense (r/adapt-react-class Suspense))\n(def perspective-camera (r/adapt-react-class PerspectiveCamera))\n\n;; onst vFov = (camera.fov * Math.PI) / 180;\n;; const height = 2 * Math.tan(vFov / 2) * camera.position.z;\n;; const aspect = window.innerWidth / window.innerHeight;\n;; const width = height * aspect;\n;; const pixelSize = window.innerWidth * ((1 / width) * sizeOfObject) // (e.g. size\n\n;; (defn get-size-in-px [size-of-object]\n;;   (let [vFov (/ (* 50 (.-PI js/Math)) 180)\n;;         height (* 2 2 (.tan js/Math (/ vFov 2)))\n;;         aspect (/ (.-innerWidth js/window) (.-innerHeight js/window))\n;;         width (* height aspect)\n;;         pixelSize (* (.-innerWidth js/window)\n;;                      (* (/ 1 width) size-of-object))]\n;;     pixelSize))\n\n\n;; (defn html-component [{:keys [text width height]}]\n;;   [:> Html\n;;    {:occlude true\n;;     :distanceFactor  3.8\n;;     :position  [0, 0, 0.51]\n;;     :transform true\n;;     :class \"html-element\"\n;;     :style {:height height\n;;             :width width\n;;             :border-radius \"10px\"\n;;             :padding \"10px\"\n;;             :pointer-events :none\n;;             :border \"1px solid rgba(255,0,0,0.3)\"}}\n;;    [:div\n;;     {:style {:height \"100%\"\n;;              :pointer-events :none\n;;              :display :flex\n;;              :justify-content :center\n;;              :align-items :center}}\n;;     [:div text]]])\n\n\n\n(defn lights []\n  [:<>\n   [:pointLight {:color \"white\"\n                 :intensity 0.6\n                 :position [0, 3, -5]}]\n                 ;:castShadow true}]\n   [:ambientLight {:args [\"white\" 0.2]}]\n                   ;:castShadow true}]\n\n   [:spotLight {:args [\"white\" 1]\n                :position [-2 2 0]}]])\n                ;:castShadow true}]])\n\n(defn letter-box [{:keys [char position size color]}]\n  (let [box-ref (useRef)\n        [box-width box-height] size\n        html-width (* 100 box-width)\n        html-height (* 100 box-height)\n        offset-position-background\n        (let [[x y z]  position]\n          [(- x 3)\n           (- (+ 3.5 y) 0.75)\n           (+ z 0.05)])\n        offset-position-letter\n        (let [[x y z]  offset-position-background]\n          [x\n           y\n           (+ z 0.05)])]\n    (useEffect (fn []\n                 (let [])\n\n                   ;(.log js/console \"hello: \" (get-size-in-px 1)))\n                 (fn []))\n               #js [])\n    [:<>\n     [:group {:position offset-position-background}\n      [:mesh {:receiveShadow true\n              :onPointerDown (fn [e]\n                               (.log js/console (.-point ^js e)))}\n       [:planeGeometry {:args [1 1.5]}]\n       [:meshPhongMaterial {:color \"red\"\n                            :side DoubleSide\n                            :opacity 0.3\n                            :transparent true}]]]\n\n     [:group {:position offset-position-letter}\n      [:> Text {:font  \"/fonts/font.ttf\"\n;\"/fonts/fragment-mono.json\"\n                ;:letterSpacing -0.06\n                :fontSize 1.5\n                :maxWidth 7}\n       char\n       [:meshPhongMaterial {:color \"#333\"}]]]]))\n\n    ;;  (comment [:f> html-component {:text  text\n    ;;                                :width html-width\n    ;;                                :height html-height}])]))\n\n\n(defn text-iterator [text row-length]\n  (get\n   (reduce\n    (fn [{:keys [result index]} letter]\n      (assoc {}\n             :index (inc index)\n             :result (vec\n                      (conj result\n                            [:f> letter-box\n                             {:char letter\n                              :size [1 2]\n                              :position [(- index\n                                            (*\n                                             (quot  index row-length)\n                                             row-length))\n                                         (- (* 1.5\n                                               (quot  index row-length)))\n                                         0]\n                              :color \"yellow\"}]))))\n    {:result [:<>]\n     :index 0}\n    text)\n   :result))\n\n\n\n(defn page-box [[page-width page-height]]\n  [:mesh {:rotation [0 0 0]\n          :position [0 0 0]\n          :receiveShadow true\n          :onPointerDown (fn [e]\n                           (.log js/console (.-point ^js e)))}\n   [:planeGeometry {:args [page-width page-height]}]\n   [:meshPhongMaterial {:color \"blue\"\n                        :side DoubleSide\n                        :opacity 0.3\n                        :transparent true}]])\n\n(defn view []\n  (let [page-width  100\n        page-height 1000]\n    [canvas\n     {:dpr [1 2]\n      :shadows true\n      :camera {:position [0 0 100]\n               :near 0.1\n               :far 2000\n               :fov 50}}\n  ;;  [:fog {:attach \"fog\" :args [\"white\" 0 350]}]\n     [sky {:sun-position [100 10 100] :scale 1000}]\n   ;[:ambientLight {:intensity 0.1}]\n     [:> OrbitControls {:makeDefault true}]\n     [:f> lights]\n     [page-box [page-width page-height]]\n     [text-iterator \"I am putting a longer text here\" page-width]]))\n\n\n\n    ;; [:f> plane {:rotation [(/ (- js/Math.PI) 2) 0 0]\n    ;;             :userData {:id \"floor\"}}]\n    ;; [:f> vehicle {:rotation [0 (/ js/Math.PI 2) 0]\n    ;;               :position [0 2 0]\n    ;;               :angularVelocity [0 0.5 0]\n    ;;               :wheelRadius 0.3}]]])")

(def canvas (r/adapt-react-class Canvas))
(def sky (r/adapt-react-class Sky))
(def environment (r/adapt-react-class Environment))
(def physics (r/adapt-react-class Physics))
(def suspense (r/adapt-react-class Suspense))
(def perspective-camera (r/adapt-react-class PerspectiveCamera))

;; onst vFov = (camera.fov * Math.PI) / 180;
;; const height = 2 * Math.tan(vFov / 2) * camera.position.z;
;; const aspect = window.innerWidth / window.innerHeight;
;; const width = height * aspect;
;; const pixelSize = window.innerWidth * ((1 / width) * sizeOfObject) // (e.g. size

;; (defn get-size-in-px [size-of-object]
;;   (let [vFov (/ (* 50 (.-PI js/Math)) 180)
;;         height (* 2 2 (.tan js/Math (/ vFov 2)))
;;         aspect (/ (.-innerWidth js/window) (.-innerHeight js/window))
;;         width (* height aspect)
;;         pixelSize (* (.-innerWidth js/window)
;;                      (* (/ 1 width) size-of-object))]
;;     pixelSize))


;; (defn html-component [{:keys [text width height]}]
;;   [:> Html
;;    {:occlude true
;;     :distanceFactor  3.8
;;     :position  [0, 0, 0.51]
;;     :transform true
;;     :class "html-element"
;;     :style {:height height
;;             :width width
;;             :border-radius "10px"
;;             :padding "10px"
;;             :pointer-events :none
;;             :border "1px solid rgba(255,0,0,0.3)"}}
;;    [:div
;;     {:style {:height "100%"
;;              :pointer-events :none
;;              :display :flex
;;              :justify-content :center
;;              :align-items :center}}
;;     [:div text]]])



(defn lights []
  [:<>
   [:pointLight {:color "white"
                 :intensity 0.6
                 :position [0, 3, -5]}]
                 ;:castShadow true}]
   [:ambientLight {:args ["white" 0.2]}]
                   ;:castShadow true}]

   [:spotLight {:args ["white" 1]
                :position [-2 2 0]}]])
                ;:castShadow true}]])

(defn letter-box [{:keys [char position size color]}]
  (let [box-ref (useRef)
        [box-width box-height] size
        html-width (* 100 box-width)
        html-height (* 100 box-height)
        offset-position-background
        (let [[x y z]  position]
          [(- x 3)
           (- (+ 3.5 y) 0.75)
           (+ z 0.05)])
        offset-position-letter
        (let [[x y z]  offset-position-background]
          [x
           y
           (+ z 0.05)])]
    (useEffect (fn []
                 (let [])

                   ;(.log js/console "hello: " (get-size-in-px 1)))
                 (fn []))
               #js [])
    [:<>
     [:group {:position offset-position-background}
      [:mesh {:receiveShadow true
              :onPointerDown (fn [e]
                               (.log js/console (.-point ^js e)))}
       [:planeGeometry {:args [1 1.5]}]
       [:meshPhongMaterial {:color "red"
                            :side DoubleSide
                            :opacity 0.3
                            :transparent true}]]]

     [:group {:position offset-position-letter}
      [:> Text {:font  "/fonts/font.ttf"
;"/fonts/fragment-mono.json"
                ;:letterSpacing -0.06
                :fontSize 1.5
                :maxWidth 7}
       char
       [:meshPhongMaterial {:color "#333"}]]]]))

    ;;  (comment [:f> html-component {:text  text
    ;;                                :width html-width
    ;;                                :height html-height}])]))


(defn text-iterator [text row-length]
  (get
   (reduce
    (fn [{:keys [result index]} letter]
      (assoc {}
             :index (inc index)
             :result (vec
                      (conj result
                            [:f> letter-box
                             {:char letter
                              :size [1 2]
                              :position [(- index
                                            (*
                                             (quot  index row-length)
                                             row-length))
                                         (- (* 1.5
                                               (quot  index row-length)))
                                         0]
                              :color "yellow"}]))))
    {:result [:<>]
     :index 0}
    text)
   :result))



(defn page-box [[page-width page-height]]
  [:mesh {:rotation [0 0 0]
          :position [0 0 0]
          :receiveShadow true
          :onPointerDown (fn [e]
                           (.log js/console (.-point ^js e)))}
   [:planeGeometry {:args [page-width page-height]}]
   [:meshPhongMaterial {:color "blue"
                        :side DoubleSide
                        :opacity 0.3
                        :transparent true}]])

(defn view []
  (let [page-width  100
        page-height 1000]
    [canvas
     {:dpr [1 2]
      :shadows true
      :camera {:position [0 0 100]
               :near 0.1
               :far 2000
               :fov 50}}
  ;;  [:fog {:attach "fog" :args ["white" 0 350]}]
     [sky {:sun-position [100 10 100] :scale 1000}]
   ;[:ambientLight {:intensity 0.1}]
     [:> OrbitControls {:makeDefault true}]
     [:f> lights]
     [page-box [page-width page-height]]
     [text-iterator demo-text page-width]]))



    ;; [:f> plane {:rotation [(/ (- js/Math.PI) 2) 0 0]
    ;;             :userData {:id "floor"}}]
    ;; [:f> vehicle {:rotation [0 (/ js/Math.PI 2) 0]
    ;;               :position [0 2 0]
    ;;               :angularVelocity [0 0.5 0]
    ;;               :wheelRadius 0.3}]]])