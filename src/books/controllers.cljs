(ns books.controllers
  (:import [goog.history Html5History EventType]
           [goog History]
           [goog.net XhrIo]
           [goog.date Date]))

(defonce state nil)
(defonce history nil)


(defn local-link? [a]
  (and (= (.-tagName a) "A")
       (goog.string/startsWith (.-href a)
                               (str
                                 js/location.protocol
                                 "//"
                                 js/location.host))))

;; set :route field in state when routing changed
(defn handle-url-change [e]
  (let [t (.slice (.-token e) 1)
        t' (js/decodeURIComponent t)]
    (swap! state assoc :route t')))

(defn start! [state']
  (set! state state')
  ;; Setup history
  (set! history (Html5History.))
  (doto history
    (.setPathPrefix "")
    (.setUseFragment false)
    (goog.events/listen EventType.NAVIGATE
                        #(handle-url-change %))
    (.setEnabled true))

;; Small hack. When use click link on page we check is this same domain link
;; and instead reloading page only change history
(goog.events/listen js/document.body "click"
                    (fn [e]
                      (when (local-link? (.-target e))
                        (.preventDefault e)
                        (.setToken history (.. e -target -pathname))))))


(defn add-book [[title year author :as book]]
  (let [esc #(js/encodeURIComponent %)
        form-data (str "title=" (esc title) "&"
                       "year=" (esc title) "&"
                       "author=" (esc author))
        callback (fn []
                   (swap! state update :books conj book)
                   (.setToken history "/"))]
    (.send XhrIo "/" callback "POST" form-data)))
