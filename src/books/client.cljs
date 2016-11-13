(ns books.client
  (:require [books.controllers :as ctrls]
            [books.components :as c]
            [rum.core :as r]
            [cljs.reader :as reader]))

(defn load-init-state []
  (reader/read-string (.-text (js/document.getElementById "initial-data"))))

(r/defc wrapper < r/reactive
  [s]
  (c/app (r/react s)))

(defn init
  []
  (let [init-state (atom (load-init-state))]
    (r/mount (wrapper init-state) (js/document.getElementById "application"))
    (ctrls/start! init-state)))
