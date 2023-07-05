(ns frontend.editor.core
  (:require [frontend.webgl-renderer.core :as webgl-renderer]
            [frontend.css-renderer.core :as css-renderer]
            [frontend.editor.render-cycle :as render-cycle]
            [re-frame.core :refer [dispatch-sync subscribe]]

            ["react" :as react]))


(def file-structure
  {:4a9281ed-30d6-4f96-8fa0-b2e536590be7 {:name "backend"
                                          :type :folder
                                          :files {:0f77162d-de35-4ff3-861d-2a7431869101
                                                  {:name "core.cljs"
                                                   :type :file
                                                   :content "Hello world"}}}
   :ad5d44cf-7061-428b-a7e6-84a812097b7c {:name "frontend"
                                          :type :folder
                                          :files {:5b362892-f752-4d9e-94e7-83f76fbeb600
                                                  {:name "core.cljs"
                                                   :type :file
                                                   :content "Na mostmar"}}}})


(defn light []
  (let [id (str (random-uuid))]
    (react/useEffect
     (fn []
       (webgl-renderer/add-light! {:path [:editor :lights id]
                                   :position [0 0 30]})
       (fn []))
     #js [])
    [:<>]))

(defn cube-with-text [{:keys [width height position]}]
  (let [id (str (random-uuid))]
    (react/useEffect
     (fn []
       (webgl-renderer/add-page! {:path [:editor :files id :webgl]
                                  :width  width
                                  :height height
                                  :position position})
       (fn []))
     #js [])
    [:<>
     [css-renderer/page-portal {:path [:editor :files id :css3d]
                                :id id
                                :width width
                                :height height
                                :position position}]]))


(defn view []
  (react/useEffect (fn []
                     (dispatch-sync [:db/set! [:files] file-structure])
                     (render-cycle/start!)
                     (fn []))
                   #js [])
  [:div#editor
   (str @(subscribe [:db/get []]))
   [css-renderer/view]
   [webgl-renderer/view]
   [light]
   [cube-with-text {:width 500
                    :height 500
                    :position [0 0 0]}]
   [cube-with-text {:width 500
                    :height 500
                    :position [700 0 0]}]
   [cube-with-text {:width 500
                    :height 500
                    :position [1400 0 0]}]
   [cube-with-text {:width 500
                    :height 500
                    :position [2100 0 0]}]
   [cube-with-text {:width 500
                    :height 500
                    :position [0 0 -700]}]
   [cube-with-text {:width 500
                    :height 500
                    :position [0 0 -1400]}]
   [cube-with-text {:width 500
                    :height 500
                    :position [0 0 -2100]}]])
   ;[cube-with-text {:position [700 0 0]}]])