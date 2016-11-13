(ns books.components
  (:require [rum.core :as r]
            #?(:cljs [books.controllers :as c])))

(r/defc link [[title year author]]
  [:li
   [:a {:href (str "/" title)} title]])

(r/defc books-list [books]
  [:.list
   [:ul
    (map link books)]
   [:a.btn {:href "/add"} "Add book"]])

(r/defc details [[title year author]]
  [:.details
   [:a {:href "/"} "List"]
   [:label "Title"] title
   [:label "Author"] author
   [:label "Year"] year])

(r/defc add-form []
  [:.modal
   [:form {:action "/"
           :method "POST"
           :on-submit #?(:clj nil
                         :cljs (fn [e]
                                 (.preventDefault e)
                                 (c/add-book ["Hello" 12324 "Dmitry"])))}
    [:input {:type "text" :name "title"  :placeholder "Title"}]
    [:input {:type "text" :name "author" :placeholder "Author"}]
    [:input {:type "text" :name "year"   :placeholder "Year"}]
    [:button.btn {:type "submit"} "Add"]
    [:a.btn {:href "/"} "Cancel"]]])

(r/defc app [{:keys [books route]}]
  [:.page
   ;; If route match any book title - show detailed view
   ;; else - show list
   (if-let [book (some #(when (= route (first %)) %) books)]
     (details book)
     (books-list books))
   ;; If route is "add" – render additional modal window with form
   (when (= route "add")
     (add-form))])
