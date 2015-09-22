(ns word-wrap.core)

(defn- rest-of-line [line position]
  (clojure.string/trim (apply str (drop position line))))

(defn- wrap-line-at [line position]
  (str (clojure.string/trim (apply str (take position line)))
       "\n"))

(defn- get-fitting-spaces-positions [line num-columns]
  (filter #(and (= (second %) \space) (< (first %) num-columns))
          (map-indexed #(vector %1 %2) line)))

(defn- compute-last-space-position [line num-columns]
  (let [fitting-spaces-positions (get-fitting-spaces-positions line num-columns)]
    (if (empty? fitting-spaces-positions)
      -1
      (first (last fitting-spaces-positions)))))

(def ^:private valid-position? pos?)

(defn- compute-wrapping-position [line num-columns]
  (let [last-space-position (compute-last-space-position line num-columns)]
    (if (valid-position? last-space-position)
      last-space-position
      num-columns)))

(defn- fits? [line num-columns]
  (<= (count line) num-columns))

(defn wrap [line num-columns]
  (if (fits? line num-columns)
    line
    (let [wrappping-position (compute-wrapping-position line num-columns)]
      (str (wrap-line-at line wrappping-position)
           (wrap (rest-of-line line wrappping-position) num-columns)))))
