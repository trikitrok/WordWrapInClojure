(ns word-wrap.core
  (:require [clojure.string :as string]))

(def ^:private to-trimmed-string
  (comp string/trim (partial apply str)))

(def ^:private rest-of-line
  (comp to-trimmed-string (partial drop)))

(defn- wrap-line-at [index line]
  (str (to-trimmed-string (take index line)) \newline))

(def ^:private indexes (partial map first))

(defn- space? [[_ character]]
  (= character \space))

(defn- spaces-indexes [line]
  (->> line
       (map-indexed #(vector %1 %2))
       (filter space?)
       indexes))

(def ^:private not-found -1)

(defn- last-or-else-not-found [indexes]
  (if-let [index (last indexes)]
    index
    not-found))

(defn- index-of-last-fitting-space [max-columns line]
  (->> line
       spaces-indexes
       (filter #(< % max-columns))
       last-or-else-not-found))

(def ^:private valid-index? pos?)

(defn- compute-wrapping-index [line max-columns]
  (let [index (index-of-last-fitting-space max-columns line)]
    (if (valid-index? index)
      index
      max-columns)))

(defn- fits? [line max-columns]
  (<= (count line) max-columns))

(defn- line->wrapped-lines [wrapped-lines line max-columns]
  (if (fits? line max-columns)
    (conj wrapped-lines line)
    (let [index (compute-wrapping-index line max-columns)]
      (recur (conj wrapped-lines (wrap-line-at index line))
             (rest-of-line index line)
             max-columns))))

(defn- wrap-line [line max-columns]
  (apply str (line->wrapped-lines [] line max-columns)))

(defn- extract-lines [text]
  (string/split text #"\n"))

(def ^:private join-lines (partial string/join \newline))

(defn wrap [text max-columns]
  (->> text
       extract-lines
       (map #(wrap-line % max-columns))
       join-lines))
