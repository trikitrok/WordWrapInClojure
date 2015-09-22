(ns word-wrap.core)

(defn- rest-of-line [line index]
  (clojure.string/trim (apply str (drop index line))))

(defn- wrap-line-at [line index]
  (str (clojure.string/trim (apply str (take index line))) "\n"))

(def ^:private indexes (partial map first))

(defn- space? [[_ character]]
  (= character \space))

(defn- spaces-indexes [line]
  (indexes (filter space? (map-indexed #(vector %1 %2) line))))

(defn- fitting-spaces-indexes [line num-columns]
  (filter #(< % num-columns) (spaces-indexes line)))

(defn- compute-last-space-index [line num-columns]
  (if-let [index (last (fitting-spaces-indexes line num-columns))]
    index
    -1))

(def ^:private valid-index? pos?)

(defn- compute-wrapping-index [line num-columns]
  (let [last-space-index (compute-last-space-index line num-columns)]
    (if (valid-index? last-space-index)
      last-space-index
      num-columns)))

(defn- fits? [line num-columns]
  (<= (count line) num-columns))

(defn- line->wrapped-lines [wrapped-lines line num-columns]
  (if (fits? line num-columns)
    (conj wrapped-lines line)
    (let [index (compute-wrapping-index line num-columns)]
      (recur (conj wrapped-lines (wrap-line-at line index))
             (rest-of-line line index)
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
