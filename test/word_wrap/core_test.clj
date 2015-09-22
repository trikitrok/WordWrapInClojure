(ns word-wrap.core-test
  (:use midje.sweet)
  (:use [word-wrap.core]))

(facts
  "about wrapping words"

  (fact
    "words that fit in the given columns number are not wrapped"
    (wrap "koko koko" 9) => "koko koko")

  (fact
    "words without spaces that don't fit in the given columns number are wrapped"
    (wrap "kokokoko" 4) => "koko\nkoko")

  (fact
    "words with spaces that don't fit in the given columns number are wrapped
    at the space that is closest to the maximum column"
    (wrap "koko koko" 7) => "koko\nkoko"
    (wrap "koko koko koko" 12) => "koko koko\nkoko"
    (wrap "koko koko koko koko koko koko" 12) => "koko koko\nkoko koko\nkoko koko"))
