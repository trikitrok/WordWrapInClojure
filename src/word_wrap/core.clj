(ns word-wrap.core)

(defn wrap [word num-columns]
  (if (<= (count word) num-columns)
    word
    (str (apply str (take num-columns word))
       "\n"
       (apply str (drop num-columns word)))))