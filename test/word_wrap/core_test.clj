(ns word-wrap.core-test
  (:use midje.sweet)
  (:use [word-wrap.core]))

(facts
  "about wrapping words"

  (fact
    "words that fit in the given columns number are not wrapped"
    (wrap "koko koko" 9) => "koko koko"
    (wrap "ko" 9) => "ko"
    (wrap "" 9) => "")

  (fact
    "words without spaces that don't fit in the given columns number are wrapped"
    (wrap "kokokoko" 4) => "koko\nkoko")

  (fact
    "words with spaces that don't fit in the given columns number are wrapped
    at the space that is closest to the maximum column"
    (wrap "koko koko" 7) => "koko\nkoko"
    (wrap "koko koko koko" 12) => "koko koko\nkoko"
    (wrap "koko koko koko koko koko koko" 12) => "koko koko\nkoko koko\nkoko koko"
    (wrap
      "This kata should be easy unless there are hidden, or not so hidden, obstacles. Let's start!"
      12) => "This kata\nshould be\neasy unless\nthere are\nhidden, or\nnot so\nhidden,\nobstacles.\nLet's start!"))
