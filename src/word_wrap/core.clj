(ns word-wrap.core)

(defn- wrap-word-at [word position]
  (str (clojure.string/trim (apply str (take position word)))
       "\n"
       (clojure.string/trim (apply str (drop position word)))))

(defn wrap [word num-columns]
  (if (<= (count word) num-columns)
    word
    (let [space-position (.indexOf word (str \space))]
      (if (and (pos? space-position)
               (<= space-position num-columns))
        (wrap-word-at word space-position)
        (wrap-word-at word num-columns)))))