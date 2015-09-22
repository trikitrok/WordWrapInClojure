(ns word-wrap.core
  (:require [clojure.string :as string]))

(def ^:private to-trimmed-string
  (comp string/trim (partial apply str)))

(defn- rest-of-line [line index]
  (to-trimmed-string (drop index line)))

(defn- wrap-line-at [line index]
  (str (to-trimmed-string (take index line)) "\n"))

(def ^:private indexes (partial map first))

(defn- space? [[_ character]]
  (= character \space))

(defn- spaces-indexes [line]
  (indexes (filter space? (map-indexed #(vector %1 %2) line))))

(defn- fitting-spaces-indexes [line max-columns]
  (filter #(< % max-columns) (spaces-indexes line)))

(def ^:private not-found -1)

(defn- index-of-last-fitting-space [line max-columns]
  (if-let [index (last (fitting-spaces-indexes line max-columns))]
    index
    not-found))

(def ^:private valid-index? pos?)

(defn- compute-wrapping-index [line max-columns]
  (let [index (index-of-last-fitting-space line max-columns)]
    (if (valid-index? index)
      index
      max-columns)))

(defn- fits? [line max-columns]
  (<= (count line) max-columns))

(defn- line->wrapped-lines [wrapped-lines line max-columns]
  (if (fits? line max-columns)
    (conj wrapped-lines line)
    (let [index (compute-wrapping-index line max-columns)]
      (recur (conj wrapped-lines (wrap-line-at line index))
             (rest-of-line line index)
             max-columns))))

(defn- wrap-line [line max-columns]
  (apply str (line->wrapped-lines [] line max-columns)))

(defn- extract-lines [text]
  (string/split text #"\n"))

(def ^:private join-lines (partial string/join "\n"))

(defn wrap [text max-columns]
  (->> text
       extract-lines
       (map #(wrap-line % max-columns))
       join-lines))
