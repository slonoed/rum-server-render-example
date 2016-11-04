(ns books.core)

(defn app
  [req]
  {:status  200
   :headers {"Content-Type" "text/html"}
   :body    "hello clojure!"})
