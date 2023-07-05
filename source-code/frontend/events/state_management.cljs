(ns frontend.events.state-management
  (:require [re-frame.core :refer [subscribe dispatch reg-event-db reg-sub]]))

(reg-event-db
 :db/set!
 (fn [db [_ path value]]
   (assoc-in db path value)))

(reg-sub
 :db/get
 (fn [db [_ path]]
   (get-in db path)))