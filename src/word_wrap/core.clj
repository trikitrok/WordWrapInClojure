(ns word-wrap.core)

(defn- rest-of-words [word position]
  (clojure.string/trim (apply str (drop position word))))

(defn- wrap-word-at [word position]
  (str (clojure.string/trim (apply str (take position word)))
       "\n"))

(defn- get-fitting-spaces-positions [word num-columns]
  (filter #(and (= (second %) \space) (< (first %) num-columns))
          (map-indexed #(vector %1 %2) word)))

(defn- compute-last-space-position [word num-columns]
  (let [fitting-spaces-positions (get-fitting-spaces-positions word num-columns)]
    (if (empty? fitting-spaces-positions)
      -1
      (first (last fitting-spaces-positions)))))

(def ^:private valid-position? pos?)

(defn- compute-wrapping-position [word num-columns]
  (let [last-space-position (compute-last-space-position word num-columns)]
    (if (valid-position? last-space-position)
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