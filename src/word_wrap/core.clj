(ns word-wrap.core
  (:require [clojure.string :as string]))

(defn- rest-of-line [line index]
  (string/trim (apply str (drop index line))))

(defn- wrap-line-at [line index]
  (str (string/trim (apply str (take index line))) "\n"))

(def ^:private indexes (partial map first))

(defn- space? [[_ character]]
  (= character \space))

(defn- spaces-indexes [line]
  (indexes (filter space? (map-indexed #(vector %1 %2) line))))

(defn- fitting-spaces-indexes [line num-columns]
  (filter #(< % num-columns) (spaces-indexes line)))

(def ^:private not-found -1)

(defn- compute-last-space-index [line num-columns]
  (if-let [index (last (fitting-spaces-indexes line num-columns))]
    index
    not-found))

(def ^:private not-found? pos?)

(defn- compute-wrapping-index [line num-columns]
  (let [index (compute-last-space-index line num-columns)]
    (if (not-found? index)
      index
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
  (string/split text #"\n"))

(def ^:private join-lines (partial string/join "\n"))

(defn wrap [text num-columns]
  (->> text
       extract-lines
       (map #(wrap-line % num-columns))
       join-lines))
