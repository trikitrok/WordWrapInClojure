(ns word-wrap.core-test
  (:use midje.sweet)
  (:use [word-wrap.core]))

(facts
  "about wrapping words"

  (fact
    "words that fit in the given columns number are not wrapped"
    (wrap "koko koko" 9) => "koko koko"))
