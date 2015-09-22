(ns word-wrap.core)

(defn- rest-of-words [word position]
  (clojure.string/trim (apply str (drop position word))))

(defn- wrap-word-at [word position]
  (str (clojure.string/trim (apply str (take position word)))
       "\n"))

(defn- compute-wrapping-position [word num-columns]
  (let [space-position (.indexOf word (str \space))]
    (if (and (pos? space-position)
             (<= space-position num-columns))
      space-position
      num-columns)))

(defn wrap [word num-columns]
  (if (<= (count word) num-columns)
    word
    (let [wrappping-position (compute-wrapping-position word num-columns)]
      (str (wrap-word-at word wrappping-position)
           (wrap (rest-of-words word wrappping-position) num-columns)))))