(ns word-wrap.core)

(defn- rest-of-words [word position]
  (clojure.string/trim (apply str (drop position word))))

(defn- wrap-word-at [word position]
  (str (clojure.string/trim (apply str (take position word)))
       "\n"))

(defn- compute-last-space-position [word num-columns]
  (let [space-positions (filter #(and (= (second %) \space) (< (first %) num-columns))
                                      (map-indexed #(vector %1 %2) word))]
    (if (empty? space-positions)
      -1
      (first (last space-positions)))))

(defn- compute-wrapping-position [word num-columns]
  (let [last-space-position (compute-last-space-position word num-columns)]
    (if (and (pos? last-space-position)
             (<= last-space-position num-columns))
      last-space-position
      num-columns)))

(defn- fits? [word num-columns]
  (<= (count word) num-columns))

(defn wrap [word num-columns]
  (if (fits? word num-columns)
    word
    (let [wrappping-position (compute-wrapping-position word num-columns)]
      (str (wrap-word-at word wrappping-position)
           (wrap (rest-of-words word wrappping-position) num-columns)))))