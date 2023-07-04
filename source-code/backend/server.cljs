
(ns backend.server
  (:require ["express" :as express]
            ["body-parser" :as body-parser]
            [backend.handlers :as handlers]
            [config   :refer [port]]))


(defonce server (atom nil))


(defn start-server []
  (let [app (express)]
    (.use app (express/static "resources/frontend"))
    (.use app (.urlencoded body-parser #js {:extended false}))
    (.use app (.json body-parser))
    (.get app "/"           handlers/index)
    (.get app "/:page"      handlers/index)
    (.listen app port
             (fn [] (println "Port: " port)))))


(defn ^:dev/before-load stop! []
  (when @server (.close @server)))


(defn ^:dev/after-load start! []
  (println "Code updated.")
  (reset! server (start-server)))



