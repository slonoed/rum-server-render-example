(ns user
  (:use org.httpkit.server)
  (:require [books.core :as b]
            [clojure.tools.namespace.repl :refer [refresh]]))

(defonce server (atom nil))


(defn go []
  (reset! server (run-server #'b/app {:port 8080})))

(defn reset
  []
  (when-not (nil? @server)
     (@server :timeout 100)
     (reset! server nil))
  (refresh :after 'user/go))
