(ns frontend.css-renderer.core
  (:require
   ["three" :as three]
   ["react" :as react]
   ["react-dom" :as react-dom]
   ["@three-ts/orbit-controls" :refer [OrbitControls]]
   ["three-css3d" :refer [CSS3DRenderer, CSS3DSprite, CSS3DObject]]
   [re-frame.core :refer [dispatch-sync subscribe]]
   [reagent.dom.server :as reagent-server]
   [reagent.core :as reagent]))

; Renderer settings 


; Connecting webgl to the MVC model

(defn append-to-canvas! []
  (let [renderer @(subscribe [:db/get [:css3d :renderer]])]
    (.appendChild
     (.getElementById js/document "css-container")
     (.-domElement ^js renderer))))



(defn page-dom-element-reagent [id width height]
  [:div
   {:id id
    :style {:width width
            :height height
            :overflow-y :scroll}}])

(defn page-dom-element [id width height]
  (let [html-string (reagent-server/render-to-string
                     [page-dom-element-reagent id width height])
        dom-element  (js/document.createElement "div")]
    (set! (.-innerHTML dom-element) html-string)
    dom-element))


(defn add-page! [{:keys [path  id width height]}]
  (let [scene @(subscribe [:db/get [:css3d :scene]])
        css-object  (new CSS3DObject (page-dom-element id width height))]
    (.add scene css-object)
    (dispatch-sync [:db/set!
                    [:css3d :scene-elements :text 1]
                    {:element css-object
                     :id id}])))

(defn add-scene-to-db! []
  (let [scene (new three/Scene)]
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


(defn timer []
  (let [[count set-count] (react/useState 0)]
    (react/useEffect
     (fn []
       (.setTimeout js/window
                    (fn [e] (set-count (inc count)))
                    1000)
       (fn []))
     #js [count])
    [:div#timer
     {:style {:color "yellow"
              :text-shadow "2px 2px #ff0000"
              :font-size "30px"}}
     [:div (str count)]]))

(defn page-portal [{:keys [id path width height]}]
  (let [element (fn [] (try (js/document.getElementById id)
                            (catch js/Error e (fn [e] nil))))
        [element? set-element?] (react/useState false)]
    (when-not element?
      (add-page! {:path [:editor :files :css3d id]
                  :width  width
                  :id id
                  :height height}))
    (react/useEffect (fn []
                       (set-element? true)
                       (fn []))
                     #js [])
    (when (element)
      [:<>
       (react-dom/createPortal
        (reagent/as-element
         [:div {:style
                {:position :absolute
                 :pointer-events :none
                 :top 0
                 :left 0
                 :width "100%"
                 :height "100%"
                 :display :flex
                 :justify-content :center
                 :align-content :center}}
          [timer]])
        (element))])))


(defn view []
  (react/useEffect (setup-effect)
                   #js [])
  (let [id @(subscribe [:db/get [:css3d :scene-elements :text 1 :id]])]
    [:div#css-container {:style {:position :absolute
                                 :top 0
                                 :left 0
                                 :width "100%"
                                 :height "100%"
                                 :z-index 2}}
     (println id)]))