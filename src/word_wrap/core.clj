(ns word-wrap.core)

(defn- rest-of-line [line position]
  (clojure.string/trim (apply str (drop position line))))

(defn- wrap-line-at [line position]
  (str (clojure.string/trim (apply str (take position line))) "\n"))

(defn- space-fits? [[index character] num-columns]
  (and (= character \space) (< index num-columns)))

(defn- get-fitting-spaces-positions [line num-columns]
  (map first (filter #(space-fits? % num-columns)
                     (map-indexed #(vector %1 %2) line))))

(defn- compute-last-space-position [line num-columns]
  (let [fitting-spaces-positions (get-fitting-spaces-positions line num-columns)]
    (if (empty? fitting-spaces-positions)
      -1
      (last fitting-spaces-positions))))

(def ^:private valid-position? pos?)

(defn- compute-wrapping-position [line num-columns]
  (let [last-space-position (compute-last-space-position line num-columns)]
    (if (valid-position? last-space-position)
      last-space-position
      num-columns)))

(defn- fits? [line num-columns]
  (<= (count line) num-columns))

(defn- line->wrapped-lines [wrapped-lines line num-columns]
  (if (fits? line num-columns)
    (conj wrapped-lines line)
    (let [wrappping-position (compute-wrapping-position line num-columns)]
      (recur (conj wrapped-lines (wrap-line-at line wrappping-position))
             (rest-of-line line wrappping-position)
             num-columns))))

(defn- wrap-line [line num-columns]
  (apply str (line->wrapped-lines [] line num-columns)))

(defn- extract-lines [text]
  (clojure.string/split text #"\n"))

(def ^:private join-lines (partial clojure.string/join "\n"))

(defn wrap [text num-columns]
  (->> text
       extract-lines
       (map #(wrap-line % num-columns))
       join-lines))
